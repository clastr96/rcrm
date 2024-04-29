# RaynetCRM
RaynetCRM is a Java application for importing client data from CSV files into a CRM system.

## Features
- Import client data from CSV files
- Process client data and update existing clients or create new ones in the CRM system
- Asynchronous processing for improved performance
- Integration with external CRM system via REST API

## Requirements
- Gradle
- Docker
- Docker Compose

## Installation
- Clone the repository: git clone https://github.com/clastr96/rcrm.git
- Navigate to the project directory: cd raynet-crm
- Build the project: **./gradlew build** 
- The application is prepared to run in docker containers, run : **docker-compose up -d** to start the java and mysql
  containers

## Tests
- Run **./gradlew test** to trigger the unit tests

## Usage
- Prepare your client data in a CSV file with the following format:
regNumber;title;email;phone
123456;Example Company;example@example.com;123-456-7890
- Call the **localhost:8080/uploadData** endpoint with the csv file as a request body
- The application will process the CSV file asynchronously, updating existing clients or creating new ones in the CRM system
- Not processed clients (after rate limit hit) will be processed in hourly scheduled job

## Technologies Used
- Java
- Gradle
- Spring Framework
- Spring Boot
- Lombok
- OpenCSV
- RESTful API
