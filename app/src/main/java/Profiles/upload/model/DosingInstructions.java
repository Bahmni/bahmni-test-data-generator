package Profiles.upload.model;

public class DosingInstructions {
    private int dose;
    private String doseUnits;
    private String route;
    private String frequency;
    private boolean asNeeded;
    private String administrationInstructions;
    private int quantity;
    private String quantityUnits;
    private int numberOfRefills;

    public DosingInstructions(int dose, String doseUnits, String route, String frequency, boolean asNeeded, String administrationInstructions, int quantity, String quantityUnits, int numberOfRefills) {
        this.dose = dose;
        this.doseUnits = doseUnits;
        this.route = route;
        this.frequency = frequency;
        this.asNeeded = asNeeded;
        this.administrationInstructions = administrationInstructions;
        this.quantity = quantity;
        this.quantityUnits = quantityUnits;
        this.numberOfRefills = numberOfRefills;
    }

    public int getDose() {
        return dose;
    }

    public String getDoseUnits() {
        return doseUnits;
    }

    public String getRoute() {
        return route;
    }

    public String getFrequency() {
        return frequency;
    }

    public boolean isAsNeeded() {
        return asNeeded;
    }

    public String getAdministrationInstructions() {
        return administrationInstructions;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getQuantityUnits() {
        return quantityUnits;
    }

    public int getNumberOfRefills() {
        return numberOfRefills;
    }

}
