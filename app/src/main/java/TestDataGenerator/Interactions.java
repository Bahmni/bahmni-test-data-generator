package TestDataGenerator;

import org.apache.http.cookie.Cookie;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.io.IOException;
import java.net.CookieStore;
import java.net.URISyntaxException;
import java.net.http.HttpHeaders;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class Interactions
{

    public static void login(String uname,String password) {
        Map<String,String> params=new HashMap<>();
        params.put("v","custom:(username,uuid,person:(uuid,),privileges:(name,retired),userProperties)");
        params.put("username","superman");
        Map<String,String> headers=new HashMap<>();
        String encoding = Base64.getEncoder().encodeToString((uname + ":" + password).getBytes());
        headers.put("Accept","application/json, text/plain, */*");
        headers.put("Cache-Control", "max-age=0, no-store");
        headers.put("Accept-Encoding","gzip, deflate, sdch, br");
        headers.put("Authorization", "Basic " + encoding);
        String path="/openmrs/ws/rest/v1/user";
        Request.get(path,headers,params);
        Assertions.assertTrue(Request.getCookieStore().toString().contains("reporting_session"));
    }
    public static void getSessionId()
    {
        Map<String,String> params=new HashMap<>();
        Map<String,String> headers=new HashMap<>();
        params.put("v","custom:(uuid)");
        headers.put("Accept","application/json, text/plain, */*");
        String path="/openmrs/ws/rest/v1/session";
        Parser parser=new Parser(Request.get(path,headers,params));
        Assertions.assertEquals("true",parser.get("authenticated").toString());
        Assertions.assertTrue(Request.getCookieStore().toString().contains("JSESSIONID"));

    }
    public  static void uploadPatients()
    {
        Map<String,String> headers=new HashMap<>();
        headers.put( "Accept","*/*");
        headers.put("Accept-Encoding","gzip, deflate, br");
        headers.put("Accept-Language","en-GB,en-US;q=0.9,en;q=0.8");
        String path="/openmrs/ws/rest/v1/bahmnicore/admin/upload/patient";
        Request.post(path,headers,System.getProperty("user.dir")+"/"+Constant.patientProfileFileName);
    }
    public  static void uploadEncounters()
    {
        Map<String,String> headers=new HashMap<>();
        headers.put( "Accept","*/*");
        headers.put("Accept-Encoding","gzip, deflate, br");
        headers.put("Accept-Language","en-GB,en-US;q=0.9,en;q=0.8");
        String path="/openmrs/ws/rest/v1/bahmnicore/admin/upload/encounter";
        Request.post(path,headers,System.getProperty("user.dir")+"/"+Constant.contactProfileFileName);
    }


    public static  void setUserLocation(String loc)
    {
        Map<String,String> params=new HashMap<>();
        Map<String,String> headers=new HashMap<>();
        params.put("operator","ALL");
        params.put("s","byTags");
        params.put("tags","Login+Location");
        params.put("v","default");
        headers.put("Accept","application/json, text/plain, */*");
        String path="/openmrs/ws/rest/v1/location";
        Parser parser=new Parser(Request.get(path,headers,params));
        Map<String,String> map=Parser.zipToMap(parser.getValuesForGivenKey("name"),parser.getValuesForGivenKey("uuid"));
        Assertions.assertTrue(map.containsKey(loc));
        Request.setUuid(map.get(loc));
    }
    public static boolean verifyUpload() {
        boolean flag=false;
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json, text/plain, */*");
        String path = "/openmrs/ws/rest/v1/bahmnicore/admin/upload/status";
        Parser parser = new Parser(Request.get(path, headers));
        String status=parser.getStringFromArray("status");
        String fileName=parser.getStringFromArray("originalFileName");
        if (status.equals("COMPLETED"))
            flag= true;
        else if(status.equals("COMPLETED_WITH_ERRORS"))
            throw new RuntimeException(fileName+" : "+status);
        else if(status.equals("IN_PROGRESS"))
            verifyUpload();
            return flag;
    }
}
