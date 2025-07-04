package com.example.holiday.service;

import com.example.holiday.model.Holiday;
import com.example.holiday.vo.CommonHolidays;

import java.util.List;
import java.util.Map;

public interface HolidayService {
    List<Holiday> getLast3Holidays(String countryCode);
    Map<String, Integer> getCountNotWeekend(int year, List<String> countryCodes);
    List<CommonHolidays> getCommonHolidays(int year, String countryCode1, String countryCode2);
}
