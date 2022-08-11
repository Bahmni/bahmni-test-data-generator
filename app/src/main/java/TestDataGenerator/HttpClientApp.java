package TestDataGenerator;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Base64;

public class HttpClientApp {

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    public static void invoke(String uname,String password) throws IOException, InterruptedException {

        String path="/openmrs/ws/rest/v1/user";
        String encoding = Base64.getEncoder().encodeToString((uname + ":" + password).getBytes());

        HttpRequest request = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create(Constant.baseUrl+path))
            .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
            .setHeader("Accept","application/json, text/plain, */*")
            .setHeader("Accept-Encoding","gzip, deflate, sdch, br")
            .setHeader("Authorization", "Basic " + encoding)
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // print response headers
        HttpHeaders headers = response.headers();
        headers.map().forEach((k, v) -> System.out.println(k + ":" + v));

        // print status code
        System.out.println(response.statusCode());

        // print response body
        System.out.println(response.body());

    }

}


