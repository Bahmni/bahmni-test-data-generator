package TestDataGenerator;

import CSVwriter.DataWriter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DataWriterTest {

    @Mock
    DataWriter dw = new DataWriter();

    @Test
    void writeDataIntoCSVTest() throws IOException {
        List<String[]> profiles = new ArrayList<>();
        String fileName = "output/mock.csv";
        dw.writeDataIntoCSV(profiles, fileName);
        verify(dw, Mockito.times(1)).writeDataIntoCSV(profiles, fileName);
        }
}
