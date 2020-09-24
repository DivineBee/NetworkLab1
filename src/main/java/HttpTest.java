/**
 * @author Beatrice V.
 * @created 07.09.2020 - 18:11
 * @project NetworkLab1
 */

import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpTest {

    public static String token = null;
    private static String url = "http://localhost:5000";
    private static final String registerAddress = "/register";
    private static final String homeAddress = "/home";

    public static String getToken(){

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(url + registerAddress);

        try {
            CloseableHttpResponse httpResponse = httpClient.execute(request);

            BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            reader.close();

            // Extract token from json page and save it to 'token' variable
            //Using JSONObject
            JSONObject myResponse = new JSONObject(response.toString());
            System.out.println("result after Reading JSON Response");
            token = myResponse.getString("access_token");
            System.out.println("X Access Token: " + token);
            //Using Jackson
            /*JsonNode parent = new ObjectMapper().readTree(response.toString());
            String token = parent.path("access_token").asText();
            System.out.println("X Access Token by using jackson: " + token);*/

        } catch (IOException e){
            System.out.println("Can't get Token.");
        }
        return token;
    }

    public static String getPageContent(String address) {

        String pageContent = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(address);
        request.addHeader("X-Access-Token", token);

        try {
            HttpResponse response = httpClient.execute(request);
            ResponseHandler<String> handler = new BasicResponseHandler();
            pageContent = handler.handleResponse(response);
            System.out.println(pageContent);

            String pattern = "(/route/[0-9])+"; //(route_[0-9])|(/route/[0-9]))+
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(pageContent);

            List<String> routes = new ArrayList<String>();

            while (m.find()) {
                System.out.println("Found value: " + m.group(1) );
                routes.add(m.group(1));
            }

            System.out.println(routes);

            for(int i = 0; i < routes.size(); i++){
                CloseableHttpClient httpClient1 = HttpClients.createDefault();
                HttpGet request1 = new HttpGet(url + routes.get(i));
                request1.addHeader("X-Access-Token", token);

                HttpResponse response1 = httpClient.execute(request1);
                ResponseHandler<String> handler1 = new BasicResponseHandler();
                pageContent = handler1.handleResponse(response1);

                System.out.println(url + routes.get(i));
                System.out.println(pageContent);
            }
            httpClient.close();
        } catch (IOException e){
            System.out.println("Can't get home page content.");
        }
        return pageContent;
    }
}