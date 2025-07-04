package com.example.holiday.controller;

import com.example.holiday.service.HolidayService;
import com.example.holiday.model.CommonHolidays;
import com.example.holiday.model.Holiday;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.Collections;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@WebFluxTest(HolidayController.class)
class HolidayControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private HolidayService holidayService;

    private Holiday sampleHoliday;

    @BeforeEach
    void setUp() {
        sampleHoliday = new Holiday();
        sampleHoliday.setDate(java.time.LocalDate.of(2024, 1, 1));
        sampleHoliday.setLocalName("New Year's Day");
        sampleHoliday.setName("New Year's Day");
        sampleHoliday.setCountryCode("US");
    }

    @Test
    void testGetLast3Holidays() {
        when(holidayService.getLast3Holidays("US")).thenReturn(Mono.just(Collections.singletonList(sampleHoliday)));
        webTestClient.get().uri("/api/holidays/last3/US")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].localName").isEqualTo("New Year's Day");
    }

    @Test
    void testGetCountNotWeekend() {
        when(holidayService.getCountNotWeekend(eq(2024), anyList())).thenReturn(Mono.just(Map.of("US", 10, "IT", 8)));
        webTestClient.get().uri("/api/holidays/count-not-weekend?year=2024&countryCodes=US,IT")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.US").isEqualTo(10);
    }

    @Test
    void testGetCommonHolidays() {
        CommonHolidays ch = new CommonHolidays("2024-01-01", java.util.Arrays.asList("New Year's Day", "Capodanno"));
        when(holidayService.getCommonHolidays(eq(2024), eq("US"), eq("IT"))).thenReturn(Flux.just(ch));
        webTestClient.get().uri("/api/holidays/common?year=2024&countryCode1=US&countryCode2=IT")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].date").isEqualTo("2024-01-01")
                .jsonPath("$[0].localNames").isArray()
                .jsonPath("$[0].localNames[0]").isEqualTo("New Year's Day")
                .jsonPath("$[0].localNames[1]").isEqualTo("Capodanno");
    }

    @Test
    void testGetLast3Holidays_extensive() {
        Holiday h1 = new Holiday();
        h1.setDate(java.time.LocalDate.of(2024, 1, 1));
        h1.setLocalName("New Year's Day");
        h1.setName("New Year's Day");
        h1.setCountryCode("US");
        Holiday h2 = new Holiday();
        h2.setDate(java.time.LocalDate.of(2024, 7, 4));
        h2.setLocalName("Independence Day");
        h2.setName("Independence Day");
        h2.setCountryCode("US");
        Holiday h3 = new Holiday();
        h3.setDate(java.time.LocalDate.of(2024, 12, 25));
        h3.setLocalName("Christmas Day");
        h3.setName("Christmas Day");
        h3.setCountryCode("US");
        when(holidayService.getLast3Holidays("US")).thenReturn(Mono.just(java.util.List.of(h1, h2, h3)));
        webTestClient.get().uri("/api/holidays/last3/US")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].date").isEqualTo("2024-01-01")
                .jsonPath("$[0].localName").isEqualTo("New Year's Day")
                .jsonPath("$[1].date").isEqualTo("2024-07-04")
                .jsonPath("$[1].localName").isEqualTo("Independence Day")
                .jsonPath("$[2].date").isEqualTo("2024-12-25")
                .jsonPath("$[2].localName").isEqualTo("Christmas Day");
    }

    @Test
    void testGetCountNotWeekend_extensive() {
        when(holidayService.getCountNotWeekend(eq(2024), anyList())).thenReturn(Mono.just(Map.of("US", 10, "IT", 8, "DE", 12)));
        webTestClient.get().uri("/api/holidays/count-not-weekend?year=2024&countryCodes=US,IT,DE")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.US").isEqualTo(10)
                .jsonPath("$.IT").isEqualTo(8)
                .jsonPath("$.DE").isEqualTo(12);
    }

    @Test
    void testGetCommonHolidays_extensive() {
        CommonHolidays ch1 = new CommonHolidays("2024-01-01", java.util.Arrays.asList("New Year's Day", "Capodanno"));
        CommonHolidays ch2 = new CommonHolidays("2024-12-25", java.util.Arrays.asList("Christmas Day", "Natale"));
        when(holidayService.getCommonHolidays(eq(2024), eq("US"), eq("IT"))).thenReturn(Flux.just(ch1, ch2));
        webTestClient.get().uri("/api/holidays/common?year=2024&countryCode1=US&countryCode2=IT")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].date").isEqualTo("2024-01-01")
                .jsonPath("$[0].localNames").isArray()
                .jsonPath("$[0].localNames[0]").isEqualTo("New Year's Day")
                .jsonPath("$[0].localNames[1]").isEqualTo("Capodanno")
                .jsonPath("$[1].date").isEqualTo("2024-12-25")
                .jsonPath("$[1].localNames[0]").isEqualTo("Christmas Day")
                .jsonPath("$[1].localNames[1]").isEqualTo("Natale");
    }
}
