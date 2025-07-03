package com.example.holiday.controller;

import com.example.holiday.service.HolidayService;
import com.example.holiday.model.Holiday;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/holidays")
public class HolidayController {
    private final HolidayService holidayService;

    public HolidayController(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    // 1. Given a country, return the last celebrated 3 holidays (date and name).
    @GetMapping("/last3/{countryCode}")
    public List<Holiday> getLast3Holidays(@PathVariable String countryCode) {
        return holidayService.getLast3Holidays(countryCode);
    }

    // 2. Given a year and country codes, for each country return a number of public holidays not falling on weekends (sort in descending order).
    @GetMapping("/count-not-weekend")
    public Map<String, Integer> getCountNotWeekend(@RequestParam int year, @RequestParam List<String> countryCodes) {
        return holidayService.getCountNotWeekend(year, countryCodes);
    }

    // 3. Given a year and 2 country codes, return the deduplicated list of dates celebrated in both countries (date + local names)
    @GetMapping("/common")
    public List<Holiday> getCommonHolidays(@RequestParam int year, @RequestParam String countryCode1, @RequestParam String countryCode2) {
        return holidayService.getCommonHolidays(year, countryCode1, countryCode2);
    }
}
