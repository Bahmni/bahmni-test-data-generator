package Profiles.upload.model;

public class ConceptReferenceTerm {

    private String name;
    private String code;
    private String conceptSource;

    public ConceptReferenceTerm(String name, String code, String conceptSource) {
        this.name = name;
        this.code = code;
        this.conceptSource = conceptSource;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getConceptSource() {
        return conceptSource;
    }
}
