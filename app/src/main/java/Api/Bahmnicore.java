package Api;

import Config.LoggerConfig;
import Constants.Constant;
import Jsonparser.Parser;
import Profiles.Coding;
import Profiles.upload.model.Concept;
import Profiles.upload.model.ConceptMapping;
import Profiles.upload.model.ConceptName;
import Profiles.upload.model.ConceptReferenceTerm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


public class Bahmnicore extends Request {

    Logger logger = LoggerConfig.LOGGER;
    static Map<String, String> headers = new HashMap<>();

    static {
        headers.put("Accept", "*/*");
        headers.put("Accept-Encoding", "gzip, deflate, br");
        headers.put("Accept-Language", "en-GB,en-US;q=0.9,en;q=0.8");
    }

    public void uploadPatients() {
        String path = "/openmrs/ws/rest/v1/bahmnicore/admin/upload/patient";
        post(path, headers, System.getProperty("user.dir") + "/" + Constant.PATIENT_PROFILE_FILE_NAME);
    }

    public void uploadEncounters() {
        String path = "/openmrs/ws/rest/v1/bahmnicore/admin/upload/encounter";
        post(path, headers, System.getProperty("user.dir") + "/" + Constant.ENCOUNTER_PROFILE_FILE_NAME);
    }

    public void uploadDrugorders(String requestBody) {
        String path = "/openmrs/ws/rest/v1/bahmnicore/bahmniencounter";
        post(requestBody, path, headers);
    }

    public String getDrugUuid(Coding drug) {
        String path = "/openmrs/ws/rest/v1/drug";
        Map<String, String> params = new HashMap<>();
        params.put("q", drug.getCode());
        params.put("s", "default");
        params.put("limit", "1");
        Parser parser = new Parser(get(path, headers, params));
        List<String> uuidList = parser.getValuesForGivenKey("uuid");
        return uuidList.isEmpty() ? null : uuidList.get(0) ;
    }

    public void verifyUpload(String name) {
        try {
            Thread.sleep(20000);
            String path = "/openmrs/ws/rest/v1/bahmnicore/admin/upload/status";
            Parser parser = new Parser(get(path, headers));
            String status = parser.getStringFromArray("status");
            String filename = parser.getStringFromArray("savedFileName");
            switch (status) {
                case "COMPLETED":
                    logger.info(filename + " : UPLOAD " + status);
                    break;
                case "COMPLETED_WITH_ERRORS":
                    if(name.equalsIgnoreCase("PATIENT")) {
                        throw new RuntimeException(filename + " : UPLOAD " + status);
                    }
                    logger.info(filename + " : UPLOAD " + status);
                    break;
                case "IN_PROGRESS":
                    verifyUpload(name);
                    break;
                default:
                    throw new IllegalStateException("Unexpected upload status : " + status);
            }
        } catch (JSONException e) {
            logger.severe("JSON Parse in verify Upload " + e.getLocalizedMessage());
            verifyUpload(name);
        } catch (Exception e) {
            logger.severe("Error in verify Upload " + e.getLocalizedMessage());
        }

    }

    public String getPatientUuidFromIdentifier(String id) {
        String path = "/openmrs/ws/rest/v1/patient";
        Map<String, String> params = new HashMap<>();
        params.put("q", id);
        params.put("v", "default");
        params.put("limit", "1");
        Parser parser = new Parser(get(path, headers, params));
        List<String> uuidList = parser.getValuesForGivenKey("uuid");
        return uuidList.isEmpty() ? null : uuidList.get(0) ;
    }

