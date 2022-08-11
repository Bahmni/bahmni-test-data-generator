package TestDataGenerator;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;

import javax.net.ssl.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Properties;

public class Request
{



    public static void Post (String path,Map<String,String> headers,String body)
    {
         final HttpURLConnection con ;
        try {
            URL app = new URL(Constant.baseUrl+path);
            con= (HttpURLConnection) app.openConnection();
            con.setRequestMethod("POST");
            headers.entrySet().forEach(e->
            {
                con.setRequestProperty(e.getKey(),e.getValue());
            });
            con.setDoOutput(true);
            try(OutputStream os = con.getOutputStream())
            {
                byte[] input = body.getBytes("utf-8");
                os.write(input, 0, input.length);
                os.flush();
                os.close();
            }
            System.out.println(con.getResponseCode());
            con.disconnect();
        }
        catch (Exception e)
        {
            System.out.println(e.getLocalizedMessage());
        }


    }
    public static void Get (String path,Map<String,String> headers,Map<String,String> params) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        /*final HttpURLConnection con ;
        System.setProperty("com.sun.net.ssl.checkRevocation","false");
        try {
            TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
                public void checkClientTrusted(X509Certificate[] certs, String authType) { }
                public void checkServerTrusted(X509Certificate[] certs, String authType) { }

            } };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

// Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) { return true; }
            };
// Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
            URL app = new URL(Constant.baseUrl+path);
            con= (HttpURLConnection) app.openConnection();
            con.setRequestMethod("GET");
            headers.entrySet().forEach(e->
            {
                con.setRequestProperty(e.getKey(),e.getValue());
            });
            con.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes(getParamsString(params));
            out.flush();
            out.close();
            System.out.println(con.getResponseCode());
            con.disconnect();
        }
        catch (Exception e)
        {
            System.out.println(e.getLocalizedMessage());
        }*/
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build())
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .build();
        HttpGet get= new HttpGet(Constant.baseUrl+path);
        headers.entrySet().forEach(e->
        {
            get.setHeader(e.getKey(),e.getValue());
        });
        ResponseHandler<String> responseHandler=new BasicResponseHandler();
        String responseBody = httpclient.execute(get, responseHandler);
        System.out.println(responseBody);


    }
    public static String getParamsString(Map<String, String> params)
            throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            result.append("&");
        }

        String resultString = result.toString();
        return resultString.length() > 0
                ? resultString.substring(0, resultString.length() - 1)
                : resultString;
    }

   // public

}
