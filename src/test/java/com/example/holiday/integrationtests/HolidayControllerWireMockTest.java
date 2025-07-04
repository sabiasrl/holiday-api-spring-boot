package com.example.holiday.controller;

import com.example.holiday.mock.WireMockConfig;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

@SpringBootTest
@AutoConfigureMockMvc
@Import(WireMockConfig.class)
class HolidayControllerWireMockTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WireMockServer wireMockServer;

    @Value("${NAGER_API_URL}")
    private String nagerApiUrl;

    @Value("${WIREMOCK_PORT:9561}")
    private int wiremockPort;

    @BeforeEach
    void setup() {
        wireMockServer.resetAll();

        int year = LocalDate.now().getYear();
        wireMockServer.stubFor(com.github.tomakehurst.wiremock.client.WireMock
                .get(urlEqualTo("/PublicHolidays/" + year + "/US"))
                .willReturn(aResponse()
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
                .get(urlEqualTo("/PublicHolidays/" + previousYear + "/US"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                                "[{\"date\":\"2024-01-01\",\"localName\":\"New Year's Day\",\"name\":\"New Year's Day\",\"countryCode\":\"US\"},"
                                        +
                                        "{\"date\":\"2024-07-04\",\"localName\":\"Independence Day\",\"name\":\"Independence Day\",\"countryCode\":\"US\"},"
                                        +
                                        "{\"date\":\"2024-12-25\",\"localName\":\"Christmas Day\",\"name\":\"Christmas Day\",\"countryCode\":\"US\"}]")));

        wireMockServer.stubFor(com.github.tomakehurst.wiremock.client.WireMock
                .get(urlEqualTo("/PublicHolidays/" + year + "/IT"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                                "[{\"date\":\"2025-01-01\",\"localName\":\"Capodanno\",\"name\":\"New Year's Day\",\"countryCode\":\"IT\"},"
                                        +
                                        "{\"date\":\"2025-04-25\",\"localName\":\"Festa della Liberazione\",\"name\":\"Liberation Day\",\"countryCode\":\"IT\"},"
                                        +
                                        "{\"date\":\"2025-12-25\",\"localName\":\"Natale\",\"name\":\"Christmas Day\",\"countryCode\":\"IT\"}]")));

        wireMockServer.stubFor(com.github.tomakehurst.wiremock.client.WireMock
                .get(urlEqualTo("/PublicHolidays/" + previousYear + "/IT"))
                .willReturn(aResponse()
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
    void testExternalApiMock() throws Exception {

        mockMvc.perform(get("/api/holidays/last3/US"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetLast3Holidays_extensive() throws Exception {
        mockMvc.perform(get("/api/holidays/last3/US"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].date").value("2025-12-25"))
                .andExpect(jsonPath("$[0].localName").value("Christmas Day"))
                .andExpect(jsonPath("$[1].date").value("2025-07-04"))
                .andExpect(jsonPath("$[1].localName").value("Independence Day"))
                .andExpect(jsonPath("$[2].date").value("2025-01-01"))
                .andExpect(jsonPath("$[2].localName").value("New Year's Day"));
    }

    @Test
    void testGetCountNotWeekend_extensive() throws Exception {
        int year = 2025;

        mockMvc.perform(get("/api/holidays/count-not-weekend?year=" + year + "&countryCodes=US,IT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.US").value(3))
                .andExpect(jsonPath("$.IT").value(3));
    }

    @Test
    void testGetCommonHolidays_extensive() throws Exception {
        int previousYear = 2024;

        mockMvc.perform(get("/api/holidays/common?year=" + previousYear + "&countryCode1=US&countryCode2=IT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].date").value("2024-01-01"))
                .andExpect(jsonPath("$[0].localNames").isArray())
                .andExpect(jsonPath("$[0].localNames[0]").value("New Year's Day"))
                .andExpect(jsonPath("$[0].localNames[1]").value("Capodanno"))
                .andExpect(jsonPath("$[1].date").value("2024-12-25"))
                .andExpect(jsonPath("$[1].localNames[0]").value("Christmas Day"))
                .andExpect(jsonPath("$[1].localNames[1]").value("Natale"));
    }

}
