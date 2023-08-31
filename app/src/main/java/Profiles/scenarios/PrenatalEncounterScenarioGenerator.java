package Profiles.scenarios;

import Constants.Constant;
import Profiles.Coding;
import Profiles.DrugOrder;
import Profiles.Encounter;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.hl7.fhir.r4.model.ValueSet;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class PrenatalEncounterScenarioGenerator implements EncounterScenarioGenerator {

	private final EncounterScenarioGenerator delegate = new DefaultEncounterScenarioGenerator();

	Map<String, Set<Coding>> conceptTypeCache = new HashMap<>();

	@Override
	public List<Encounter> create(int patientAge, String patientGender, GregorianCalendar registrationDate) {
		if (patientGender.equals("F") && patientAge > 14 && patientAge < 40) {
			List<Encounter> encounters = new ArrayList<>();

			// 3% of all patients become pregnant
			if (chancePercent(3f)) {
				encounters.add(new Encounter(registrationDate)
						.setDiagnosis(selectRandomDescendantOf("77386006")));// 77386006 |Pregnancy (finding)|

				if (chancePercent(78f)) {
					// 78% get iron and folic acid supplement
					encounters.add(new Encounter(registrationDate)
							.addDrugOrder(new DrugOrder(
									// 327552003 |Product containing precisely folic acid 400 microgram/1 each conventional release oral tablet (clinical drug)|
									Coding.of("327552003", Coding.SNOMED_URI, "Product containing precisely folic acid 400 microgram/1 each conventional release oral tablet (clinical drug)"),
									"Tablet", 1, "Tablet(s)", "Oral", "Once a day", false, 30, "Tablet(s)", 3))
							.addDrugOrder(new DrugOrder(
									// 1285040004 |Product containing precisely iron (as sucroferric oxyhydroxide) 500 milligram/1 each conventional release chewable tablet (clinical drug)|
									Coding.of("1285040004", Coding.SNOMED_URI, "Product containing precisely iron (as sucroferric oxyhydroxide) 500 milligram/1 each conventional release chewable tablet (clinical drug)"),
									"Tablet", 1, "Tablet(s)", "Oral", "Once a day", false, 30, "Tablet(s)", 3)));

					// TODO: add iron supplement to Bahmni drugs list

					if (chancePercent(23f)) {
						// After 6 to 8 months
						registrationDate.add(Calendar.MONTH, ThreadLocalRandom.current().nextInt(6, 8));

						// if registration date is in the future, then we can't have any encounters after this date
						if (registrationDate.after(new GregorianCalendar())) {
							return encounters;
						}

						// Iron deficiency anemia
						encounters.add(new Encounter(registrationDate)
								.setDiagnosis(selectRandomDescendantOf("87522002")));// 87522002 |Iron deficiency anemia (disorder)|

						if (chancePercent(7f)) {
							// Premature delivery
							encounters.add(new Encounter(registrationDate)
									.setDiagnosis(selectRandomDescendantOf("282020008")));// 282020008 |Premature delivery (finding)|
						}
					} else {
						if (chancePercent(3.5f)) {
							// Premature delivery
							encounters.add(new Encounter(registrationDate)
									.setDiagnosis(selectRandomDescendantOf("282020008")));// 282020008 |Premature delivery (finding)|
						}
					}

					if (chancePercent(5.5f)) {
						// Pre-eclampsia
						encounters.add(new Encounter(registrationDate)
								.setDiagnosis(selectRandomDescendantOf("398254007")));// 398254007 |Pre-eclampsia (disorder)|
					}

				} else {
					// 22% do not get a supplement
					if (chancePercent(53f)) {
						// After 6 to 8 months
						registrationDate.add(Calendar.MONTH, ThreadLocalRandom.current().nextInt(6, 8));

						// Iron deficiency anemia
						encounters.add(new Encounter(registrationDate)
								.setDiagnosis(selectRandomDescendantOf("87522002")));// 87522002 |Iron deficiency anemia (disorder)|

						if (chancePercent(7f)) {
							// Premature delivery
							encounters.add(new Encounter(registrationDate)
									.setDiagnosis(selectRandomDescendantOf("282020008")));// 282020008 |Premature delivery (finding)|
						}
					} else {
						if (chancePercent(3.5f)) {
							// Premature delivery
							encounters.add(new Encounter(registrationDate)
									.setDiagnosis(selectRandomDescendantOf("282020008")));// 282020008 |Premature delivery (finding)|
						}
					}

					if (chancePercent(4.5f)) {
						// Pre-eclampsia
						encounters.add(new Encounter(registrationDate)
								.setDiagnosis(selectRandomDescendantOf("398254007")));// 398254007 |Pre-eclampsia (disorder)|
					}
				}
			}
			return encounters;
		} else {
			return delegate.create(patientAge, patientGender, registrationDate);
		}
	}

	private Coding selectRandomDescendantOf(String snomedCode) {
		if (!conceptTypeCache.containsKey(snomedCode)) {
			updateCache(snomedCode);
		}
		Set<Coding> codes = conceptTypeCache.get(snomedCode);
		// return random one of these
		int randomIndex = ThreadLocalRandom.current().nextInt(0, codes.size());
		return (Coding) codes.toArray()[randomIndex];
//		return snomedCode;// TODO: Grab page of 100 SNOMED CT concepts. Pick a random code from this.
		// e.g. https://snowstorm.ihtsdotools.org/fhir/ValueSet/$expand?count=100&url=http://snomed.info/sct?fhir_vs=isa/$snomedCode$
		// Use config SNOWSTORM_URL/ValueSet/$expand......
		// Need RestTemplate and maybe the HAPI FHIR R4 dependency - ca.uhn.hapi.fhir : hapi-fhir-structures-r4
	}
	private void updateCache(String snomedCode) {
		String url = Constant.SNOWSTORM_URL + "/ValueSet/$expand?count=100&url=http://snomed.info/sct?fhir_vs=isa/" + snomedCode;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);

		try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				String jsonResponse = EntityUtils.toString(response.getEntity());
				IParser iParser = FhirContext.forR4().newJsonParser();
				iParser.setPrettyPrint(true);

				//Get Valueset from above json response
				ValueSet valueSet = iParser.parseResource(ValueSet.class, jsonResponse);
				//Extract codes from valueset to set of Profiles.Coding
				Set<Coding> codes = new HashSet<>();
				valueSet.getExpansion().getContains().forEach(coding -> codes.add(Coding.of(coding.getCode(), coding.getSystem(), coding.getDisplay())));
				//Add to cache
				conceptTypeCache.put(snomedCode, codes);
			} else {
				System.err.println("Request failed with status code: " + statusCode);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean chancePercent(float percent) {
		float probabilityFraction = percent / 100;
		return probabilityFraction >= Math.random();
	}

}
