package Profiles.upload.model;

public class ConceptMapping {

         private String conceptReferenceTerm;
        private String conceptMapType;

        public ConceptMapping(String conceptReferenceTerm, String conceptMapType) {
            this.conceptReferenceTerm = conceptReferenceTerm;
            this.conceptMapType = conceptMapType;
        }

        public String getConceptReferenceTerm() {
            return conceptReferenceTerm;
        }

        public String getConceptMapType() {
            return conceptMapType;
        }

}
