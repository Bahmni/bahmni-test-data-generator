package TestDataGenerator;

import Bahmnicore.Bahmnicore;
import Constants.Constant;
import Openmrs.Openmrs;
import Profiles.ContactProfile;
import Profiles.PatientProfile;
import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TestDataGenApp {


    public static void main(String args[]) throws InterruptedException {
        String isGradleRun=System.getProperty("isGradle");
        Bahmnicore bah=new Bahmnicore();
        Openmrs mrs=new Openmrs();
        Map<String, Integer> userInput = (isGradleRun==null) ? validateInput():validateArgsInput();
        try {
            createCSVs(userInput.get("PATIENT_COUNT"), userInput.get("ENCOUNTER_COUNT"));
            if(userInput.get("S_UPLOAD_CSV")==1)
            {
                mrs.setUserLocation(validProperty("LOCATION"));
                mrs.login(validProperty("USERNAME"), validProperty("PASSWORD"));
                mrs.getSessionId();
                bah.uploadPatients();
                bah.verifyUpload();
                bah.uploadEncounters();
                bah.verifyUpload();

            }
        }

        catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        } catch (ParseException e) {
            System.out.println(e.getLocalizedMessage());
        } catch (CsvException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    protected static Map<String, Integer> validateInput()
    {
        int PATIENT_COUNT = 0;
//        int contactCount = 0;
        Map<String, Integer> totalCount = new HashMap<>();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the number of patient profiles needed: ");
        PATIENT_COUNT = scanner.nextInt();

        System.out.println("Do you need contact csv to be created? (y/n):");
        String S_CREATE_ENCOUNTER = scanner.next();

        if(S_CREATE_ENCOUNTER.equalsIgnoreCase("y")) {
            System.out.println("Enter the number of profiles need to have contacts from above. " +
                    "Enter a number between 0 and "+PATIENT_COUNT);
            int ENCOUNTER_COUNT = scanner.nextInt();

            while(ENCOUNTER_COUNT > PATIENT_COUNT)
                {
                    System.out.println("Enter the number of lesser than of patient profile number entered above.");
                    ENCOUNTER_COUNT = scanner.nextInt();
                }

            totalCount.put("ENCOUNTER_COUNT", ENCOUNTER_COUNT);
        }

        else if(S_CREATE_ENCOUNTER.equalsIgnoreCase("n"))
        {
            totalCount.put("ENCOUNTER_COUNT", 0);
        }
        System.out.println("Do you need to upload csv? (y/n):");
        String S_UPLOAD_CSV = scanner.next();
        if(S_UPLOAD_CSV.equalsIgnoreCase("y")) {
            totalCount.put("S_UPLOAD_CSV", 1);
        }
        else if(S_UPLOAD_CSV.equalsIgnoreCase("n")) {
            totalCount.put("S_UPLOAD_CSV", 0);
        }
        totalCount.put("PATIENT_COUNT", PATIENT_COUNT);


        return totalCount;
    }

    protected static Map<String, Integer> validateArgsInput()
    {
       // int patientProfileCount = 0;
//        int contactCount = 0;
        Map<String, Integer> totalCount = new HashMap<>();

       int PATIENT_COUNT = Integer.parseInt(validProperty("PATIENT_COUNT"));
        System.out.println("Entered number of patient profiles : "+PATIENT_COUNT);


        String S_CREATE_ENCOUNTER = validProperty("S_CREATE_ENCOUNTER");
        System.out.println("Do you need contact csv to be created? (y/n) : "+S_CREATE_ENCOUNTER);

        if(S_CREATE_ENCOUNTER.equalsIgnoreCase("y")) {

            int ENCOUNTER_COUNT = Integer.parseInt(validProperty("ENCOUNTER_COUNT"));
            System.out.println("Entered number of profiles need to have contacts from above : "+ENCOUNTER_COUNT);

            if(ENCOUNTER_COUNT > PATIENT_COUNT)
            {
                throw new RuntimeException("contact count cannot be greater than patient count");
            }

            totalCount.put("ENCOUNTER_COUNT", ENCOUNTER_COUNT);
        }
        else if(S_CREATE_ENCOUNTER.equalsIgnoreCase("n"))
        {
            totalCount.put("ENCOUNTER_COUNT", 0);
        }
        String sUploadCsv=validProperty("S_UPLOAD_CSV");
        System.out.println("Do you need to upload csv? (y/n):"+sUploadCsv);
        if(sUploadCsv.equalsIgnoreCase("y")) {
            totalCount.put("S_UPLOAD_CSV", 1);
        }
        else if(sUploadCsv.equalsIgnoreCase("n")) {
            totalCount.put("S_UPLOAD_CSV", 0);
        }
        totalCount.put("PATIENT_COUNT", PATIENT_COUNT);


        return totalCount;
    }

    protected static void createCSVs(int patientProfileCount, int contactProfileCount) throws IOException, ParseException,
            CsvException {
        PatientProfile patientProfile = new PatientProfile();
        ContactProfile contactProfile = new ContactProfile();

        if(patientProfileCount!=0) {
            patientProfile.writePatientProfileInCSV(patientProfileCount);
        }

        if(contactProfileCount!=0)
        {
            contactProfile.writeContactProfileInCSV(contactProfileCount);
        }

    }
    protected static String validProperty(String str)
    {
        String prop=System.getenv(str);
        String result = (prop == null) ? Constant.getProperty(str) : prop;
        return result;
    }
}

