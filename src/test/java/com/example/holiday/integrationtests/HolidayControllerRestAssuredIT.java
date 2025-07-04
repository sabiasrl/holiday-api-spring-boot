package com.example.holiday.integrationtests;

import com.example.holiday.mock.WireMockConfig;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(WireMockConfig.class)
class HolidayControllerRestAssuredIT {

    @LocalServerPort
    private int port;

    @Autowired
    private WireMockServer wireMockServer;

    @Value("${NAGER_API_URL}")
    private String nagerApiUrl;

    @Value("${WIREMOCK_PORT:9561}")
    private int wiremockPort;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        wireMockServer.resetAll();

        int year = LocalDate.now().getYear();
        wireMockServer.stubFor(com.github.tomakehurst.wiremock.client.WireMock
                .get(com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo("/PublicHolidays/" + year + "/US"))
                .willReturn(com.github.tomakehurst.wiremock.client.WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                                "[{\"date\":\"2025-01-01\",\"localName\":\"New Year's Day\",\"name\":\"New Year's Day\",\"countryCode\":\"US\"},"
                                        +
                                        "{\"date\":\"2025-07-04\",\"localName\":\"Independence Day\",\"name\":\"Independence Day\",\"countryCode\":\"US\"},"
                                        +
                                        "{\"date\":\"2025-12-25\",\"localName\":\"Christmas Day\",\"name\":\"Christmas Day\",\"countryCode\":\"US\"}]")));
        int previousYear = year - 1;
        wireMockServer.stubFor(com.github.tomakehurst.wiremock.client.WireMock
                .get(com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo("/PublicHolidays/" + previousYear + "/US"))
                .willReturn(com.github.tomakehurst.wiremock.client.WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                                "[{\"date\":\"2024-01-01\",\"localName\":\"New Year's Day\",\"name\":\"New Year's Day\",\"countryCode\":\"US\"},"
                                        +
                                        "{\"date\":\"2024-07-04\",\"localName\":\"Independence Day\",\"name\":\"Independence Day\",\"countryCode\":\"US\"},"
                                        +
                                        "{\"date\":\"2024-12-25\",\"localName\":\"Christmas Day\",\"name\":\"Christmas Day\",\"countryCode\":\"US\"}]")));
        wireMockServer.stubFor(com.github.tomakehurst.wiremock.client.WireMock
                .get(com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo("/PublicHolidays/" + year + "/IT"))
                .willReturn(com.github.tomakehurst.wiremock.client.WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                                "[{\"date\":\"2025-01-01\",\"localName\":\"Capodanno\",\"name\":\"New Year's Day\",\"countryCode\":\"IT\"},"
                                        +
                                        "{\"date\":\"2025-04-25\",\"localName\":\"Festa della Liberazione\",\"name\":\"Liberation Day\",\"countryCode\":\"IT\"},"
                                        +
                                        "{\"date\":\"2025-12-25\",\"localName\":\"Natale\",\"name\":\"Christmas Day\",\"countryCode\":\"IT\"}]")));
        wireMockServer.stubFor(com.github.tomakehurst.wiremock.client.WireMock
                .get(com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo("/PublicHolidays/" + previousYear + "/IT"))
                .willReturn(com.github.tomakehurst.wiremock.client.WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                                "[{\"date\":\"2024-01-01\",\"localName\":\"Capodanno\",\"name\":\"New Year's Day\",\"countryCode\":\"IT\"},"
                                        +
                                        "{\"date\":\"2024-04-25\",\"localName\":\"Festa della Liberazione\",\"name\":\"Liberation Day\",\"countryCode\":\"IT\"},"
                                        +
                                        "{\"date\":\"2024-12-25\",\"localName\":\"Natale\",\"name\":\"Christmas Day\",\"countryCode\":\"IT\"}]")));
    }

    @AfterEach
    void tearDown() {
        wireMockServer.resetAll();
    }

    @Test
    void testNagerApiUrlIsWiremock() {
        String expectedUrl = "http://localhost:" + wiremockPort;
        org.junit.jupiter.api.Assertions.assertEquals(expectedUrl, nagerApiUrl,
                "NAGER_API_URL should point to the WireMock server");
    }

    @Test
    void testGetLast3Holidays_extensive() {
        given()
        .when()
            .get("/api/holidays/last3/US")
        .then()
            .statusCode(200)
            .body("[0].date", equalTo("2025-12-25"))
            .body("[0].localName", equalTo("Christmas Day"))
            .body("[1].date", equalTo("2025-07-04"))
            .body("[1].localName", equalTo("Independence Day"))
            .body("[2].date", equalTo("2025-01-01"))
            .body("[2].localName", equalTo("New Year's Day"));
    }

    @Test
    void testGetCountNotWeekend_extensive() {
        int year = 2025;
        given()
            .queryParam("year", year)
            .queryParam("countryCodes", "US,IT")
        .when()
            .get("/api/holidays/count-not-weekend")
        .then()
            .statusCode(200)
            .body("US", equalTo(3))
            .body("IT", equalTo(3));
    }

    @Test
    void testGetCommonHolidays_extensive() {
        int previousYear = 2024;
        given()
            .queryParam("year", previousYear)
            .queryParam("countryCode1", "US")
            .queryParam("countryCode2", "IT")
        .when()
            .get("/api/holidays/common")
        .then()
            .statusCode(200)
            .body("[0].date", equalTo("2024-01-01"))
            .body("[0].localNames[0]", equalTo("New Year's Day"))
            .body("[0].localNames[1]", equalTo("Capodanno"))
            .body("[1].date", equalTo("2024-12-25"))
            .body("[1].localNames[0]", equalTo("Christmas Day"))
            .body("[1].localNames[1]", equalTo("Natale"));
    }
}
