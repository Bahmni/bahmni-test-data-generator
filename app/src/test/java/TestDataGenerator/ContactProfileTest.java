package TestDataGenerator;

import com.opencsv.exceptions.CsvException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ContactProfileTest {

    List<String[]> data = new ArrayList<>();

    @Before
    public void init() {
        String[] d1 = {"a"};
        String[] d2 = {"b"};
        data.add(d1);
        data.add(d2);
    }


    @Test
    public void getShuffledPatientProfilesToObject() throws IOException, CsvException {

        ContactProfile cp = new ContactProfile();
        cp.getShuffledPatientProfilesToObject();

    }

    @Test
    public void writeContactProfileInCSV() {
    }
}