    public void getPatientVisitAndEncounterUuid(String patientId, String patientUuid, Map<String, String> patientIdentifierToVisitUuidMap, Map<String, String> patientIdentifierToEncounterUuidMap) {
        String path = "/openmrs/ws/rest/v1/visit";
        Map<String, String> params = new HashMap<>();
        params.put("patient", patientUuid);
        params.put("v", "default");
        Parser parser = new Parser(get(path, headers, params));
        List<String> uuidList = parser.getValuesForGivenKey("uuid");
        String visitUuid = uuidList.isEmpty() ? null : uuidList.get(0) ;

        if (visitUuid == null) {
            try {
                Thread.sleep(5000);
                parser = new Parser(get(path, headers, params));
                uuidList = parser.getValuesForGivenKey("uuid");
                visitUuid = uuidList.isEmpty() ? null : uuidList.get(0) ;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("patientId " + patientId + " patientUuid : " + patientUuid + " visitUuid : " + visitUuid);
        if (visitUuid == null)
            return;
        List<String> encounters = parser.getValuesForGivenKey("encounters");
        String encountersElement = encounters.isEmpty() ? null : encounters.get(0) ;

        List<String> encountersUuid = parser.getValuesForGivenKeyAndResponse(encountersElement, "uuid");
        String encounterUuid = encountersUuid.isEmpty() ? null : encountersUuid.get(0) ;

        patientIdentifierToVisitUuidMap.put(patientId, visitUuid);
        patientIdentifierToEncounterUuidMap.put(patientId, encounterUuid);
    }
    public void createDiagnosisConcepts(Map<String, Coding> newDiagnosisMap) {
        String referenceTermPathPrefix = "/openmrs/ws/rest/v1/conceptreferenceterm?codeOrName=";

        if (newDiagnosisMap.isEmpty())
            return;

        newDiagnosisMap.entrySet().stream().forEach(entry -> {
            String referenceTermUuid = getReferenceTermUuid(referenceTermPathPrefix, entry);
            if (referenceTermUuid == null) {
                referenceTermUuid = createReferenceTerm(entry.getValue().getLabel(), entry.getKey(), referenceTermPathPrefix);
                System.out.println(referenceTermUuid);
                ConceptName conceptName = new ConceptName(entry.getValue().getLabel(), "en", "FULLY_SPECIFIED");
                ConceptMapping conceptMapping = new ConceptMapping(referenceTermUuid, Constant.CONCEPT_MAPPING_SAME_AS_UUID);
                Concept concept = new Concept(Collections.singletonList(conceptName), Constant.CONCEPT_DATATYPE_NA_UUID, Constant.CONCEPT_CLASS_DIAGNOSIS_UUID, Collections.singletonList(conceptMapping));
                ObjectMapper mapper = new ObjectMapper();
                try {
                    String conceptPayload = mapper.writeValueAsString(concept);
                    String conceptCreatePath = "/openmrs/ws/rest/v1/concept";
                    post(conceptPayload, conceptCreatePath, headers);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    private String getReferenceTermUuid(String referenceTermPathPrefix, Map.Entry<String, Coding> entry) {
        String referenceCode = entry.getKey();
        String referenceTermPath = referenceTermPathPrefix + referenceCode;
        System.out.println(referenceTermPath);
        Parser parser = new Parser(get(referenceTermPath, headers));
        List<String> uuidList = parser.getValuesForGivenKey("uuid");
        return uuidList.isEmpty() ? null : uuidList.get(0);
    }
    private String createReferenceTerm(String name, String referenceCode, String referenceTermPathPrefix) {
        List<String> uuidList;
        // create reference term with code and coding system name
        String referenceTermPath = "/openmrs/ws/rest/v1/conceptreferenceterm";
        ConceptReferenceTerm conceptReferenceTerm = new ConceptReferenceTerm(name, referenceCode, Constant.CONCEPT_SOURCE_SNOMED_UUID);
        ObjectMapper mapper = new ObjectMapper();
        try {
            String requestBody = mapper.writeValueAsString(conceptReferenceTerm);
            post(requestBody, referenceTermPath, headers);
            Parser parser = new Parser(get(referenceTermPathPrefix + referenceCode, headers));
            uuidList = parser.getValuesForGivenKey("uuid");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return uuidList.isEmpty() ? null : uuidList.get(0);
    }

    public void updateSearchIndex() {
        String path = "/openmrs/ws/rest/v1/searchindexupdate";
        post(null, path, headers);

        try {
            Thread.sleep(15*1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
