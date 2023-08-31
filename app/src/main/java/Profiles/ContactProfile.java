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
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static java.lang.String.format;

public class ContactProfile {

    private final EncounterScenarioGenerator encounterScenarioGenerator;
    private final List<DrugOrderEncounter> drugOrderEncounterList;
    Map<String, Coding> newDiagnosisMap = new HashMap<>();
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final Logger logger = LoggerConfig.LOGGER;

    public ContactProfile(EncounterScenarioGenerator encounterScenarioGenerator, List<DrugOrderEncounter> drugOrderEncounterList) {
        this.encounterScenarioGenerator = encounterScenarioGenerator;
        this.drugOrderEncounterList = drugOrderEncounterList;
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
                encounterDate = updatedEncounterDateIfLessThanPatientDob(tempPatientInfo[6], encounterDate);
                //encounterDate = updatedEncounterDateIfFutureDate(encounterDate);

                if (encounter.getDiagnosis() != null) {
                    System.out.println("Code " + encounter.getDiagnosis().getCode() + " Label " + encounter.getDiagnosis().getLabel());
                    newDiagnosisMap.put(encounter.getDiagnosis().getCode(), encounter.getDiagnosis());
                }
                if (encounter.getCondition() != null) {
                    System.out.println("Code " + encounter.getCondition().getCode() + " Label " + encounter.getCondition().getLabel());
                    newDiagnosisMap.put(encounter.getCondition().getCode(), encounter.getCondition());
                }

                // TODO: Drug Requests
                List<DrugOrder> drugOrders = encounter.getDrugOrders();
                // Write out to another CSV
                if (!drugOrders.isEmpty()) {
                    drugOrderEncounterList.add(new DrugOrderEncounter(
                            tempPatientInfo[0],
                            encounterType,
                            visitType,
                            encounterDate,
                            encounterDate,
                            encounterDate,
                            drugOrders
                    ));
                }

                String diagnosis = encounter.getDiagnosis() == null ? "" : encounter.getDiagnosis().getLabel();
                String condition = encounter.getCondition() == null ? "" : encounter.getCondition().getLabel();

                if ("".equals(diagnosis) && "".equals(condition)) {
                    continue;
                }
                tempContactInfo = new String[]{
                        tempPatientInfo[0], encounterType, visitType, patientName, String.valueOf(patientAge),
                        patientGender, tempPatientInfo[9], encounterDate, encounterDate, encounterDate,
                        encounter.getChiefComplaintDuration(),
                        encounter.getChiefComplaintNotes(),
                        encounter.getHistoryNotes(),
                        encounter.getExaminationNotes(),
                        encounter.getSmokingHistory(),
                        diagnosis,// TODO: Not sure how to do this.. should this work just using labels?
                        condition,
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

        String medicationOrderFileName = Constant.MEDICATION_ORDER__PROFILE_FILE_NAME;
        List<String[]> medicationOrderProfiles = getMedicationOrderProfiles();

        dataWriter.writeDataIntoCSV(medicationOrderProfiles, medicationOrderFileName);
    }

    public int getPatientAge(String dob) {
        LocalDate birthday = LocalDate.parse(dob);
        LocalDate today = LocalDate.now();
        Period period = Period.between(birthday, today);
        return period.getYears();
    }

    public String updatedEncounterDateIfLessThanPatientDob(String dob, String encounterDateStr) {
        LocalDate birthday = LocalDate.parse(dob);
        LocalDate encounterDate = LocalDate.parse(encounterDateStr);
        if (birthday.isAfter(encounterDate)) {
            return simpleDateFormat.format(Date.from(birthday.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }
        return encounterDateStr;
    }

    public String updatedEncounterDateIfFutureDate(String encounterDateStr) {
        LocalDate encounterDate = LocalDate.parse(encounterDateStr);
        if (LocalDate.now().isBefore(encounterDate)) {
            return simpleDateFormat.format(Date.from(LocalDate.now().minusDays(10).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }
        return encounterDateStr;
    }

    private List<String[]> getMedicationOrderProfiles() {
        List<String[]> entries = new ArrayList<>();
        entries.add(Constant.medicationOrderHeader);

        // iterate over drugOrderEncounterList and create entries and add to entries list
        drugOrderEncounterList.forEach(drugOrderEncounter -> {
            List<DrugOrder> drugOrders = drugOrderEncounter.getDrugOrders();
            // "drug", "form", "dose", "doseUnits", "route", "frequency", "asNeeded", "quantity", "quantityUnits", "numberOfRefills"
            drugOrders.forEach(drugOrder -> {
                String[] entry = new String[16];
                entry[0] = drugOrderEncounter.getRegistrationNumber();
                entry[1] = drugOrderEncounter.getEncounterType();
                entry[2] = drugOrderEncounter.getVisitType();
                entry[3] = drugOrderEncounter.getVisitStartDate();
                entry[4] = drugOrderEncounter.getVisitEndDate();
                entry[5] = drugOrderEncounter.getEncounterDate();
                entry[6] = drugOrder.getDrug().getLabel();
                entry[7] = drugOrder.getForm();
                entry[8] = String.valueOf(drugOrder.getDose());
                entry[9] = drugOrder.getDoseUnits();
                entry[10] = drugOrder.getRoute();
                entry[11] = drugOrder.getFrequency();
                entry[12] = String.valueOf(drugOrder.isAsNeeded());
                entry[13] = String.valueOf(drugOrder.getQuantity());
                entry[14] = drugOrder.getQuantityUnits();
                entry[15] = String.valueOf(drugOrder.getNumberOfRefills());
                entries.add(entry);
            });
        });

        return  entries;
    }

    public Map<String, Coding> getNewDiagnosisMap() {
        return newDiagnosisMap;
    }
}
