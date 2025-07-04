package com.example.holiday.controller;

import com.example.holiday.service.HolidayService;
import com.example.holiday.model.CommonHolidays;
import com.example.holiday.model.Holiday;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@Tag(name = "Holiday API", description = "Endpoints for holiday queries")
@RestController
@RequestMapping("/api/holidays")
public class HolidayController {
    private final HolidayService holidayService;

    public HolidayController(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    // 1. Given a country, return the last celebrated 3 holidays (date and name).
    @Operation(summary = "Get last 3 holidays for a country")
    @GetMapping("/last3/{countryCode}")
    public List<Holiday> getLast3Holidays(
        @Parameter(description = "Country code", required = true)
        @PathVariable String countryCode) {
        return holidayService.getLast3Holidays(countryCode);
    }

    // 2. Given a year and country codes, for each country return a number of public holidays not falling on weekends (sort in descending order).
    @Operation(summary = "Get count of holidays not on weekends for countries")
    @GetMapping("/count-not-weekend")
    public Map<String, Integer> getCountNotWeekend(
        @Parameter(description = "Year", required = true)
        @RequestParam int year,
        @Parameter(description = "Comma-separated country codes", required = true)
        @RequestParam List<String> countryCodes) {
        return holidayService.getCountNotWeekend(year, countryCodes);
    }

    // 3. Given a year and 2 country codes, return the deduplicated list of dates celebrated in both countries (date + local names)
    @Operation(summary = "Get common holidays for two countries")
    @GetMapping("/common")
    public List<CommonHolidays> getCommonHolidays(
        @Parameter(description = "Year", required = true)
        @RequestParam int year,
        @Parameter(description = "First country code", required = true)
        @RequestParam String countryCode1,
        @Parameter(description = "Second country code", required = true)
        @RequestParam String countryCode2) {
        return holidayService.getCommonHolidays(year, countryCode1, countryCode2);
    }
}
