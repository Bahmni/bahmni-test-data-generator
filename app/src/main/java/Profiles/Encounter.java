package Profiles;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Encounter {

	private final Date date;
	private String chiefComplaintDuration;
	private String chiefComplaintNotes;
	private String historyNotes;
	private String examinationNotes;
	private String smokingHistory;
	private Coding diagnosis;
	private Coding condition;
	private String consultationNote;
	private String hospitalCourse;
	private String operativeNotesCondition;
	private String operativeNotesProcedure;
	private String procedureNotesDiagnosis;
	private List<DrugOrder> drugOrders;

	public Encounter(Calendar date) {
		this.date = date.getTime();
		drugOrders = new ArrayList<>();
	}

	public Encounter addDrugOrder(DrugOrder drugOrder) {
		drugOrders.add(drugOrder);
		return this;
	}

	public Date getDate() {
		return date;
	}

	public String getChiefComplaintDuration() {
		return chiefComplaintDuration;
	}

	public Encounter setChiefComplaintDuration(String chiefComplaintDuration) {
		this.chiefComplaintDuration = chiefComplaintDuration;
		return this;
	}

	public String getChiefComplaintNotes() {
		return chiefComplaintNotes;
	}

	public Encounter setChiefComplaintNotes(String chiefComplaintNotes) {
		this.chiefComplaintNotes = chiefComplaintNotes;
		return this;
	}

	public String getHistoryNotes() {
		return historyNotes;
	}

	public Encounter setHistoryNotes(String historyNotes) {
		this.historyNotes = historyNotes;
		return this;
	}

	public String getExaminationNotes() {
		return examinationNotes;
	}

	public Encounter setExaminationNotes(String examinationNotes) {
		this.examinationNotes = examinationNotes;
		return this;
	}

	public String getSmokingHistory() {
		return smokingHistory;
	}

	public Encounter setSmokingHistory(String smokingHistory) {
		this.smokingHistory = smokingHistory;
		return this;
	}

	public Coding getDiagnosis() {
		return diagnosis;
	}

	public Encounter setDiagnosis(Coding diagnosis) {
		this.diagnosis = diagnosis;
		return this;
	}

	public Coding getCondition() {
		return condition;
	}

	public Encounter setCondition(Coding condition) {
		this.condition = condition;
		return this;
	}

	public String getConsultationNote() {
		return consultationNote;
	}

	public Encounter setConsultationNote(String consultationNote) {
		this.consultationNote = consultationNote;
		return this;
	}

	public String getHospitalCourse() {
		return hospitalCourse;
	}

	public Encounter setHospitalCourse(String hospitalCourse) {
		this.hospitalCourse = hospitalCourse;
		return this;
	}

	public String getOperativeNotesCondition() {
		return operativeNotesCondition;
	}

	public Encounter setOperativeNotesCondition(String operativeNotesCondition) {
		this.operativeNotesCondition = operativeNotesCondition;
		return this;
	}

	public String getOperativeNotesProcedure() {
		return operativeNotesProcedure;
	}

	public Encounter setOperativeNotesProcedure(String operativeNotesProcedure) {
		this.operativeNotesProcedure = operativeNotesProcedure;
		return this;
	}

	public String getProcedureNotesDiagnosis() {
		return procedureNotesDiagnosis;
	}

	public Encounter setProcedureNotesDiagnosis(String procedureNotesDiagnosis) {
		this.procedureNotesDiagnosis = procedureNotesDiagnosis;
		return this;
	}

	public List<DrugOrder> getDrugOrders() {
		return drugOrders;
	}
}
