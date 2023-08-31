package Profiles.upload.model;

import java.util.List;
public class Concept {

    private List<ConceptName> names;
    private String datatype;
    private String conceptClass;

    private List<ConceptMapping> mappings;

    public Concept(List<ConceptName> names, String datatype, String conceptClass, List<ConceptMapping> mappings) {
        this.names = names;
        this.datatype = datatype;
        this.conceptClass = conceptClass;
        this.mappings = mappings;
    }

    public List<ConceptName> getNames() {
        return names;
    }

    public String getDatatype() {
        return datatype;
    }

    public String getConceptClass() {
        return conceptClass;
    }

    public List<ConceptMapping> getMappings() {
        return mappings;
    }
}
