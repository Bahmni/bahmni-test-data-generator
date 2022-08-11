package TestDataGenerator;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

public class Constant {
    static String patientProfileFileName = getProperties().get("patientProfileFileName").toString();
    static String contactProfileFileName = getProperties().get("contactProfileFileName").toString();
    static int registrationIndexStartPoint = (Integer) getProperties().get("registrationIndexStartPoint");
    static String patientRegistrationStartDay = getProperties().get("patientRegistrationStartDay").toString();
    static String regInitial = getProperties().get("regInitial").toString();
    static String patientProfileCount =  getProperties().get("patientProfileCount").toString();
    static String sCreateContact =  getProperties().get("sCreateContact").toString();
    static String contactCount =  getProperties().get("contactCount").toString();
    static String baseUrl=getProperties().get("baseUrl").toString();
    static String user=getProperties().get("user").toString();
    static String password=getProperties().get("password").toString();
    static String location=getProperties().get("location").toString();



    static String[] registrationHeader = {"Registration Number","Registration Date","First Name","Middle Name","Last Name","Gender",
            "Birth Date", "Address.Village", "Address.Tehsil", "Address.District", "Address.State"};

    static String[] contactHeader = {"Registration Number","encounterType","visitType","Patient.Name","Patient.AGE",
            "Patient.Gender","Patient.Village","Visit Start Date","Visit End Date","Repeat.1.EncounterDate",
            "Repeat.1.Obs.Chief Complaint Duration","Repeat.1.Obs.Chief Complaint Notes",
            "Repeat.1.Obs.History Notes","Repeat.1.Obs.Examination Notes","Repeat.1.Obs.Smoking History",
            "Repeat.1.Diagnosis.1","Repeat.1.Condition.1","Repeat.1.Obs.Consultation Note",
            "Repeat.1.Obs.Hospital Course", "Repeat.1.Obs.Operative Notes, Condition",
            "Repeat.1.Obs.Operative Notes, Procedure","Repeat.1.Obs.Procedure Notes, Diagnosis"};


    public static Map<String, Object> getProperties() {
        Map<String, Object> data;
        String configFileName = "config.yaml";

        try {
            File file = new File(Constant.class.getClassLoader().getResource(configFileName).getFile());
            InputStream inputStream = new FileInputStream(file);
            Yaml yaml = new Yaml();
            data = yaml.load(inputStream);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return data;
    }
    public static String getProperty(String str)
    {
        return getProperties().get(str).toString();
    }
}
