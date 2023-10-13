package TestDataGenerator;

import Api.Bahmnicore;
import Api.Openmrs;
import Config.LoggerConfig;
import Constants.Constant;
import Profiles.ContactProfile;
import Profiles.PatientProfile;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class TestDataGenApp {

    Logger logger;

    public TestDataGenApp() {
        logger = LoggerConfig.LOGGER;
        LoggerConfig.init();
    }

    public static void main(String[] args) {
        TestDataGenApp app = new TestDataGenApp();
        Bahmnicore bah = new Bahmnicore();
        Openmrs omrs = new Openmrs();
        Map<String, Integer> inputArgs = app.getInputArgs();
        app.createCSVs(inputArgs.get("PATIENT_COUNT"), inputArgs.get("ENCOUNTER_COUNT"));
        if (inputArgs.get("S_UPLOAD_CSV") == 1) {
            omrs.setUserLocation(getArg("LOCATION"));
            omrs.login(getArg("UNAME"), getArg("PASSWORD"));
            omrs.getSessionId();
            bah.uploadPatients();
            try {
                bah.verifyUpload("PATIENT");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

    }

    protected Map<String, Integer> getInputArgs() {
        Map<String, Integer> args = new HashMap<>();
        int PATIENT_COUNT = Integer.parseInt(getArg("PATIENT_COUNT"));
        logger.info("Patient Count : " + PATIENT_COUNT);
        args.put("PATIENT_COUNT", PATIENT_COUNT);


        String S_CREATE_ENCOUNTER = getArg("S_CREATE_ENCOUNTER");
        logger.info("Create Encounters : " + S_CREATE_ENCOUNTER);

        if (S_CREATE_ENCOUNTER.equalsIgnoreCase("y")) {

            int ENCOUNTER_COUNT = Integer.parseInt(getArg("ENCOUNTER_COUNT"));
            logger.info("No of Patients with encounter : " + ENCOUNTER_COUNT);

            if (ENCOUNTER_COUNT > PATIENT_COUNT) {
                throw new RuntimeException("encounter count cannot be greater than patient count");
            }

            args.put("ENCOUNTER_COUNT", ENCOUNTER_COUNT);
        } else if (S_CREATE_ENCOUNTER.equalsIgnoreCase("n")) {
            args.put("ENCOUNTER_COUNT", 0);
        }
        String sUploadCsv = getArg("S_UPLOAD_CSV");
        logger.info("Upload CSV to Bahmni :" + sUploadCsv);
        if (sUploadCsv.equalsIgnoreCase("y")) {
            args.put("S_UPLOAD_CSV", 1);
        } else if (sUploadCsv.equalsIgnoreCase("n")) {
            args.put("S_UPLOAD_CSV", 0);
        }

        return args;
    }

    protected void createCSVs(int patientProfileCount, int contactProfileCount) {
        PatientProfile patientProfile = new PatientProfile();
        ContactProfile contactProfile = new ContactProfile();

        if (patientProfileCount != 0) {
            patientProfile.writePatientProfileInCSV(patientProfileCount);
        }

        if (contactProfileCount != 0) {
            contactProfile.writeContactProfileInCSV(contactProfileCount);
        }

    }

    protected static String getArg(String str) {
        String prop = System.getenv(str);
        return (prop == null) ? Constant.getProperty(str) : prop;
    }
}

