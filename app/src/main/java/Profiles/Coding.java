package Profiles;

public class Coding {

	public static final String SNOMED_URI = "http://snomed.info/sct";

	private final String code;
	private final String system;

	private Coding(String code, String system) {
		this.code = code;
		this.system = system;
	}

	public static Coding of(String code, String system) {
		return new Coding(code, system);
	}

	public String getCode() {
		return code;
	}

	public String getSystem() {
		return system;
	}
}
