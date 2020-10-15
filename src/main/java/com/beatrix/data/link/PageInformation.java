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
import java.util.concurrent.atomic.AtomicInteger;

public class PageInformation {
    // concurrent storage of all found routes that must be processed
    private static BlockingQueue<String> concurrentRoutes = new LinkedBlockingQueue<>();
    //  counter of active threads at moment
    private static AtomicInteger activeThreadsCount = new AtomicInteger(0);
    // token of connection
    public static String token = null;
    // url for connection
    public static final String url = "http://localhost:5000";
    // postfixes for getting to correct urls
    private static final String registerAddress = "/register";
    public static final String homeAddress = "/home";

    // getters
    public static BlockingQueue<String> getConcurrentRoutes() {
        return concurrentRoutes;
    }
    public static AtomicInteger getActiveThreadsCount() { return activeThreadsCount; }

    /**
     * This method gets http request to the url register address and reads the token
     * through buffered reader and extracts the token which will be next used in accessing
     * future routes
     */
    public static void getToken(){
        // initialize http client
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //  create get http request that will be transmitted via client
        HttpGet request = new HttpGet(url + registerAddress);

        try {
            // get response to request from page
            CloseableHttpResponse httpResponse = httpClient.execute(request);
            // get content (payload) from http response, removing http-related data
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
            // builder for appending multiple strings in one big text
            StringBuilder response = new StringBuilder();
            // string for reading line-by-line content of page
            String inputLine;

            // check if there is next line in text
            while ((inputLine = reader.readLine()) != null) {
                // if there is then append line to response text
                response.append(inputLine);
            }
            // close stream
            reader.close();

            // transform json-formatted text from page to tree (hashmap) of elements
            JsonNode parent = new ObjectMapper().readTree(response.toString());

            // find value attached to the key "access_token" in text of page
            token = parent.path("access_token").asText();
            System.out.println("Access Token: " + token);
        } catch (IOException e){
            System.out.println("Can't get Token.");
        }
    }

    /**
     * Method which gets the content on the page and reads it.
     * @param address of the link to be get
     * @return content present on the link
     */
    public static String getPageContent(String address) {
        //  initialize string for getting content of page
        String pageContent = null;
        //  create client for http
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //  make http-get request to address
        HttpGet request = new HttpGet(address);
        //  append token to the header of your http-get request
        request.addHeader("X-Access-Token", token);

        try {
            //  perform request via client
            HttpResponse response = httpClient.execute(request);
            System.out.println(request);

            //  handle response as string removing http-related data and append it to pageContent variable
            ResponseHandler<String> handler = new BasicResponseHandler();
            System.out.println(response);
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
     * @return work of getting all routes finished or not
     */
    public static boolean findRoute() throws InterruptedException {
        // Here is the checking if some routes weren't processed
        while(concurrentRoutes.size() > 0 || activeThreadsCount.get() > 0) {
//          // Number of routes which weren't processed
            if(concurrentRoutes.size() == 0)
                continue;

            // pop head route from queue of unhandled routes(takes and deletes it from queue)
            String currentRoute = concurrentRoutes.take();
            activeThreadsCount.getAndIncrement();
            // creates thread with already overwritten run() from MyRunnable and pass to its constructor - current route
            new Thread(new MyRunnable(currentRoute)).start();
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