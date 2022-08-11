package TestDataGenerator;


import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;

import java.io.File;
import java.io.OutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Request {

    static BasicCookieStore cookieStore ;

    public static void post(String path, Map<String, String> headers, File file) {
        try {
            HttpClientContext context = new HttpClientContext();
            CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).setProxy(HttpHost.create("http://localhost:8080"))
                    .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build())
                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                    .build();
            HttpPost post = new HttpPost(Constant.baseUrl + path);
            headers.entrySet().forEach(e ->
            {
                post.setHeader(e.getKey(), e.getValue());
            });

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = httpclient.execute(post, responseHandler, context);
            cookieStore = (BasicCookieStore) context.getCookieStore();

        } catch (Exception e)
        {

        }


    }

    public static void get(String path, Map<String, String> headers, Map<String, String> params) {
        try {
            HttpClientContext context = new HttpClientContext();
            CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).setProxy(HttpHost.create("http://localhost:8080"))
                    .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build())
                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                    .build();
            HttpGet get = new HttpGet(Constant.baseUrl + path);
            headers.entrySet().forEach(e ->
            {
                get.setHeader(e.getKey(), e.getValue());
            });
            List nameValuePairs = new ArrayList();
            params.entrySet().forEach((e) -> {
                nameValuePairs.add(new BasicNameValuePair(e.getKey(), e.getValue()));
            });
            URI uri = new URIBuilder(get.getURI())
                    .addParameters(nameValuePairs)
                    .build();
            ((HttpRequestBase) get).setURI(uri);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = httpclient.execute(get, responseHandler, context);
             cookieStore = (BasicCookieStore) context.getCookieStore();

        } catch (Exception e) {

        }

    }
}
