# Holiday API Spring Boot

This project implements the Accenture developer assessment using Spring Boot and the Nager.Date API.

## Assessment Description

Implement a Spring Boot application for the Accenture developer assessment using the Nager.Date API, with the following features:

1. **Given a country, return the last celebrated 3 holidays (date and name).**
2. **Given a year and country codes, for each country return the number of public holidays not falling on weekends (sorted descending).**
3. **Given a year and 2 country codes, return the deduplicated list of dates celebrated in both countries (date + local names).**

### Requirements
- Production-ready code
- Test coverage
- Documentation on how to run
- Code ready for GitHub

## Features
- Given a country, return the last celebrated 3 holidays (date and name).
- Given a year and country codes, for each country return a number of public holidays not falling on weekends (sorted descending).
- Given a year and 2 country codes, return the deduplicated list of dates celebrated in both countries (date + local names).

## How to Run

1. **Build the project:**
   ```sh
   mvn clean install
   ```
2. **Run the application:**
   ```sh
   mvn spring-boot:run
   ```
   The API will be available at `http://localhost:8080/api/holidays`.

## API Endpoints

- `GET /api/holidays/last3/{countryCode}`
- `GET /api/holidays/count-not-weekend?year=YYYY&countryCodes=IT,US,...`
- `GET /api/holidays/common?year=YYYY&countryCode1=IT&countryCode2=US`

## API Specification

- **Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI JSON:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

## Quick API Verification

After starting the application, you can verify it is running with a sample `curl` command:

- **Get last 3 holidays for US:**
  ```sh
  curl http://localhost:8080/api/holidays/last3/US
  ```

- **Get count of holidays not on weekends for US and IT in 2024:**
  ```sh
  curl "http://localhost:8080/api/holidays/count-not-weekend?year=2024&countryCodes=US,IT"
  ```

- **Get common holidays for US and IT in 2024:**
  ```sh
  curl "http://localhost:8080/api/holidays/common?year=2024&countryCode1=US&countryCode2=IT"
  ```

## Useful Maven CLI Commands

- **Build the project:**
  ```sh
  mvn clean install
  ```
- **Run the application:**
  ```sh
  mvn spring-boot:run
  ```
- **Run tests:**
  ```sh
  mvn test
  ```
- **Package the application:**
  ```sh
  mvn package
  ```

## Test Coverage

This project uses JUnit 5 for unit and integration tests. Code coverage is measured with JaCoCo.

### Run Tests

- **Run all tests:**
  ```sh
  mvn test
  ```

### Generate Coverage Report

- **Generate the report:**
  ```sh
  mvn test
  # Open target/site/jacoco/index.html in your browser for the report
  ```

## Actuator Endpoints

Spring Boot Actuator provides production-ready endpoints for monitoring and management.

- **Health check:** [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)
- **Info:** [http://localhost:8080/actuator/info](http://localhost:8080/actuator/info)
- **All endpoints:** [http://localhost:8080/actuator](http://localhost:8080/actuator)

You can customize which endpoints are enabled or exposed in `application.properties`.

## Notes
- Uses Java 17 and Spring Boot 3.x
- Calls the public Nager.Date API (https://date.nager.at)
