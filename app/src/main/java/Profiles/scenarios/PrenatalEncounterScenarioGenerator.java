package Profiles.scenarios;

import Profiles.Coding;
import Profiles.DrugOrder;
import Profiles.Encounter;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class PrenatalEncounterScenarioGenerator implements EncounterScenarioGenerator {

	private final EncounterScenarioGenerator delegate = new DefaultEncounterScenarioGenerator();

	@Override
	public List<Encounter> create(int patientAge, String patientGender, GregorianCalendar registrationDate) {
		if (patientGender.equals("F") && patientAge > 14 && patientAge < 40) {
			List<Encounter> encounters = new ArrayList<>();

			// 3% of all patients become pregnant
			if (chancePercent(3f)) {
				encounters.add(new Encounter(registrationDate)
						.setCondition(Coding.of(selectRandomDescendantOf("77386006"), Coding.SNOMED_URI)));// 77386006 |Pregnancy (finding)|

				if (chancePercent(78f)) {
					// 78% get iron and folic acid supplement
					encounters.add(new Encounter(registrationDate)
							.addDrugOrder(new DrugOrder(
									// 327552003 |Product containing precisely folic acid 400 microgram/1 each conventional release oral tablet (clinical drug)|
									Coding.of("327552003", Coding.SNOMED_URI),
									"Tablet", 1, "Tablet(s)", "Oral", "Once a day", false, 30, "Tablet(s)", 3))
							.addDrugOrder(new DrugOrder(
									// 1285040004 |Product containing precisely iron (as sucroferric oxyhydroxide) 500 milligram/1 each conventional release chewable tablet (clinical drug)|
									Coding.of("1285040004", Coding.SNOMED_URI),
									"Tablet", 1, "Tablet(s)", "Oral", "Once a day", false, 30, "Tablet(s)", 3)));

					// TODO: add iron supplement to Bahmni drugs list

					if (chancePercent(23f)) {
						// After 6 to 8 months
						registrationDate.add(Calendar.MONTH, ThreadLocalRandom.current().nextInt(6, 8));

						// Iron deficiency anemia
						encounters.add(new Encounter(registrationDate)
								.setDiagnosis(Coding.of(selectRandomDescendantOf("87522002"), Coding.SNOMED_URI)));// 87522002 |Iron deficiency anemia (disorder)|

						if (chancePercent(7f)) {
							// Premature delivery
							encounters.add(new Encounter(registrationDate)
									.setDiagnosis(Coding.of(selectRandomDescendantOf("282020008"), Coding.SNOMED_URI)));// 282020008 |Premature delivery (finding)|
						}
					} else {
						if (chancePercent(3.5f)) {
							// Premature delivery
							encounters.add(new Encounter(registrationDate)
									.setDiagnosis(Coding.of(selectRandomDescendantOf("282020008"), Coding.SNOMED_URI)));// 282020008 |Premature delivery (finding)|
						}
					}

					if (chancePercent(5.5f)) {
						// Pre-eclampsia
						encounters.add(new Encounter(registrationDate)
								.setDiagnosis(Coding.of(selectRandomDescendantOf("398254007"), Coding.SNOMED_URI)));// 398254007 |Pre-eclampsia (disorder)|
					}

				} else {
					// 22% do not get a supplement
					if (chancePercent(53f)) {
						// After 6 to 8 months
						registrationDate.add(Calendar.MONTH, ThreadLocalRandom.current().nextInt(6, 8));

						// Iron deficiency anemia
						encounters.add(new Encounter(registrationDate)
								.setDiagnosis(Coding.of(selectRandomDescendantOf("87522002"), Coding.SNOMED_URI)));// 87522002 |Iron deficiency anemia (disorder)|

						if (chancePercent(7f)) {
							// Premature delivery
							encounters.add(new Encounter(registrationDate)
									.setDiagnosis(Coding.of(selectRandomDescendantOf("282020008"), Coding.SNOMED_URI)));// 282020008 |Premature delivery (finding)|
						}
					} else {
						if (chancePercent(3.5f)) {
							// Premature delivery
							encounters.add(new Encounter(registrationDate)
									.setDiagnosis(Coding.of(selectRandomDescendantOf("282020008"), Coding.SNOMED_URI)));// 282020008 |Premature delivery (finding)|
						}
					}

					if (chancePercent(4.5f)) {
						// Pre-eclampsia
						encounters.add(new Encounter(registrationDate)
								.setDiagnosis(Coding.of(selectRandomDescendantOf("398254007"), Coding.SNOMED_URI)));// 398254007 |Pre-eclampsia (disorder)|
					}
				}
			}
			return encounters;
		} else {
			return delegate.create(patientAge, patientGender, registrationDate);
		}
	}

	Map<String, Set<String>> conceptTypeCache = new HashMap<>();
	private String selectRandomDescendantOf(String snomedCode) {

		if (!conceptTypeCache.containsKey(snomedCode)) {

		}
		Set<String> codes = conceptTypeCache.get(snomedCode);
		// return random one of these

		return snomedCode;// TODO: Grab page of 100 SNOMED CT concepts. Pick a random code from this.
		// e.g. https://snowstorm.ihtsdotools.org/fhir/ValueSet/$expand?count=100&url=http://snomed.info/sct?fhir_vs=isa/$snomedCode$
		// Use config SNOWSTORM_URL/ValueSet/$expand......
		// Need RestTemplate and maybe the HAPI FHIR R4 dependency - ca.uhn.hapi.fhir : hapi-fhir-structures-r4
	}

	private boolean chancePercent(float percent) {
		float probabilityFraction = percent / 100;
		return probabilityFraction >= Math.random();
	}

}
