package com.example.holiday.service;

import com.example.holiday.model.Holiday;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HolidayServiceImplTest {
    private HolidayServiceImpl holidayService;

    @BeforeEach
    void setUp() {
        holidayService = new HolidayServiceImpl();
    }

    @Test
    void testGetLast3Holidays() {
        List<Holiday> holidays = holidayService.getLast3Holidays("US");
        assertNotNull(holidays);
        assertTrue(holidays.size() <= 3);
        holidays.forEach(h -> assertNotNull(h.getDate()));
    }

    @Test
    void testGetCountNotWeekend() {
        Map<String, Integer> result = holidayService.getCountNotWeekend(2024, Arrays.asList("US", "IT"));
        assertNotNull(result);
        assertTrue(result.containsKey("US"));
        assertTrue(result.containsKey("IT"));
        assertTrue(result.get("US") >= 0);
        assertTrue(result.get("IT") >= 0);
    }

    @Test
    void testGetCommonHolidays() {
        List<Holiday> common = holidayService.getCommonHolidays(2024, "US", "IT");
        assertNotNull(common);
        for (Holiday h : common) {
            assertNotNull(h.getDate());
            assertNotNull(h.getLocalName());
        }
    }
}
