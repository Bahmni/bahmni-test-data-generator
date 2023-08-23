Test data generator
---
---
This program creates patient and encounters CSV files and also allows to upload the same to Bahmni as an admin user.

- java-faker library to create random data. 
- JDK 11 or higher.
- REST client: org.apache.httpcomponents. This library provides options to skip the ssl certificate validation with better cookie handling mechanism.
- org.json:json to parse responses.

 How to run the program
---
---
The application expects below environment variables in the absence of which it would consider default values from `resource/config.yaml` 
```shell
export PATIENT_COUNT=10 &&
export S_CREATE_ENCOUNTER=y &&
export ENCOUNTER_COUNT=5 &&
export S_UPLOAD_CSV=y &&
export USERNAME=admin &&
export PASSWORD=nimda &&
export LOCATION=frontdesk
```
You can run the application via gradle `./gradlew run`

### Data Analytics Scenarios
By default, the generated patients will all have the same encounters.
It is possible to add patterns into the data that mimic more realistic scenarios. This can be useful for testing reporting or data analytics.

Use the `SCENARIO` environment variable to control this. For example:
```
export SCENARIO=PRENATAL_SUPPLEMENTS
```
The following scenarios are available:

| Constant             | Behaviour                                                                                                                                                                                                                                             |
|----------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| DEFAULT              | Patient details are random. Encounter data is all the same.                                                                                                                                                                                           |
| PRENATAL_SUPPLEMENTS | Patient details are random. A small percentage of female patients become pregnant. Some take folic acid and iron supplements. This has a reduced correlation with anemia and premature delivery. The rest of the patients follow the DEFAULT profile. |


### Output
On successful execution two CSV files will be created in `/output` folder and (conditionally) will be uploaded.

 Documentation
---
---
For more information - click [here](https://bahmni.atlassian.net/l/cp/QVEX0AYk)

