package com.example.holiday.controller;

import com.example.holiday.service.HolidayService;
import com.example.holiday.model.Holiday;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Arrays;
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
        when(holidayService.getCommonHolidays(eq(2024), eq("US"), eq("IT"))).thenReturn(Arrays.asList(sampleHoliday));
        mockMvc.perform(get("/api/holidays/common?year=2024&countryCode1=US&countryCode2=IT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].countryCode").value("US"));
    }
}
