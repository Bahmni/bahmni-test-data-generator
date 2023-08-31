package Profiles.upload.model;

public class ConceptName {

    //create pojo for JSON with below example
//    {
//        "name" : "Fever (finding)",
//            "locale": "en",
//            "conceptNameType": "FULLY_SPECIFIED"
//    }

    private String name;
    private String locale;
    private String conceptNameType;

    public ConceptName(String name, String locale, String conceptNameType) {
        this.name = name;
        this.locale = locale;
        this.conceptNameType = conceptNameType;
    }

    public String getName() {
        return name;
    }

    public String getLocale() {
        return locale;
    }

    public String getConceptNameType() {
        return conceptNameType;
    }
}
