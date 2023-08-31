package Api;


import Config.LoggerConfig;
import Constants.Constant;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class Request {

    private static BasicCookieStore cookieStore;
    private static String uuid;

    public static void setUuid(String uuid) {
        Request.uuid = uuid;
    }

    public static String getUuid() {
        return uuid;
    }

    public static BasicCookieStore getCookieStore() {
        return cookieStore;
    }

    HttpClientContext context = null;
    HttpGet get = null;
    HttpPost post = null;
    Logger logger = LoggerConfig.LOGGER;

    protected void post(String path, Map<String, String> headers, String file) {
        HttpResponse response = null;
        try {
            context = new HttpClientContext();
            BasicClientCookie cookie = new BasicClientCookie(
                    "bahmni.user.location",
                    URLEncoder.encode("{name:" + Constant.LOCATION + ",uuid:" + uuid + "}", StandardCharsets.UTF_8)
            );
            String domain = Constant.BASEURL.split("//")[1];
            cookie.setDomain(domain);
            cookie.setPath("/");
            BasicClientCookie cookie1 = new BasicClientCookie(
                    "app.clinical.grantProviderAccessData", URLEncoder.encode("null", StandardCharsets.UTF_8));
            cookie1.setDomain(domain);
            cookie1.setPath("/");
            BasicClientCookie cookie2 = new BasicClientCookie("bahmni.user", URLEncoder.encode(Constant.USERNAME, StandardCharsets.UTF_8));
            cookie2.setDomain(domain);
            cookie2.setPath("/");
            cookieStore.addCookie(cookie);
            cookieStore.addCookie(cookie1);
            cookieStore.addCookie(cookie2);

            HttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore)
                    .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build())
                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                    .build();

            post = new HttpPost(Constant.BASEURL + path);
            headers.forEach(post::setHeader);
            File csvfile = new File(file);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            String[] bits = file.split("/");
            String csvFileName = bits[bits.length - 1];
            builder.addBinaryBody("file", csvfile, ContentType.MULTIPART_FORM_DATA, csvFileName);
            HttpEntity entity = builder.build();
            post.setEntity(entity);
            response = httpclient.execute(post, context);
            logger.info(context.getRequest().toString());
            logger.info("Response Status is : " + response.getStatusLine().getStatusCode());
            Assertions.assertEquals(200, response.getStatusLine().getStatusCode());
            cookieStore = (BasicCookieStore) context.getCookieStore();

        } catch (Exception e) {
            assert response != null;
            for (String s : Arrays.asList(response.getStatusLine().toString(), e.getLocalizedMessage())) {
                logger.severe(s);
            }
        }

    }

    protected HttpResponse get(String path, Map<String, String> headers, Map<String, String> params) {
        HttpResponse response = null;
        try {
            context = new HttpClientContext();
            CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore)
                    .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build())
                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                    .build();

            get = new HttpGet(Constant.BASEURL + path);
            headers.forEach(get::setHeader);

            List<NameValuePair> nameValuePairs = new ArrayList<>();
            params.forEach((key, value) -> nameValuePairs.add(new BasicNameValuePair(key, value)));

            URI uri = new URIBuilder(get.getURI())
                    .addParameters(nameValuePairs)
                    .build();
            get.setURI(uri);
            response = httpclient.execute(get, context);
            logger.info(context.getRequest().toString());
            logger.info("Response Status is : " + response.getStatusLine().getStatusCode());
            cookieStore = (BasicCookieStore) context.getCookieStore();

        } catch (Exception e) {
            assert response != null;
            for (String s : Arrays.asList(response.getStatusLine().toString(), e.getLocalizedMessage())) {
                logger.severe(s);
            }
        }
        return response;
    }

    protected HttpResponse get(String path, Map<String, String> headers) {
        HttpResponse response = null;
        try {
            context = new HttpClientContext();
            CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore)
                    .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build())
                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                    .build();

            get = new HttpGet(Constant.BASEURL + path);
            headers.forEach(get::setHeader);
            response = httpclient.execute(get, context);
            logger.info(context.getRequest().toString());
            logger.info("Status is : " + response.getStatusLine().getStatusCode());
            cookieStore = (BasicCookieStore) context.getCookieStore();

        } catch (Exception e) {
            assert response != null;
            logger.severe(e.getLocalizedMessage());
            logger.severe(response.getStatusLine().toString());
        }
        return response;
    }

    protected void post(String requestBody, String path, Map<String, String> headers) {
        HttpResponse response = null;
        try {
            context = new HttpClientContext();
            BasicClientCookie cookie = new BasicClientCookie(
                    "bahmni.user.location",
                    URLEncoder.encode("{name:" + Constant.LOCATION + ",uuid:" + uuid + "}", StandardCharsets.UTF_8)
            );
            String domain = Constant.BASEURL.split("//")[1];
            cookie.setDomain(domain);
            cookie.setPath("/");
            BasicClientCookie cookie1 = new BasicClientCookie(
                    "app.clinical.grantProviderAccessData", URLEncoder.encode("null", StandardCharsets.UTF_8));
            cookie1.setDomain(domain);
            cookie1.setPath("/");
            BasicClientCookie cookie2 = new BasicClientCookie("bahmni.user", URLEncoder.encode(Constant.USERNAME, StandardCharsets.UTF_8));
            cookie2.setDomain(domain);
            cookie2.setPath("/");
            cookieStore.addCookie(cookie);
            cookieStore.addCookie(cookie1);
            cookieStore.addCookie(cookie2);

            HttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore)
                    .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build())
                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                    .build();

            post = new HttpPost(Constant.BASEURL + path);
            headers.forEach(post::setHeader);
            post.setHeader("Accept", "application/json");
            post.setHeader("Content-type", "application/json");

            StringEntity entity;
            if (requestBody != null) {
                entity = new StringEntity(requestBody);
                post.setEntity(entity);
                System.out.println(requestBody);
            } else {
                post.setEntity(null);
            }

            response = httpclient.execute(post, context);
            logger.info(context.getRequest().toString());
            logger.info("Response Status is : " + response.getStatusLine().getStatusCode());
            cookieStore = (BasicCookieStore) context.getCookieStore();

        } catch (Exception e) {
            assert response != null;
            for (String s : Arrays.asList(response.getStatusLine().toString(), e.getLocalizedMessage())) {
                logger.severe(s);
            }
        }

    }
}
