package com.beatrix.data.link;
/**
 * @author Beatrice V.
 * @created 07.09.2020 - 18:11
 * @project NetworkLab1
 * This class contains work with a page's information.
 * Present methods are:
 * getToken()
 * getPageContent()
 * findRoute()
 */

import com.beatrix.data.DataManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class PageInformation {
    // concurrent storage of all found routes that must be processed
    private static BlockingQueue<String> concurrentRoutes = new LinkedBlockingQueue<>();

    // concurrent storage of all currently processing routes
    private static BlockingQueue<String> takenRoutes = new LinkedBlockingQueue<>();

    // token of connection
    public static String token = null;

    // url for connection
    public static final String url = "http://localhost:5000";

    // postfixes for getting to correct urls
    private static final String registerAddress = "/register";
    public static final String homeAddress = "/home";

    public static BlockingQueue<String> getConcurrentRoutes() {
        return concurrentRoutes;
    }

    /**
     * This method gets http request to the url register address and reads the token
     * through buffered reader and extracts the token which will be next used in accessing
     * future routes
     * @return access token for page
     */
    public static String getToken(){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(url + registerAddress);
        try {
            CloseableHttpResponse httpResponse = httpClient.execute(request);

            BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            reader.close();

            JsonNode parent = new ObjectMapper().readTree(response.toString());
            token = parent.path("access_token").asText();
            System.out.println("Access Token: " + token);
        } catch (IOException e){
            System.out.println("Can't get Token.");
        }
        return token;
    }

    /**
     * Method which gets the content on the page and reads it.
     * @param address of the link to be get
     * @return content present on the link
     */
    public static String getPageContent(String address) {
        String pageContent = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(address);
        request.addHeader("X-Access-Token", token);

        try {
            HttpResponse response = httpClient.execute(request);
            System.out.println(request);
            ResponseHandler<String> handler = new BasicResponseHandler();
            pageContent = handler.handleResponse(response);
            httpClient.close();
        } catch (IOException e){
            System.out.println("Can't get home page content.");
        }
        return pageContent;
    }
    /**
     * Method for getting unserialized data from page and finding new links mentioned inside page.
     * Works using iterative approach of threads creation using concurrent safe structure LinkedBlockingQueue.
     * Requires setting first route by user.
     * @see LinkedBlockingQueue
     * @return is work of getting all routes finished or not
     */
    public static boolean findRoute(){
        // Here is the checking if some routes weren't processed(обработанные)
        while(concurrentRoutes.size() > 0 || takenRoutes.size() > 0) {
            // Number of routes which weren't processed
            if(concurrentRoutes.size() == 0)
                // continue is used for waiting the thread to finish its job and don't interrupt it with throwing exception
                continue;

            // Creates as many Threads as there are unhandled routes
            new Thread(() -> {
                // Create mapper for primary deserialization
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    // pop head route from queue of unhandled routes(takes and deletes it from queue)
                    String currentRoute = concurrentRoutes.take();
                    //and place it to another list
                    takenRoutes.offer(currentRoute);

                    // Perform get request to current route and performs first partial deserialization
                    Route routeData = objectMapper.readValue(PageInformation.getPageContent(currentRoute), Route.class);

                    // Check if data from page contains links
                    if (routeData.getLink() != null && routeData.getLink().size() > 0) {
                        List<String> innerList = new ArrayList<>(routeData.getLink().values());
                        // Try to append found links to the queue of unhandled routes
                        for (int j = 0; j < innerList.size(); j++) {
                            // Try appending until offer() returns true(successful appending)
                            String fullpath = url + innerList.get(j);
                            concurrentRoutes.offer(fullpath);
                        }
                    }
                    // If it is not home address then add all data from page to database of json, xml and csv data.
                    if (!(url + homeAddress).equals(currentRoute)) {
                        DataManager.getDataFromServer().add(routeData);
                    }
                    takenRoutes.remove(currentRoute);
                } catch (IOException | InterruptedException ie) {
                    System.err.println("Can't read the links.\n" + ie);
                }
            }).start();
        }
        return true;
    }

    // Recursive way to find route
    public static void recursiveFindRoute(List<String> availableRoutes) {
        if(availableRoutes != null && availableRoutes.size() > 0) {
            for(int i = 0; i < availableRoutes.size(); i++){
                //considering that lambdas require work with final variables, was used this field
                final int finalI = i;
                new Thread(() -> {
                    ObjectMapper objectMapper = new ObjectMapper();
                    String currentRoute = availableRoutes.get(finalI);
                    try {
                        Route routeData = objectMapper.readValue(getPageContent(currentRoute), Route.class);
                        if(!(url+homeAddress).equals(currentRoute)){
                            DataManager.dataFromServer.add(routeData);
                        }
                        if(routeData.getLink() != null && routeData.getLink().size() > 0){
                            List<String> innerList = new ArrayList<>(routeData.getLink().values());
                            recursiveFindRoute(innerList.stream().map(route -> route = url + route).collect(Collectors.toList()));
                        }
                    } catch (IOException ie) {
                        System.err.println("Error reading links from server.\n" + ie);
                    }
                }).start();
            }
        } else System.err.println("No routes, check your connection or server!");
    }
}