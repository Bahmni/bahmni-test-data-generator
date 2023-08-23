package Profiles.scenarios;

import Profiles.Encounter;

import java.util.GregorianCalendar;
import java.util.List;

public interface EncounterScenarioGenerator {

	List<Encounter> create(int patientAge, String patientGender, GregorianCalendar registrationDate);

}
