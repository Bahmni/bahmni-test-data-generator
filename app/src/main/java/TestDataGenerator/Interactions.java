package TestDataGenerator;

import java.io.IOException;
import java.net.http.HttpHeaders;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class Interactions
{
    public static void login(String uname,String password) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        Map<String,String> params=new HashMap<>();
        params.put("v","custom:(username,uuid,person:(uuid,),privileges:(name,retired),userProperties)");
        params.put("username","superman");
        Map<String,String> headers=new HashMap<>();
        String encoding = Base64.getEncoder().encodeToString((uname + ":" + password).getBytes());
        headers.put("Accept","application/json, text/plain, */*");
        headers.put("Accept","Cache-Control, max-age=0, no-store");
        headers.put("Accept-Encoding","gzip, deflate, sdch, br");
        headers.put("Authorization", "Basic " + encoding);
        String path="/openmrs/ws/rest/v1/user";
        Request.Get(path,headers,params);

    }
    public  static void uploadPatient()
    {

    }

    public static void uploadEncounter()
    {

    }
}
