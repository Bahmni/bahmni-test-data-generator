package CSVwriter;

import com.opencsv.CSVWriter;


import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;
import Config.LoggerConfig;

public class DataWriter {
    Logger logger= LoggerConfig.LOGGER;
    public void writeDataIntoCSV(List<String[]> profiles, String fileName)  {

        try{
            Files.createDirectories(Paths.get("output"));
            FileWriter outputFile = new FileWriter(System.getProperty("user.dir")+"/"+fileName);
            CSVWriter writer = new CSVWriter(outputFile, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.DEFAULT_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
            writer.writeAll(profiles);
            writer.close();
        } catch (IOException e) {
            logger.severe("Datawriter.writeDataIntoCSV"+e.getLocalizedMessage());
        }
    }
}
