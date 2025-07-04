package com.example.holiday.service;

import com.example.holiday.model.Holiday;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.mockito.Mockito;
import org.mockito.Spy;
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

        List<Holiday> common = holidayService.getCommonHolidays(2024, "US", "IT");
        assertNotNull(common);
        for (Holiday h : common) {
            assertNotNull(h.getDate());
            assertNotNull(h.getLocalName());
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
