Test data generator
---
---
This program creates patient and encounter CSV files which can be uploaded into app using admin console-> import feature.
The program assumes that Bahmni is under default configuration. The CSVs are designed accordingly. In case your instance of Bahmni has customisation, update the code accordingly.

It uses java-faker library to crate random data. You can use JDK 11 or higher.


 How to run the program
---
---
Run "TestDataGenApp".
Enter the number of patient registration needed.
Choose 'y' or 'n' if you need encounter CSV.
Enter the percentage of profiles need to have encounter information. You need to put number only. Don`t put %.
On successful execution, two CSV files will be created in output folder. Now you are ready import them into app.

In src/main/resource folder you can find config.yaml file.
Here you can change output file names, patient registration index start number and registration number initials.
