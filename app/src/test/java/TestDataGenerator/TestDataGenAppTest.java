package TestDataGenerator;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class TestDataGenAppTest {


    @Test
    void createCSVsTest() throws IOException{
        Files.deleteIfExists(Paths.get(System.getProperty("user.dir") + "/output/registrations.csv"));
        Files.deleteIfExists(Paths.get(System.getProperty("user.dir") + "/output/encounters.csv"));

        TestDataGenApp testDataGenApp = new TestDataGenApp();
        testDataGenApp.createCSVs(1,0, encounterScenarioGenerator);

        boolean bRegCSVPresent = Files.exists(Paths.get(System.getProperty("user.dir")
                + "/output/registrations.csv"));
        assertTrue(bRegCSVPresent);
        boolean bEncounterCSVPresent = Files.exists(Paths.get(System.getProperty("user.dir")
                + "/output/encounters.csv"));
        assertFalse(bEncounterCSVPresent);

        testDataGenApp.createCSVs(1,1, encounterScenarioGenerator);
        bEncounterCSVPresent = Files.exists(Paths.get(System.getProperty("user.dir")
                + "/output/encounters.csv"));
        bRegCSVPresent = Files.exists(Paths.get(System.getProperty("user.dir")
                + "/output/registrations.csv"));
        assertTrue(bEncounterCSVPresent);
        assertTrue(bRegCSVPresent);


    }
}