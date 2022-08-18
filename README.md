Test data generator
---
---
This program creates patient and encounters CSV files and also allows to upload the same to Bahmni as an admin user.

It uses java-faker library to create random data. You can use JDK 11 or higher.
It uses org.apache.httpcomponents and org.apache.httpcomponents:httpmime to make the REST connections.
This library provides options to skip the ssl certificate validation with better cookie handling mechanism.
It uses org.json:json to parse responses.



 How to run the program
---
---
Defualt values - src/main/resource folder you can find config.yaml file.
Option 1 : 

Run "TestDataGenApp".
Enter the number of patient registration needed.
Choose 'y' or 'n' if you need encounter CSV.
Enter the number of patients that needs an encounter.
Choose 'y' or 'n' if you want to upload the CSV files.
On successful execution, two CSV files will be created in output folder and will be uploaded to configured env .

In default configuration you can change output file names, patient registration index start number , registration number initials , Bahmni env , credentials and location

Option 2:

Run ./gradlew run
It will pick up the default values from default values
On successful execution two CSV files will be created in output folder and will be uploaded to configured env .

Option 3:

Run export PATIENT_COUNT=10 && ./gradlew run  (set the environment variables)
It will pick up the runtime arguments which is passed from environmet variables and overrides the default values.
On successful execution two CSV files will be created in output folder and will be uploaded to configured env .

For more information - click [here](https://bahmni.atlassian.net/l/cp/QVEX0AYk)

