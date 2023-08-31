package Profiles;

import Api.Bahmnicore;
import Api.Request;
import Config.LoggerConfig;
import Profiles.upload.model.BahmniEncounter;
import Profiles.upload.model.DosingInstructions;
import Profiles.upload.model.Drug;
import Profiles.upload.model.DrugOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DrugOrderProfile {
    private final List<DrugOrderEncounter> drugOrderEncounterList;

    Logger logger = LoggerConfig.LOGGER;

    private Map<String, String> patientIdentifierToPatientUuidMap = new HashMap<>();

    private Map<String, String> patientIdentifierToVisitUuidMap = new HashMap<>();

    private Map<String, String> patientIdentifierToEncounterUuidMap = new HashMap<>();

    public DrugOrderProfile(List<DrugOrderEncounter> drugOrderEncounterList) {
        this.drugOrderEncounterList = drugOrderEncounterList;
    }

    public void uploadDrugOrders() {
        Map<String, String> drugNameToUuidMap = new HashMap<>();
        Bahmnicore bahmnicore = new Bahmnicore();
        drugOrderEncounterList.forEach(drugOrderEncounter -> {
            if (!patientIdentifierToPatientUuidMap.containsKey(drugOrderEncounter.getRegistrationNumber())) {
                updateCache(drugOrderEncounter.getRegistrationNumber());
            }
            String patientUuid = patientIdentifierToPatientUuidMap.get(drugOrderEncounter.getRegistrationNumber());
            String visitUuid = patientIdentifierToVisitUuidMap.get(drugOrderEncounter.getRegistrationNumber());
            String encounterUuid = patientIdentifierToEncounterUuidMap.get(drugOrderEncounter.getRegistrationNumber());
            String locationUuid = Request.getUuid();

            if (patientUuid == null || visitUuid == null || encounterUuid == null) {
                logger.warning("Patient with identifier " + drugOrderEncounter.getRegistrationNumber() + " visitUuid " + visitUuid + " encounterUuid " + encounterUuid);
            } else {
                List<DrugOrder> drugOrders = drugOrderEncounter.getDrugOrders().stream().map(drugOrder -> {
                    if (!drugNameToUuidMap.containsKey(drugOrder.getDrug().getCode())) {
                        String drugUuid = bahmnicore.getDrugUuid(drugOrder.getDrug());
                        drugNameToUuidMap.put(drugOrder.getDrug().getCode(), drugUuid);
                    }
                    String drugUuid = drugNameToUuidMap.get(drugOrder.getDrug().getCode());  //TODO: Get drug uuid from OpenMRS
                    DosingInstructions dosingInstructions = new DosingInstructions(drugOrder.getDose(), drugOrder.getDoseUnits(), drugOrder.getRoute(), drugOrder.getFrequency(), drugOrder.isAsNeeded(), "{\"instructions\":\"As directed\"}", drugOrder.getQuantity(), drugOrder.getQuantityUnits(), drugOrder.getNumberOfRefills());
                    Drug drug = new Drug(drugOrder.getDrug().getLabel(), drugOrder.getForm(), drugUuid);
                    DrugOrder order = new DrugOrder("OUTPATIENT", drug, "Drug Order", "org.openmrs.module.bahmniemrapi.drugorder.dosinginstructions.FlexibleDosingInstructions", dosingInstructions, drugOrder.getQuantity() / drugOrder.getNumberOfRefills(), "Day(s)");
                    return order;
                }).collect(Collectors.toList());

                if (!drugOrders.isEmpty()) {
                    BahmniEncounter bahmniEncounter = new BahmniEncounter(locationUuid, patientUuid, encounterUuid, visitUuid, "OPD", drugOrders, "81852aee-3f10-11e4-adec-0800271c1b75"); //TODO: Get encounter type uuid from OpenMRS
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        bahmnicore.uploadDrugorders(mapper.writeValueAsString(bahmniEncounter));
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    private void updateCache(String patientRegistrationNumber) {
        Bahmnicore bah = new Bahmnicore();
        String patientUuidFromIdentifier = bah.getPatientUuidFromIdentifier(patientRegistrationNumber);
        if (patientUuidFromIdentifier != null) {
            patientIdentifierToPatientUuidMap.put(patientRegistrationNumber, patientUuidFromIdentifier);
            bah.getPatientVisitAndEncounterUuid(patientRegistrationNumber, patientUuidFromIdentifier, patientIdentifierToVisitUuidMap, patientIdentifierToEncounterUuidMap);
        }
    }
}
