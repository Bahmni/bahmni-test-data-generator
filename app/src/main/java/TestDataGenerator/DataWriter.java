package TestDataGenerator;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class DataWriter {

    protected void writeDataIntoCSV(List<String[]> profiles, String fileName) throws IOException {
        Files.createDirectories(Paths.get("output"));
        try{
            FileWriter outputFile = new FileWriter(fileName);
            CSVWriter writer = new CSVWriter(outputFile, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.DEFAULT_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
            writer.writeAll(profiles);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
