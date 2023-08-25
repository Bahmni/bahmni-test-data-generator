package Profiles.scenarios;

import Profiles.Coding;
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
				.setDiagnosis(getCoding("386661006", "Fever (finding)")) //Fever
				.setCondition(getCoding("19213003", "Infectious diarrheal disease (disorder)")) // "Infectious diarrhea"
				.setConsultationNote("Testing Consultation Note")
				.setHospitalCourse("Testing hospital course comment")
				.setOperativeNotesCondition("Stable")
				.setOperativeNotesProcedure("Wound Care")
				.setProcedureNotesDiagnosis("Need dressing"));
	}

	private Coding getCoding(String code, String label) {
		return Coding.of(code, Coding.SNOMED_URI, label);
	}

}
