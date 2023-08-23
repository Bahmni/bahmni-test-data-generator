package Profiles;

import CSVwriter.DataWriter;
import Config.LoggerConfig;
import Constants.Constant;
import Profiles.scenarios.EncounterScenarioGenerator;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Logger;

import static java.lang.String.format;

public class ContactProfile {

    private final EncounterScenarioGenerator encounterScenarioGenerator;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final Logger logger = LoggerConfig.LOGGER;

    public ContactProfile(EncounterScenarioGenerator encounterScenarioGenerator) {
        this.encounterScenarioGenerator = encounterScenarioGenerator;
    }

    public List<String[]> getShuffledPatientProfilesToObject() {
        List<String[]> allPatientProfileData = null;
        try {
            FileReader patientRegFile = new FileReader(System.getProperty("user.dir") + "/" + Constant.PATIENT_PROFILE_FILE_NAME);
            CSVReader csvReader = new CSVReaderBuilder(patientRegFile)
                    .withSkipLines(1)
                    .build();
            allPatientProfileData = csvReader.readAll();

            Collections.shuffle(allPatientProfileData);
        } catch (IOException | CsvException e) {
            logger.severe("Patient Registration file not found" + e.getLocalizedMessage());
        }
        return allPatientProfileData;
    }

    private List<String[]> setContactProfiles(int count) {
        List<String[]> entries = new ArrayList<>();
        String[] tempPatientInfo;
        String[] tempContactInfo;
        String encounterType = "Consultation";
        String visitType = "OPD";
        String patientName;

        entries.add(Constant.contactHeader);
        List<String[]> patientList = getShuffledPatientProfilesToObject();
        for (int i = 0; i < count; i++) {
            tempPatientInfo = patientList.get(i);
            patientName = tempPatientInfo[2] + " " + tempPatientInfo[3] + " " + tempPatientInfo[4];

            int patientAge = getPatientAge(tempPatientInfo[6]);
            String patientGender = tempPatientInfo[5];
            GregorianCalendar registrationDate = toCalendar(tempPatientInfo[1]);
            List<Encounter> encounters = encounterScenarioGenerator.create(patientAge, patientGender, registrationDate);

            for (Encounter encounter : encounters) {
                String encounterDate = simpleDateFormat.format(encounter.getDate());
                tempContactInfo = new String[]{
                        tempPatientInfo[0], encounterType, visitType, patientName, String.valueOf(patientAge),
                        patientGender, tempPatientInfo[9], encounterDate, encounterDate, encounterDate,
                        encounter.getChiefComplaintDuration(),
                        encounter.getChiefComplaintNotes(),
                        encounter.getHistoryNotes(),
                        encounter.getExaminationNotes(),
                        encounter.getSmokingHistory(),
                        encounter.getDiagnosis().getCode(),// TODO: Not sure how to do this.. should this work just using labels?
                        encounter.getCondition().getCode(),
                        encounter.getConsultationNote(),
                        encounter.getHospitalCourse(),
                        encounter.getOperativeNotesCondition(),
                        encounter.getOperativeNotesProcedure(),
                        encounter.getProcedureNotesDiagnosis()
                };
                entries.add(tempContactInfo);
            }
        }
        return entries;
    }

    private GregorianCalendar toCalendar(String source) {
        GregorianCalendar calendar = new GregorianCalendar();
        try {
            calendar.setTime(simpleDateFormat.parse(source));
        } catch (ParseException e) {
            throw new DataGenerationRuntimeException(format("Failed to parse date '%s'", source), e);
        }
        return calendar;
    }

    public void writeContactProfileInCSV(int count) {
        DataWriter dataWriter = new DataWriter();
        String fileName = Constant.ENCOUNTER_PROFILE_FILE_NAME;
        List<String[]> profiles = setContactProfiles(count);
        dataWriter.writeDataIntoCSV(profiles, fileName);
    }

    public int getPatientAge(String dob) {
        LocalDate birthday = LocalDate.parse(dob);
        LocalDate today = LocalDate.now();
        Period period = Period.between(birthday, today);
        return period.getYears();
    }
}
