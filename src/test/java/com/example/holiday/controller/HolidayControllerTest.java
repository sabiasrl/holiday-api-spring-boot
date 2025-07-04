package com.example.holiday.controller;

import com.example.holiday.service.HolidayService;
import com.example.holiday.model.CommonHolidays;
import com.example.holiday.model.Holiday;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Collections;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HolidayController.class)
class HolidayControllerTest {
    @Autowired
    private MockMvc mockMvc;

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
    void testGetLast3Holidays() throws Exception {
        when(holidayService.getLast3Holidays("US")).thenReturn(Collections.singletonList(sampleHoliday));
        mockMvc.perform(get("/api/holidays/last3/US"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].localName").value("New Year's Day"));
    }

    @Test
    void testGetCountNotWeekend() throws Exception {
        when(holidayService.getCountNotWeekend(eq(2024), anyList())).thenReturn(Map.of("US", 10, "IT", 8));
        mockMvc.perform(get("/api/holidays/count-not-weekend?year=2024&countryCodes=US,IT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.US").value(10));
    }

    @Test
    void testGetCommonHolidays() throws Exception {
        CommonHolidays ch = new CommonHolidays("2024-01-01", java.util.Arrays.asList("New Year's Day", "Capodanno"));
        when(holidayService.getCommonHolidays(eq(2024), eq("US"), eq("IT"))).thenReturn(java.util.Arrays.asList(ch));
        mockMvc.perform(get("/api/holidays/common?year=2024&countryCode1=US&countryCode2=IT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].date").value("2024-01-01"))
                .andExpect(jsonPath("$[0].localNames").isArray())
                .andExpect(jsonPath("$[0].localNames[0]").value("New Year's Day"))
                .andExpect(jsonPath("$[0].localNames[1]").value("Capodanno"));
    }

    @Test
    void testGetLast3Holidays_extensive() throws Exception {
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
        when(holidayService.getLast3Holidays("US")).thenReturn(java.util.List.of(h1, h2, h3));
        mockMvc.perform(get("/api/holidays/last3/US"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].date").value("2024-01-01"))
                .andExpect(jsonPath("$[0].localName").value("New Year's Day"))
                .andExpect(jsonPath("$[1].date").value("2024-07-04"))
                .andExpect(jsonPath("$[1].localName").value("Independence Day"))
                .andExpect(jsonPath("$[2].date").value("2024-12-25"))
                .andExpect(jsonPath("$[2].localName").value("Christmas Day"));
    }

    @Test
    void testGetCountNotWeekend_extensive() throws Exception {
        when(holidayService.getCountNotWeekend(eq(2024), anyList())).thenReturn(Map.of("US", 10, "IT", 8, "DE", 12));
        mockMvc.perform(get("/api/holidays/count-not-weekend?year=2024&countryCodes=US,IT,DE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.US").value(10))
                .andExpect(jsonPath("$.IT").value(8))
                .andExpect(jsonPath("$.DE").value(12));
    }

    @Test
    void testGetCommonHolidays_extensive() throws Exception {
        CommonHolidays ch1 = new CommonHolidays("2024-01-01", java.util.Arrays.asList("New Year's Day", "Capodanno"));
        CommonHolidays ch2 = new CommonHolidays("2024-12-25", java.util.Arrays.asList("Christmas Day", "Natale"));
        when(holidayService.getCommonHolidays(eq(2024), eq("US"), eq("IT"))).thenReturn(java.util.Arrays.asList(ch1, ch2));
        mockMvc.perform(get("/api/holidays/common?year=2024&countryCode1=US&countryCode2=IT"))
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
