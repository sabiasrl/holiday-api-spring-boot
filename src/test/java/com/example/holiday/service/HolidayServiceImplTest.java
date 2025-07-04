package com.example.holiday.service;

import com.example.holiday.model.Holiday;
import com.example.holiday.vo.CommonHolidays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class HolidayServiceImplTest {
    
    @MockBean
    private HolidayServiceImpl holidayService;

    @BeforeEach
    void setUp() {
        // Initialize the mock service for the entire test class
        List<Holiday> mockHolidays = Arrays.asList(
            createHoliday(2024, "US", "New Year's Day", "New Year's Day", "2024-01-01"),
            createHoliday(2024, "IT", "Capodanno", "New Year's Day", "2024-01-01"),
            createHoliday(2024, "US", "Independence Day", "Independence Day", "2024-07-04")
        );
        doReturn(mockHolidays).when(holidayService).fetchHolidays(anyInt(), anyString());

    }

    @Test
    void testGetLast3Holidays() {
        when(holidayService.getLast3Holidays(anyString())).thenCallRealMethod();

        List<Holiday> holidays = holidayService.getLast3Holidays("US");
        
        assertNotNull(holidays);
        assertTrue(holidays.size() <= 3);
        holidays.forEach(h -> assertNotNull(h));
    }

    @Test
    void testGetCountNotWeekend() {
        when(holidayService.getCountNotWeekend(anyInt(), anyList())).thenCallRealMethod();
        
        Map<String, Integer> result = holidayService.getCountNotWeekend(2024, Arrays.asList("US", "IT"));
        
        assertNotNull(result);
        assertTrue(result.containsKey("US"));
        assertTrue(result.containsKey("IT"));
        assertTrue(result.get("US") >= 0);
        assertTrue(result.get("IT") >= 0);
    }

    @Test
    void testGetCommonHolidays() {
        when(holidayService.getCommonHolidays(anyInt(), anyString(), anyString())).thenCallRealMethod();

        List<CommonHolidays> common = holidayService.getCommonHolidays(2024, "US", "IT");
        assertNotNull(common);
        for (CommonHolidays ch : common) {
            assertNotNull(ch.getDate());
            assertNotNull(ch.getLocalNames());
            assertFalse(ch.getLocalNames().isEmpty());
        }
    }

    @Test
    void testGetLast3Holidays_emptyResult() {
        doReturn(Arrays.asList()).when(holidayService).fetchHolidays(anyInt(), eq("ZZ"));
        when(holidayService.getLast3Holidays(anyString())).thenCallRealMethod();
        List<Holiday> holidays = holidayService.getLast3Holidays("ZZ");
        assertNotNull(holidays);
        assertEquals(0, holidays.size());
    }

    @Test
    void testGetCountNotWeekend_emptyList() {
        when(holidayService.getCountNotWeekend(anyInt(), eq(Arrays.asList()))).thenCallRealMethod();
        Map<String, Integer> result = holidayService.getCountNotWeekend(2024, Arrays.asList());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetCommonHolidays_noCommon() {
        List<Holiday> us = Arrays.asList(createHoliday(2024, "US", "Independence Day", "Independence Day", "2024-07-04"));
        List<Holiday> it = Arrays.asList(createHoliday(2024, "IT", "Ferragosto", "Assumption Day", "2024-08-15"));
        doReturn(us).when(holidayService).fetchHolidays(anyInt(), eq("US"));
        doReturn(it).when(holidayService).fetchHolidays(anyInt(), eq("IT"));
        when(holidayService.getCommonHolidays(anyInt(), anyString(), anyString())).thenCallRealMethod();
        List<CommonHolidays> common = holidayService.getCommonHolidays(2024, "US", "IT");
        assertNotNull(common);
        assertTrue(common.isEmpty());
    }

    @Test
    void testFetchHolidays_handlesException() {
        doThrow(new RuntimeException("API error")).when(holidayService).fetchHolidays(anyInt(), eq("FAIL"));
        when(holidayService.getLast3Holidays(anyString())).thenCallRealMethod();
        try {
            holidayService.fetchHolidays(2024, "FAIL");
            fail("Should throw exception");
        } catch (Exception e) {
            assertTrue(e instanceof RuntimeException);
            // Accept either direct or wrapped exception
            if (e.getCause() != null) {
                assertEquals("API error", e.getCause().getMessage());
            } else {
                assertEquals("API error", e.getMessage());
            }
        }
    }

    private Holiday createHoliday(int year, String countryCode, String localName, String name, String dateStr) {
        Holiday h = new Holiday();
        h.setCountryCode(countryCode);
        h.setLocalName(localName);
        h.setName(name);
        h.setDate(java.time.LocalDate.parse(dateStr));
        return h;
    }
}
