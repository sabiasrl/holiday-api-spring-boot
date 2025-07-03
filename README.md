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

This project uses JUnit 5 for unit and integration tests. To generate a code coverage report, JaCoCo is recommended.

### Run Tests

- **Run all tests:**
  ```sh
  mvn test
  ```

### Generate Coverage Report (after JaCoCo is configured)

- **Add JaCoCo to your `pom.xml`** (if not already present):
  ```xml
  <plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.11</version>
    <executions>
      <execution>
        <goals>
          <goal>prepare-agent</goal>
        </goals>
      </execution>
      <execution>
        <id>report</id>
        <phase>test</phase>
        <goals>
          <goal>report</goal>
        </goals>
      </execution>
    </executions>
  </plugin>
  ```
- **Generate the report:**
  ```sh
  mvn test
  # Open target/site/jacoco/index.html in your browser for the report
  ```

## Notes
- Uses Java 17 and Spring Boot 3.x
- Calls the public Nager.Date API (https://date.nager.at)
