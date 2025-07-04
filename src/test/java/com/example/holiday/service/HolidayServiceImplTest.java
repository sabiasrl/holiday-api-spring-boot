package com.example.holiday.service;

import com.example.holiday.model.CommonHolidays;
import com.example.holiday.model.Holiday;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class HolidayServiceImplTest {

    @MockBean
    private HolidayServiceImpl holidayServiceMock;

    @BeforeEach
    void setUp() {
        List<Holiday> mockHolidays = Arrays.asList(
            createHoliday(2024, "US", "New Year's Day", "New Year's Day", "2024-01-01"),
            createHoliday(2024, "IT", "Capodanno", "New Year's Day", "2024-01-01"),
            createHoliday(2024, "US", "Independence Day", "Independence Day", "2024-07-04")
        );
        when(holidayServiceMock.fetchHolidays(anyInt(), anyString())).thenReturn(Mono.just(mockHolidays));
    }

    @Test
    void testGetLast3Holidays() {
        lenient().when(holidayServiceMock.getLast3Holidays(anyString())).thenCallRealMethod();
        lenient().when(holidayServiceMock.fetchHolidays(anyInt(), anyString())).thenReturn(Mono.just(Arrays.asList(
            createHoliday(2024, "US", "New Year's Day", "New Year's Day", "2024-01-01"),
            createHoliday(2024, "US", "Independence Day", "Independence Day", "2024-07-04"),
            createHoliday(2024, "US", "Christmas Day", "Christmas Day", "2024-12-25")
        )));
        StepVerifier.create(holidayServiceMock.getLast3Holidays("US"))
            .expectNextMatches(list -> list != null && list.size() == 3)
            .verifyComplete();
    }

    @Test
    void testGetCountNotWeekend() {
        lenient().when(holidayServiceMock.getCountNotWeekend(anyInt(), anyList())).thenCallRealMethod();
        lenient().when(holidayServiceMock.fetchHolidays(anyInt(), anyString())).thenReturn(Mono.just(Arrays.asList(
            createHoliday(2024, "US", "New Year's Day", "New Year's Day", "2024-01-01"),
            createHoliday(2024, "IT", "Capodanno", "New Year's Day", "2024-01-01"),
            createHoliday(2024, "US", "Independence Day", "Independence Day", "2024-07-04")
        )));
        StepVerifier.create(holidayServiceMock.getCountNotWeekend(2024, Arrays.asList("US", "IT")))
            .expectNextMatches(map -> map.containsKey("US") && map.containsKey("IT"))
            .verifyComplete();
    }

    @Test
    void testGetCommonHolidays() {
        lenient().when(holidayServiceMock.getCommonHolidays(anyInt(), anyString(), anyString())).thenCallRealMethod();
        lenient().when(holidayServiceMock.fetchHolidays(anyInt(), eq("US"))).thenReturn(Mono.just(Arrays.asList(
            createHoliday(2024, "US", "New Year's Day", "New Year's Day", "2024-01-01"),
            createHoliday(2024, "US", "Christmas Day", "Christmas Day", "2024-12-25")
        )));
        lenient().when(holidayServiceMock.fetchHolidays(anyInt(), eq("IT"))).thenReturn(Mono.just(Arrays.asList(
            createHoliday(2024, "IT", "Capodanno", "New Year's Day", "2024-01-01"),
            createHoliday(2024, "IT", "Natale", "Christmas Day", "2024-12-25")
        )));
        StepVerifier.create(holidayServiceMock.getCommonHolidays(2024, "US", "IT"))
            .expectNextCount(2)
            .verifyComplete();
    }

    @Test
    void testGetLast3Holidays_emptyResult() {
        lenient().when(holidayServiceMock.getLast3Holidays(anyString())).thenCallRealMethod();
        lenient().when(holidayServiceMock.fetchHolidays(anyInt(), eq("ZZ"))).thenReturn(Mono.just(Arrays.asList()));
        StepVerifier.create(holidayServiceMock.getLast3Holidays("ZZ"))
            .expectNextMatches(List::isEmpty)
            .verifyComplete();
    }

    @Test
    void testGetCountNotWeekend_emptyList() {
        lenient().when(holidayServiceMock.getCountNotWeekend(anyInt(), eq(Arrays.asList()))).thenCallRealMethod();
        StepVerifier.create(holidayServiceMock.getCountNotWeekend(2024, Arrays.asList()))
            .expectNextMatches(Map::isEmpty)
            .verifyComplete();
    }

    @Test
    void testGetCommonHolidays_noCommon() {
        lenient().when(holidayServiceMock.getCommonHolidays(anyInt(), anyString(), anyString())).thenCallRealMethod();
        lenient().when(holidayServiceMock.fetchHolidays(anyInt(), eq("US"))).thenReturn(Mono.just(Arrays.asList(
            createHoliday(2024, "US", "Independence Day", "Independence Day", "2024-07-04")
        )));
        lenient().when(holidayServiceMock.fetchHolidays(anyInt(), eq("IT"))).thenReturn(Mono.just(Arrays.asList(
            createHoliday(2024, "IT", "Ferragosto", "Assumption Day", "2024-08-15")
        )));
        StepVerifier.create(holidayServiceMock.getCommonHolidays(2024, "US", "IT"))
            .expectNextCount(0)
            .verifyComplete();
    }

    @Test
    void testFetchHolidays_handlesException() {
        lenient().when(holidayServiceMock.fetchHolidays(anyInt(), eq("FAIL"))).thenThrow(new RuntimeException("API error"));
        try {
            holidayServiceMock.fetchHolidays(2024, "FAIL");
            fail("Should throw exception");
        } catch (Exception e) {
            assertTrue(e instanceof RuntimeException);
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
