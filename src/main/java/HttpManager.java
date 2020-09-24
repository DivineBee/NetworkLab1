/**
 * @author Beatrice V.
 * @created 07.09.2020 - 17:42
 * @project NetworkLab1
 */

import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;

public class HttpManager {

    public static String url = "http://localhost:5000";
    public static String registerAddress = "/register";
    public static String homeAddress = "/home";
    public static String token;

    public HttpManager() {
    }

    public static void getToken(){
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url + registerAddress);

        try {
            HttpResponse response = client.execute(request);
            ResponseHandler<String> handler = new BasicResponseHandler();
            String text = handler.handleResponse(response);
            token = text.split("\"")[3];
            System.out.println(token);
        } catch (IOException e) {
            System.out.println("Couldn't get token!" + e);
        }
    }

    public static String getPageContents(String address) {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(address);
        request.addHeader("X-Access-Token", token);
        String pageContent = null;
        try{
            HttpResponse response = client.execute(request);
            ResponseHandler<String> handler = new BasicResponseHandler();
            pageContent = handler.handleResponse(response);
            client.close();
        } catch (IOException e) { System.err.println("Can't read page content, try again!\n" + e); }
        return pageContent;
    }
}
