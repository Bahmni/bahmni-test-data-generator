package Bahmnicore;

import Api.Request;
import Config.LoggerConfig;
import Constants.Constant;
import Jsonparser.Parser;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


public class Bahmnicore extends Request{

    Logger logger= LoggerConfig.LOGGER;
    static Map<String,String> headers=new HashMap<>();
            static {
                headers.put( "Accept","*/*");
                headers.put("Accept-Encoding","gzip, deflate, br");
                headers.put("Accept-Language","en-GB,en-US;q=0.9,en;q=0.8");
            }

    public void uploadPatients()
    {

        String path="/openmrs/ws/rest/v1/bahmnicore/admin/upload/patient";
        post(path, headers, System.getProperty("user.dir") + "/" + Constant.PATIENT_PROFILE_FILE_NAME);
    }
    public   void uploadEncounters()
    {

        String path="/openmrs/ws/rest/v1/bahmnicore/admin/upload/encounter";
        post(path,headers,System.getProperty("user.dir")+"/"+Constant.ENCOUNTER_PROFILE_FILE_NAME);
    }

    public void verifyUpload() {
        String path = "/openmrs/ws/rest/v1/bahmnicore/admin/upload/status";
        Parser parser = new Parser(get(path, headers));
        String status=parser.getStringFromArray("status");
        String filename=parser.getStringFromArray("savedFileName");
        switch (status) {
            case "COMPLETED":
                logger.info(filename+" : UPLOAD "+status);
                break;
            case "COMPLETED_WITH_ERRORS":
                throw new RuntimeException(filename + " : UPLOAD "+status);
            case "IN_PROGRESS":
                verifyUpload();
                break;
            default:
                throw new IllegalStateException("Unexpected upload status : " + status);
        }
    }
}
