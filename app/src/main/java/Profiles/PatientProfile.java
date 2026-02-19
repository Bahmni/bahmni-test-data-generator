package Profiles;

import CSVwriter.DataWriter;
import Config.LoggerConfig;
import Constants.Constant;
import com.github.javafaker.Faker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

public class PatientProfile {
    Logger logger = LoggerConfig.LOGGER;
    Faker faker = new Faker(new Locale("en-IND"));

    public String[] createPatientProfile() {
        String addressLine1 = Constant.ADDRESS_LINE_1;
        String addressLine2 = Constant.ADDRESS_LINE_2;
        String locality = Constant.ADDRESS_LOCALITY;
        String cityVillage = Constant.ADDRESS_CITY_VILLAGE;
        String postalCode = Constant.ADDRESS_POSTAL_CODE;
        String country = Constant.ADDRESS_COUNTRY;

        return new String[]{"x", getRegistrationDate(), getFirstName(), getMiddleName(),
                getLastName(), getGender(), getBirthDate(), addressLine1, addressLine2,
                locality, cityVillage, postalCode, country};
    }

    public List<String[]> getPatientProfileList(int count) {
        List<String[]> entries = new ArrayList<>();
        int startPoint = Constant.REGISTRATION_INDEX_START_POINT;

        entries.add(Constant.registrationHeader);

        for (int i = 1; i <= count; i++) {
            String[] pProfile = createPatientProfile();
            pProfile[0] = Constant.REG_INITIAL + faker.random().nextInt(1, 10000) + (i + startPoint);
            entries.add(pProfile);
        }

        return entries;
    }

    public void writePatientProfileInCSV(int count) {
        DataWriter dataWriter = new DataWriter();
        String fileName = Constant.PATIENT_PROFILE_FILE_NAME;
        List<String[]> profiles = getPatientProfileList(count);
        dataWriter.writeDataIntoCSV(profiles, fileName);
    }

    private String getRegistrationDate() {
        Date now = new Date();

        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = Constant.PATIENT_REGISTRATION_START_DAY;
        Date then = null;
        try {
            then = sdf.parse(dateString);
        } catch (ParseException e) {
            logger.severe("Could'nt parse registration date " + e.getLocalizedMessage());
        }
        Date randomDate = faker.date().between(then, now);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(randomDate);
    }

    private String getBirthDate() {
        Date now = new Date();

        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = "1930-01-01";
        Date then = null;
        try {
            then = sdf.parse(dateString);
        } catch (ParseException e) {
            logger.severe("Could'nt parse birth date " + e.getLocalizedMessage());

        }
        Date randomDate = faker.date().between(then, now);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(randomDate);
    }

    private String getFirstName() {
        return faker.name().firstName();
    }

    private String getLastName() {
        return faker.name().lastName();
    }

    private String getMiddleName() {
        return faker.name().nameWithMiddle().split(" ")[1];
    }

    private String getGender() {
        int i = faker.random().nextInt(1, 2);
        return i == 1 ? "M" : "F";
    }

    private String getTownCityName() {
        return faker.address().cityName();
    }

    private String getPinCode() {
        return faker.address().zipCode();
    }

    private String getStateName() {
        return faker.address().state();
    }

    private String getDistrict() {
        return faker.address().cityName();
    }
    
    protected String formatTimestampToDate(long timestampMillis) {
        Date date = new Date(timestampMillis);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }
}
