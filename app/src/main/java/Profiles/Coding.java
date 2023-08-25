package Profiles;

public class Coding {

	public static final String SNOMED_URI = "http://snomed.info/sct";

	private final String code;
	private final String system;
	private final String label;

	private Coding(String code, String system, String label) {
		this.code = code;
		this.system = system;
		this.label = label;
	}

	public static Coding of(String code, String system, String label) {
		return new Coding(code, system, label);
	}

	public String getCode() {
		return code;
	}

	public String getSystem() {
		return system;
	}

	public String getLabel() {
		return label;
	}
}
