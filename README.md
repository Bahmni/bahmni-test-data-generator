Test data generator
---
---
This program creates patient and encounters CSV files which can be either uploaded manually into the app using admin console-> import feature or the program itself uploads to the configured Bahmni env. The program assumes that Bahmni is under default configuration. The CSVs are designed accordingly. In case your instance of Bahmni has customization, update the code accordingly

It uses java-faker library to create random data. You can use JDK 11 or higher.
It uses org.apache.httpcomponents and org.apache.httpcomponents:httpmime to make connection with the bahmni core apis
This library provides options to skip the ssl certificate validation and better cookies handling
It uses org.json:json to parse responses



 How to run the program
---
---
Option 1 : 

Run "TestDataGenApp".
Enter the number of patient registration needed.
Choose 'y' or 'n' if you need encounter CSV.
Enter the number of profiles need to have encounter information.
Choose 'y' or 'n' if you want to upload the CSV
On successful execution, two CSV files will be created in output folder and will be uploaded to configured env .

In src/main/resource folder you can find config.yaml file.
Here you can change output file names, patient registration index start number , registration number initials , Bahmni env , credentials and location

Option 2:

Run ./gradlew run
It will pick up the default values configured in config.yaml 
On successful execution two CSV files will be created in output folder and will be uploaded to configured env .

Option 3:

Run ./gradlew run -DpatientProfileCount=? -DsCreateContact=? -DcontactCount=? -DsUploadCsv=? (Replace ? with values)
It will pick up the runtime arguments which is passed
On successful execution two CSV files will be created in output folder and will be uploaded to configured env .

