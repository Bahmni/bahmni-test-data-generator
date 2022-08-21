package Openmrs;

import Api.Request;
import Jsonparser.Parser;
import org.apache.http.HttpResponse;
import org.junit.jupiter.api.Assertions;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class Openmrs extends Request{

    static Map<String,String> headers=new HashMap<>();
    {
        headers.put( "Accept","*/*");
        headers.put("Accept-Encoding","gzip, deflate, br");
        headers.put("Accept-Language","en-GB,en-US;q=0.9,en;q=0.8");
    };
    public HttpResponse login(String uname, String password) {
        Map<String,String> params=new HashMap<>();
        params.put("v","custom:(username,uuid,person:(uuid,),privileges:(name,retired),userProperties)");
        params.put("username","superman");
        String encoding = Base64.getEncoder().encodeToString((uname + ":" + password).getBytes());
        headers.put("Authorization", "Basic " + encoding);
        String path="/openmrs/ws/rest/v1/user";
        return get(path,headers,params);
    }
    public  HttpResponse getSessionId()
    {
        Map<String,String> params=new HashMap<>();
        params.put("v","custom:(uuid)");
        String path="/openmrs/ws/rest/v1/session";
        return get(path,headers,params);

    }
    public  HttpResponse setUserLocation(String loc)
    {
        Map<String,String> params=new HashMap<>();
        params.put("operator","ALL");
        params.put("s","byTags");
        params.put("tags","Login+Location");
        params.put("v","default");
        String path="/openmrs/ws/rest/v1/location";
        return get(path,headers,params);
    }
}
