package Profiles.upload.model;

import java.util.List;

public class BahmniEncounter {

    private String locationUuid;
    private String patientUuid;
    private String encounterUuid;
    private String visitUuid;
    private String visitType;
    private List<DrugOrder> drugOrders;
    private String encounterTypeUuid;

    public BahmniEncounter(String locationUuid, String patientUuid, String encounterUuid, String visitUuid, String visitType, List<DrugOrder> drugOrders, String encounterTypeUuid) {
        this.locationUuid = locationUuid;
        this.patientUuid = patientUuid;
        this.encounterUuid = encounterUuid;
        this.visitUuid = visitUuid;
        this.visitType = visitType;
        this.drugOrders = drugOrders;
        this.encounterTypeUuid = encounterTypeUuid;
    }

    public String getLocationUuid() {
        return locationUuid;
    }

    public String getPatientUuid() {
        return patientUuid;
    }

    public String getEncounterUuid() {
        return encounterUuid;
    }

    public String getVisitUuid() {
        return visitUuid;
    }

    public String getVisitType() {
        return visitType;
    }


    public List<DrugOrder> getDrugOrders() {
        return drugOrders;
    }

    public String getEncounterTypeUuid() {
        return encounterTypeUuid;
    }

}
