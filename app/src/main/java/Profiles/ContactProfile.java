package Profiles;

import Config.LoggerConfig;
import Constants.Constant;
import CSVwriter.DataWriter;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class ContactProfile {

     Logger logger= LoggerConfig.LOGGER;
     public List<String[]> getShuffledPatientProfilesToObject()  {
          List<String[]> allPatientProfileData=null;
          try {
               FileReader patientRegFile = new FileReader(System.getProperty("user.dir") + "/" + Constant.PATIENT_PROFILE_FILE_NAME);
               CSVReader csvReader = new CSVReaderBuilder(patientRegFile)
                       .withSkipLines(1)
                       .build();
               allPatientProfileData = csvReader.readAll();

               Collections.shuffle(allPatientProfileData);
          }
          catch (IOException | CsvException e)
          {
               logger.severe("Patient Registration file not found"+e.getLocalizedMessage());
          }
          return allPatientProfileData;
     }

     private List<String[]> setContactProfiles(int count) {
          List<String[]> entries = new ArrayList<>();
          String[] tempPatientInfo;
          String[] tempContactInfo;
          String encounterType="Consultation";
          String visitType="OPD";
          String patientName;

          entries.add(Constant.contactHeader);
          List<String[]> patientList = getShuffledPatientProfilesToObject();
          for(int i=0;i<count;i++)
          {
               tempPatientInfo = patientList.get(i);
               patientName = tempPatientInfo[2]+ " "+tempPatientInfo[3]+" "+tempPatientInfo[4];
               tempContactInfo = new String[]{tempPatientInfo[0],encounterType,visitType,patientName,
                       String.valueOf(getPatientAge(tempPatientInfo[6])),tempPatientInfo[5],tempPatientInfo[9],
                       tempPatientInfo[1],tempPatientInfo[1],
                       tempPatientInfo[1],"12","Test notes","history notes","exam notes","yes","Fever",
                       "Infectious diarrhea", "Testing Consultation Note","Testing hospital course comment","Stable",
                       "Wound Care", "Need dressing"};
               entries.add(tempContactInfo);
          }
          return entries;
     }

     public void writeContactProfileInCSV(int count)  {
          DataWriter dataWriter = new DataWriter();
          String fileName = Constant.ENCOUNTER_PROFILE_FILE_NAME;
          List<String[]> profiles = setContactProfiles(count);
          dataWriter.writeDataIntoCSV(profiles, fileName);
     }
     public int getPatientAge(String dob)  {
          LocalDate birthday = LocalDate.parse(dob);
          LocalDate today = LocalDate.now();
          Period period = Period.between(birthday, today);
          return period.getYears();
     }
}
