package TestDataGenerator;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ContactProfileTest {

    ContactProfile cp = new ContactProfile();
    @Test
    void getShuffledPatientProfilesTest() throws IOException, CsvException {
        DataWriter dataWriter = new DataWriter();
        List<String[]> profiles = new ArrayList<>();
        profiles.add(Constant.registrationHeader);
        profiles.add(new String[]{"a"});
        profiles.add(new String[]{"b"});

        String fileName = Constant.patientProfileFileName;
        dataWriter.writeDataIntoCSV(profiles, fileName);
        List<String[]> obj = cp.getShuffledPatientProfilesToObject();
        assertEquals(obj.size(), 2);
        assertNotNull(obj);
    }

    @Test
    void writeContactProfileInCSVnTest() throws IOException, ParseException, CsvException {
        PatientProfile pf = new PatientProfile();
        pf.writePatientProfileInCSV(1);
        cp.writeContactProfileInCSV(1);
        FileReader patientRegFile = new FileReader(Constant.contactProfileFileName);
        CSVReader csvReader = new CSVReaderBuilder(patientRegFile)
                .withSkipLines(1)
                .build();
        List<String[]> contactFileInfo = csvReader.readAll();
        assertEquals(1, contactFileInfo.size());

    }

    @Mock
    ContactProfile contactProfile = new ContactProfile();

    @Test
    void writeContactProfileInCSVInvocationTest() throws IOException, ParseException, CsvException {
        contactProfile.writeContactProfileInCSV(1);
        verify(contactProfile, Mockito.times(1)).writeContactProfileInCSV(1);
    }
}