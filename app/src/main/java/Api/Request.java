package Api;


import Constants.Constant;
import org.apache.http.*;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Request {

    private static BasicCookieStore cookieStore ;
    private static String uuid;

    public static void setUuid(String uuid) {
        Request.uuid = uuid;
    }

    public static String getUuid()
    {
        return uuid;
    }
    public static BasicCookieStore getCookieStore() {
        return cookieStore;
    }

    public  HttpResponse post(String path, Map<String, String> headers, String file)
    {
        HttpResponse response = null;
        try {
            HttpClientContext context = new HttpClientContext();
            BasicClientCookie cookie=new BasicClientCookie("bahmni.user.location",URLEncoder.encode("{name:"+ Constant.LOCATION+",uuid:"+uuid+"}", StandardCharsets.UTF_8));

            cookie.setDomain(Constant.BASEURL.split("//")[1]);
            cookie.setPath("/");
            BasicClientCookie cookie1=new BasicClientCookie("app.clinical.grantProviderAccessData",URLEncoder.encode("null", StandardCharsets.UTF_8));
            cookie1.setDomain(Constant.BASEURL.split("//")[1]);
            cookie1.setPath("/");
            BasicClientCookie cookie2=new BasicClientCookie("bahmni.user",URLEncoder.encode(Constant.USERNAME, StandardCharsets.UTF_8));
            cookie2.setDomain(Constant.BASEURL.split("//")[1]);
            cookie2.setPath("/");
            cookieStore.addCookie(cookie);
            cookieStore.addCookie(cookie1);
            cookieStore.addCookie(cookie2);

            CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore)
                    .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build())
                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                    .build();

            HttpPost post = new HttpPost(Constant.BASEURL + path);

            headers.entrySet().forEach(e ->
            {
                post.setHeader(e.getKey(), e.getValue());
            });
            File csvfile = new File(file);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            String[] bits = file.split("/");
            String csvFileName = bits[bits.length-1];
            builder.addBinaryBody("file", csvfile, ContentType.MULTIPART_FORM_DATA, csvFileName);
            HttpEntity entity = builder.build();
            post.setEntity(entity);
            response=httpclient.execute(post,context);
            Assertions.assertEquals(response.getStatusLine().getStatusCode(),200);
            cookieStore = (BasicCookieStore) context.getCookieStore();

        } catch (Exception e)
        {

        }

            return response;
    }

    public  HttpResponse get(String path, Map<String, String> headers, Map<String, String> params)
    {
        HttpResponse response=null;
        try {
            HttpClientContext context = new HttpClientContext();
            CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore)
                    .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build())
                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                    .build();

            HttpGet get = new HttpGet(Constant.BASEURL + path);
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
            response=httpclient.execute(get,context);
            Assertions.assertEquals(response.getStatusLine().getStatusCode(),200);
             cookieStore = (BasicCookieStore) context.getCookieStore();

        } catch (Exception e) {

        }
        return response;
    }
    public HttpResponse get(String path, Map<String, String> headers)
    {
        HttpResponse response=null;
        try {
            HttpClientContext context = new HttpClientContext();
            CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore)
                    .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build())
                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                    .build();

            HttpGet get = new HttpGet(Constant.BASEURL + path);
            headers.entrySet().forEach(e ->
            {
                get.setHeader(e.getKey(), e.getValue());
            });
            response=httpclient.execute(get,context);
            Assertions.assertEquals(response.getStatusLine().getStatusCode(),200);
            cookieStore = (BasicCookieStore) context.getCookieStore();

        } catch (Exception e) {

        }
        return response;
    }
}
