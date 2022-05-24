package TestDataGenerator;

import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TestDataGenApp {

    public static void main(String args[])
    {
        Map<String, Integer> userInput = validateInput();
        try {
            createCSVs(userInput.get("profileCount"), userInput.get("contactCount"));
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        } catch (ParseException e) {
            System.out.println(e.getLocalizedMessage());
        } catch (CsvException e) {
            System.out.println(e.getLocalizedMessage());
        }

    }

    protected static Map<String, Integer> validateInput()
    {
        int patientProfileCount = 0;
//        int contactCount = 0;
        Map<String, Integer> totalCount = new HashMap<>();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the number of patient profiles needed: ");
        patientProfileCount = scanner.nextInt();

        System.out.println("Do you need contact csv to be created? (y/n):");
        String sCreateContact = scanner.next();

        if(sCreateContact.equalsIgnoreCase("y")) {
            System.out.println("Enter the number of profiles need to have contacts from above. " +
                    "Enter a number between 0 and "+patientProfileCount);
            int contactCount = scanner.nextInt();

            while(contactCount > patientProfileCount)
                {
                    System.out.println("Enter the number of lesser than of patient profile number entered above.");
                    contactCount = scanner.nextInt();
                }

            totalCount.put("contactCount", contactCount);
        }

        totalCount.put("profileCount", patientProfileCount);


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
}
