package Bahmnicore;

import Api.Request;
import Constants.Constant;
import Jsonparser.Parser;
import org.apache.http.HttpResponse;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Bahmnicore extends Request{
    static Map<String,String> headers=new HashMap<>();
            {
                headers.put( "Accept","*/*");
                headers.put("Accept-Encoding","gzip, deflate, br");
                headers.put("Accept-Language","en-GB,en-US;q=0.9,en;q=0.8");
            };

    public HttpResponse uploadPatients()
    {

        String path="/openmrs/ws/rest/v1/bahmnicore/admin/upload/patient";
        return post(path,headers,System.getProperty("user.dir")+"/"+ Constant.PATIENT_PROFILE_FILE_NAME);
    }
    public   HttpResponse uploadEncounters()
    {

        String path="/openmrs/ws/rest/v1/bahmnicore/admin/upload/encounter";
        return post(path,headers,System.getProperty("user.dir")+"/"+Constant.ENCOUNTER_PROFILE_FILE_NAME);
    }

    public  boolean verifyUpload() throws IOException {
        boolean flag=false;
        String path = "/openmrs/ws/rest/v1/bahmnicore/admin/upload/status";
        Parser parser = new Parser(get(path, headers));
        String status=parser.getStringFromArray("status");
        String filename=parser.getStringFromArray("savedFileName");
        if (status.equals("COMPLETED"))
            flag= true;
        else if(status.equals("COMPLETED_WITH_ERRORS"))
            throw new RuntimeException(filename+": UPLOAD COMPLETED_WITH_ERRORS");
        else if(status.equals("IN_PROGRESS"))
            verifyUpload();
        return flag;
    }
}
