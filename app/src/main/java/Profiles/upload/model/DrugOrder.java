package Profiles.upload.model;

public class DrugOrder {

    private String careSetting;
    private Drug drug;
    private String orderType;
    private String dosingInstructionType;
    private DosingInstructions dosingInstructions;
    private int duration;
    private String durationUnits;

    public DrugOrder(String careSetting, Drug drug, String orderType, String dosingInstructionType, DosingInstructions dosingInstructions, int duration, String durationUnits) {
        this.careSetting = careSetting;
        this.drug = drug;
        this.orderType = orderType;
        this.dosingInstructionType = dosingInstructionType;
        this.dosingInstructions = dosingInstructions;
        this.duration = duration;
        this.durationUnits = durationUnits;
    }

    public String getCareSetting() {
        return careSetting;
    }

    public Drug getDrug() {
        return drug;
    }

    public String getOrderType() {
        return orderType;
    }

    public String getDosingInstructionType() {
        return dosingInstructionType;
    }

    public DosingInstructions getDosingInstructions() {
        return dosingInstructions;
    }

    public int getDuration() {
        return duration;
    }

    public String getDurationUnits() {
        return durationUnits;
    }
}
