package Profiles.scenarios;

import Profiles.Encounter;

import java.util.GregorianCalendar;
import java.util.List;

public class DefaultEncounterScenarioGenerator implements EncounterScenarioGenerator {

	@Override
	public List<Encounter> create(int patientAge, String patientGender, GregorianCalendar registrationDate) {
		return List.of(new Encounter(registrationDate)
				.setChiefComplaintDuration("12")
				.setChiefComplaintNotes("Test notes")
				.setHistoryNotes("history notes")
				.setExaminationNotes("exam notes")
				.setSmokingHistory("yes")
				.setDiagnosis("Fever")
				.setCondition("Infectious diarrhea")
				.setConsultationNote("Testing Consultation Note")
				.setHospitalCourse("Testing hospital course comment")
				.setOperativeNotesCondition("Stable")
				.setOperativeNotesProcedure("Wound Care")
				.setProcedureNotesDiagnosis("Need dressing"));
	}

}
