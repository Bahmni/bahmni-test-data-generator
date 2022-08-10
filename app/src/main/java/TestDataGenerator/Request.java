package TestDataGenerator;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.net.http.HttpRequest;
import java.util.Map;

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
    public static void Get (String path,Map<String,String> headers,Map<String,String> params)
    {
        final HttpURLConnection con ;
        System.setProperty("com.sun.net.ssl.checkRevocation","false");
        try {
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
        }


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

}
