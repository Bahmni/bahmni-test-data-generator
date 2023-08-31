package Profiles;

import java.util.List;
public class DrugOrderEncounter {

    private String registrationNumber;
    private String encounterType;
    private String visitType;
    private String visitStartDate;
    private String visitEndDate;
    private String encounterDate;
    private List<DrugOrder> drugOrders;

    public DrugOrderEncounter(String registrationNumber, String encounterType, String visitType, String visitStartDate, String visitEndDate, String encounterDate, List<DrugOrder> drugOrders) {
        this.registrationNumber = registrationNumber;
        this.encounterType = encounterType;
        this.visitType = visitType;
        this.visitStartDate = visitStartDate;
        this.visitEndDate = visitEndDate;
        this.encounterDate = encounterDate;
        this.drugOrders = drugOrders;
    }
    public String getRegistrationNumber() {
        return registrationNumber;
    }
    public String getEncounterType() {
        return encounterType;
    }
    public String getVisitType() {
        return visitType;
    }
    public String getVisitStartDate() {
        return visitStartDate;
    }
    public String getVisitEndDate() {
        return visitEndDate;
    }
    public String getEncounterDate() {
        return encounterDate;
    }
    public List<DrugOrder> getDrugOrders() {
        return drugOrders;
    }
}
