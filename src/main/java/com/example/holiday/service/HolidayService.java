package com.example.holiday.service;

import com.example.holiday.model.CommonHolidays;
import com.example.holiday.model.Holiday;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface HolidayService {
    Mono<List<Holiday>> getLast3Holidays(String countryCode);
    Mono<Map<String, Integer>> getCountNotWeekend(int year, List<String> countryCodes);
    Flux<CommonHolidays> getCommonHolidays(int year, String countryCode1, String countryCode2);
}
