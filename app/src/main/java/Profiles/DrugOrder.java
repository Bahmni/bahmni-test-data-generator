package Profiles;

public class DrugOrder {

	private Coding drug;
	private String form;
	private int dose;
	private String doseUnits;
	private String route;
	private String frequency;
	private boolean asNeeded;
	private int quantity;
	private String quantityUnits;
	private int numberOfRefills;

	public DrugOrder(Coding drug, String form, int dose, String doseUnits, String route, String frequency, boolean asNeeded, int quantity, String quantityUnits,
			int numberOfRefills) {
		this.drug = drug;
		this.form = form;
		this.dose = dose;
		this.doseUnits = doseUnits;
		this.route = route;
		this.frequency = frequency;
		this.asNeeded = asNeeded;
		this.quantity = quantity;
		this.quantityUnits = quantityUnits;
		this.numberOfRefills = numberOfRefills;
	}

	public Coding getDrug() {
		return drug;
	}

	public void setDrug(Coding drug) {
		this.drug = drug;
	}

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	public int getDose() {
		return dose;
	}

	public void setDose(int dose) {
		this.dose = dose;
	}

	public String getDoseUnits() {
		return doseUnits;
	}

	public void setDoseUnits(String doseUnits) {
		this.doseUnits = doseUnits;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public boolean isAsNeeded() {
		return asNeeded;
	}

	public void setAsNeeded(boolean asNeeded) {
		this.asNeeded = asNeeded;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getQuantityUnits() {
		return quantityUnits;
	}

	public void setQuantityUnits(String quantityUnits) {
		this.quantityUnits = quantityUnits;
	}

	public int getNumberOfRefills() {
		return numberOfRefills;
	}

	public void setNumberOfRefills(int numberOfRefills) {
		this.numberOfRefills = numberOfRefills;
	}
}
