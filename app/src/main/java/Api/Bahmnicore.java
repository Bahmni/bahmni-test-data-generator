package Api;

import Config.LoggerConfig;
import Constants.Constant;
import Jsonparser.Parser;
import org.apache.http.HttpResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


public class Bahmnicore extends Request {

    Logger logger = LoggerConfig.LOGGER;
    static Map<String, String> headers = new HashMap<>();

    static {
        headers.put("Accept", "*/*");
        headers.put("Accept-Encoding", "gzip, deflate, br");
        headers.put("Accept-Language", "en-GB,en-US;q=0.9,en;q=0.8");
    }

    public void uploadPatients() {
        String path = "/openmrs/ws/rest/v1/bahmnicore/admin/upload/patient";
        post(path, headers, System.getProperty("user.dir") + "/" + Constant.PATIENT_PROFILE_FILE_NAME);
    }

    public void uploadEncounters() {
        String path = "/openmrs/ws/rest/v1/bahmnicore/admin/upload/encounter";
        post(path, headers, System.getProperty("user.dir") + "/" + Constant.ENCOUNTER_PROFILE_FILE_NAME);
    }

    public void verifyUpload(String name) throws InterruptedException {
        Thread.sleep(5000);
        String path = "/openmrs/ws/rest/v1/bahmnicore/admin/upload/status";
        HttpResponse response = get(path, headers);
        int responseCode = response.getStatusLine().getStatusCode();
        if (responseCode == 200) {
            Parser parser = new Parser(response);
            String status = parser.getStringFromArray("status");
            int count = parser.getIntFromArray("successfulRecords");
            String filename = parser.getStringFromArray("savedFileName");
            switch (status) {
                case "COMPLETED":
                    logger.info(filename + " : UPLOAD " + status);
                    break;
                case "COMPLETED_WITH_ERRORS":
                    if (name.equalsIgnoreCase("PATIENT")) {
                        if (count < 50)
                            throw new RuntimeException(filename + " : UPLOAD " + status);
                    }
                    logger.info(filename + " : UPLOAD " + status);
                    break;
                case "IN_PROGRESS":
                    verifyUpload(name);
                    break;
                default:
                    throw new IllegalStateException("Unexpected upload status : " + status);
            }
        }
        else
        {
            verifyUpload(name);
        }
    }
}
