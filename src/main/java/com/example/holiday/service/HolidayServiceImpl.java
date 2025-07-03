package com.example.holiday.service;

import com.example.holiday.model.Holiday;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HolidayServiceImpl implements HolidayService {
    private static final String NAGER_API_URL = "https://date.nager.at/api/v3/PublicHolidays/{year}/{countryCode}";
    private final RestTemplate restTemplate;

    @Autowired
    public HolidayServiceImpl() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public List<Holiday> getLast3Holidays(String countryCode) {
        int year = LocalDate.now().getYear();
        List<Holiday> holidays = fetchHolidays(year, countryCode);
        if (holidays.isEmpty()) {
            holidays = fetchHolidays(year - 1, countryCode);
        }
        holidays.sort(Comparator.comparing(Holiday::getDate).reversed());
        return holidays.stream().limit(3).collect(Collectors.toList());
    }

    @Override
    public Map<String, Integer> getCountNotWeekend(int year, List<String> countryCodes) {
        Map<String, Integer> result = new HashMap<>();
        for (String code : countryCodes) {
            List<Holiday> holidays = fetchHolidays(year, code);
            int count = (int) holidays.stream()
                .filter(h -> !isWeekend(h.getDate()))
                .count();
            result.put(code, count);
        }
        return result.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
    }

    @Override
    public List<Holiday> getCommonHolidays(int year, String countryCode1, String countryCode2) {
        List<Holiday> holidays1 = fetchHolidays(year, countryCode1);
        List<Holiday> holidays2 = fetchHolidays(year, countryCode2);
        Set<LocalDate> dates1 = holidays1.stream().map(Holiday::getDate).collect(Collectors.toSet());
        Map<LocalDate, Holiday> map2 = holidays2.stream().collect(Collectors.toMap(Holiday::getDate, h -> h, (a, b) -> a));
        List<Holiday> common = new ArrayList<>();
        for (Holiday h1 : holidays1) {
            if (map2.containsKey(h1.getDate())) {
                Holiday h2 = map2.get(h1.getDate());
                Holiday combined = new Holiday();
                combined.setDate(h1.getDate());
                combined.setLocalName(h1.getLocalName() + " / " + h2.getLocalName());
                combined.setName(h1.getName() + " / " + h2.getName());
                combined.setCountryCode(h1.getCountryCode() + "," + h2.getCountryCode());
                common.add(combined);
            }
        }
        return common;
    }

    private List<Holiday> fetchHolidays(int year, String countryCode) {
        try {
            Holiday[] holidays = restTemplate.getForObject(NAGER_API_URL, Holiday[].class, year, countryCode);
            return holidays != null ? Arrays.asList(holidays) : Collections.emptyList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private boolean isWeekend(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }
}
