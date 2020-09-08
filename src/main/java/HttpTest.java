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

public class HttpTest {

    public static String token = null;
    private static final String url = "http://localhost:5000";
    private static final String registerAddress = "/register";
    private static final String homeAddress = "/home";

    public static String getToken(){

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url + registerAddress);

        try {
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
            System.out.println("GET Response Status:: " + httpResponse.getStatusLine().getStatusCode());

            BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            reader.close();

            // Extract token from json page and save it to 'token' variable
            JSONObject myResponse = new JSONObject(response.toString());
            System.out.println("result after Reading JSON Response");
            token = myResponse.getString("access_token");
            System.out.println("X Access Token: " + token);

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
            System.out.println(pageContent.toString());
            httpClient.close();
        } catch (IOException e){
            System.out.println("Can't get home page content.");
        }
        return pageContent;
    }
}