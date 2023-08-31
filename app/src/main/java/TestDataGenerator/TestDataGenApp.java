package TestDataGenerator;

import Api.Bahmnicore;
import Api.Openmrs;
import Config.LoggerConfig;
import Constants.Constant;
import Profiles.Coding;
import Profiles.ContactProfile;
import Profiles.DrugOrderEncounter;
import Profiles.DrugOrderProfile;
import Profiles.PatientProfile;
import Profiles.scenarios.DefaultEncounterScenarioGenerator;
import Profiles.scenarios.EncounterScenarioGenerator;
import Profiles.scenarios.PrenatalEncounterScenarioGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static java.lang.String.format;

public class TestDataGenApp {

    Logger logger;

    List<DrugOrderEncounter> drugOrderEncounterList = new ArrayList<>();

    Map<String, Coding> newDiagnosisMap = new HashMap<>();


    Map<String, EncounterScenarioGenerator> scenarioGeneratorMap = Map.of(
            "DEFAULT", new DefaultEncounterScenarioGenerator(),
            "PRENATAL_SUPPLEMENTS", new PrenatalEncounterScenarioGenerator()
    );

    public TestDataGenApp() {
        logger = LoggerConfig.LOGGER;
        LoggerConfig.init();
    }

    public static void main(String[] args) {
        new TestDataGenApp().run();
    }

    private void run() {
        Bahmnicore bah = new Bahmnicore();
        Openmrs omrs = new Openmrs();
        Map<String, Integer> inputArgs = getInputArgs();
        String scenarioName = getArg("SCENARIO");
        EncounterScenarioGenerator encounterScenarioGenerator = scenarioGeneratorMap.get(scenarioName);
        if (encounterScenarioGenerator == null) {
            throw new IllegalArgumentException(format("No encounter scenario generator found for SCENARIO '%s'", scenarioName));
        }

        createCSVs(inputArgs.get("PATIENT_COUNT"), inputArgs.get("ENCOUNTER_COUNT"), encounterScenarioGenerator);

        if (inputArgs.get("S_CREATE_DIAGNOSIS_CONCEPTS") == 1) {
            omrs.setUserLocation(getArg("LOCATION"));
            omrs.login(getArg("USERNAME"), getArg("PASSWORD"));
            omrs.getSessionId();
            bah.createDiagnosisConcepts(newDiagnosisMap);
        }

        if (inputArgs.get("S_UPLOAD_CSV") == 1) {
            omrs.setUserLocation(getArg("LOCATION"));
            omrs.login(getArg("USERNAME"), getArg("PASSWORD"));
            omrs.getSessionId();
            bah.uploadPatients();
            try {
                Thread.sleep(1000);
                bah.verifyUpload("PATIENT");
                bah.uploadEncounters();
                Thread.sleep(1000);
                bah.verifyUpload("ENCOUNTER");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

        if (inputArgs.get("S_UPLOAD_MEDICATIONS") == 1) {
            bah.updateSearchIndex();
            DrugOrderProfile drugOrderProfile = new DrugOrderProfile(drugOrderEncounterList);
            drugOrderProfile.uploadDrugOrders();
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

        String sUploadMedication = getArg("S_UPLOAD_MEDICATIONS");

        logger.info("Upload Medications to Bahmni :" + sUploadMedication);
        if (sUploadMedication.equalsIgnoreCase("y")) {
            args.put("S_UPLOAD_MEDICATIONS", 1);
        } else if (sUploadMedication.equalsIgnoreCase("n")) {
            args.put("S_UPLOAD_MEDICATIONS", 0);
        }

        String createNewDiagnosisConcepts = getArg("S_CREATE_DIAGNOSIS_CONCEPTS");

        logger.info("Create New Diagnosis concepts :" + createNewDiagnosisConcepts);
        if (sUploadMedication.equalsIgnoreCase("y")) {
            args.put("S_CREATE_DIAGNOSIS_CONCEPTS", 1);
        } else if (sUploadMedication.equalsIgnoreCase("n")) {
            args.put("S_CREATE_DIAGNOSIS_CONCEPTS", 0);
        }

        return args;
    }

    protected void createCSVs(int patientProfileCount, int contactProfileCount, EncounterScenarioGenerator encounterScenarioGenerator) {
        PatientProfile patientProfile = new PatientProfile();
        ContactProfile contactProfile = new ContactProfile(encounterScenarioGenerator, drugOrderEncounterList);

        if (patientProfileCount != 0) {
            patientProfile.writePatientProfileInCSV(patientProfileCount);
        }

        if (contactProfileCount != 0) {
            contactProfile.writeContactProfileInCSV(contactProfileCount);
        }

         newDiagnosisMap = contactProfile.getNewDiagnosisMap();
    }

    protected static String getArg(String str) {
        String prop = System.getenv(str);
        return (prop == null) ? Constant.getProperty(str) : prop;
    }
}

