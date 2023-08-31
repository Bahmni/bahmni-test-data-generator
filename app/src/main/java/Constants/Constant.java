package Constants;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;

public class Constant {
    public static String PATIENT_PROFILE_FILE_NAME = getProperties().get("PATIENT_PROFILE_FILE_NAME").toString();
    public static String ENCOUNTER_PROFILE_FILE_NAME = getProperties().get("ENCOUNTER_PROFILE_FILE_NAME").toString();
    public static String MEDICATION_ORDER__PROFILE_FILE_NAME = getProperties().get("MEDICATION_ORDER_PROFILE_FILE_NAME").toString();
    public static int REGISTRATION_INDEX_START_POINT = (Integer) getProperties().get("REGISTRATION_INDEX_START_POINT");
    public static String PATIENT_REGISTRATION_START_DAY = getProperties().get("PATIENT_REGISTRATION_START_DAY").toString();
    public static String REG_INITIAL = getProperties().get("REG_INITIAL").toString();
    public static String BASEURL = getProperties().get("BASEURL").toString();
    public static String USERNAME = getProperties().get("USERNAME").toString();
    public static String PASSWORD = getProperties().get("PASSWORD").toString();
    public static String LOCATION = getProperties().get("LOCATION").toString();
    public static String SNOWSTORM_URL = getProperties().get("SNOWSTORM_URL").toString();
    public static String CONCEPT_SOURCE_SNOMED_UUID = getProperties().get("CONCEPT_SOURCE_SNOMED_UUID").toString();
    public static String CONCEPT_MAPPING_SAME_AS_UUID = getProperties().get("CONCEPT_MAPPING_SAME_AS_UUID").toString();

    public static String CONCEPT_CLASS_DIAGNOSIS_UUID = getProperties().get("CONCEPT_CLASS_DIAGNOSIS_UUID").toString();
    public static String CONCEPT_DATATYPE_NA_UUID = getProperties().get("CONCEPT_DATATYPE_NA_UUID").toString();

    public static String[] registrationHeader = {"Registration Number", "Registration Date", "First Name", "Middle Name", "Last Name", "Gender",
            "Birth Date", "Address.Village", "Address.Gram Panchayat", "Address.House No., Street", "Address.State"};

    public static String[] contactHeader = {"Registration Number", "encounterType", "visitType", "Patient.Name", "Patient.AGE",
            "Patient.Gender", "Patient.Village", "Visit Start Date", "Visit End Date", "Repeat.1.EncounterDate",
            "Repeat.1.Obs.Chief Complaint Duration", "Repeat.1.Obs.Chief Complaint Notes",
            "Repeat.1.Obs.History Notes", "Repeat.1.Obs.Examination Notes", "Repeat.1.Obs.Smoking History",
            "Repeat.1.Diagnosis.1", "Repeat.1.Diagnosis.2", "Repeat.1.Obs.Consultation Note",
            "Repeat.1.Obs.Hospital Course", "Repeat.1.Obs.Operative Notes, Condition",
            "Repeat.1.Obs.Operative Notes, Procedure", "Repeat.1.Obs.Procedure Notes, Diagnosis"};

    public static String[] medicationOrderHeader = {"Registration Number", "encounterType", "visitType",
            "Visit Start Date", "Visit End Date", "Repeat.1.EncounterDate",
            "drug", "form", "dose", "doseUnits", "route", "frequency", "asNeeded", "quantity", "quantityUnits", "numberOfRefills"};

    public static Map<String, Object> getProperties() {
        Map<String, Object> data;
        String configFileName = "config.yaml";

        try {
            File file = new File(Objects.requireNonNull(Constant.class.getClassLoader().getResource(configFileName)).getFile());
            InputStream inputStream = new FileInputStream(file);
            Yaml yaml = new Yaml();
            data = yaml.load(inputStream);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    public static String getProperty(String str) {
        return getProperties().get(str).toString();
    }
}
