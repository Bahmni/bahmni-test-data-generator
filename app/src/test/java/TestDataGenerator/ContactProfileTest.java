package TestDataGenerator;

import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ContactProfileTest {

    ContactProfile cp = new ContactProfile();
    @Test
    void getShuffledPatientProfilesTest() throws IOException, CsvException {
        List<String[]> obj = cp.getShuffledPatientProfilesToObject();
        assertNotNull(obj);
    }

    @Mock
    ContactProfile contactProfile = new ContactProfile();

    @Test
    void writeContactProfileInCSVTest() throws IOException, ParseException, CsvException {
        contactProfile.writeContactProfileInCSV(1);
        verify(contactProfile, Mockito.times(1)).writeContactProfileInCSV(1);
    }
}