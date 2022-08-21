package Profiles;

import Constants.Constant;
import CSVwriter.DataWriter;
import com.github.javafaker.Faker;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PatientProfile {

    Faker faker = new Faker(new Locale("en-IND"));;
    public String[] createPatientProfile()
    {
        String villageDistrict = getTownCityName();

        String[] item = {"x", getRegistrationDate(), getFirstName(), getMiddleName(),
                getLastName(), getGender(), getBirthDate(), villageDistrict, getTehsilName(), villageDistrict,
                getStateName()};
        return item;
    }

    public List<String[]> getPatientProfileList(int count)
    {
        List<String[]> entries = new ArrayList<>();
        int startPoint = Constant.REGISTRATION_INDEX_START_POINT;

        entries.add(Constant.registrationHeader);

        for(int i=1;i<=count;i++)
        {
            String[] pProfile = createPatientProfile();
            pProfile[0] = Constant.REG_INITIAL+faker.random().nextInt(1,10000) +String.valueOf(i+startPoint);
            entries.add(pProfile);
        }

        return entries;
    }

    public void writePatientProfileInCSV(int count) throws IOException
    {
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
            e.printStackTrace();
        }
        Date randomDate = faker.date().between(then, now);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(randomDate);
        return strDate;
    }

    private String getBirthDate() {
        Date now = new Date();

        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = "1930-01-01";
        Date then = null;
        try {
            then = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date randomDate = faker.date().between(then, now);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(randomDate);
        return strDate;
    }

    private String getFirstName()
    {
        return faker.name().firstName();
    }

    private String getLastName()
    {
        return faker.name().lastName();
    }

    private String getMiddleName()
    {
        return faker.name().nameWithMiddle().split(" ")[1];
    }

    private String getGender()
    {
        int i = faker.random().nextInt(1,2);
        return i==1? "M":"F";
    }

    private String getTownCityName()
    {
        return faker.address().cityName();
    }

    private String getTehsilName()
    {
        return faker.address().secondaryAddress();
    }

    private String getStateName()
    {
        return faker.address().state();
    }
}
