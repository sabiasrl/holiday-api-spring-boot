package com.example.holiday.service;

import com.example.holiday.model.Holiday;
import com.example.holiday.vo.CommonHolidays;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HolidayServiceImpl implements HolidayService {
    @Value("${NAGER_API_URL}")
    private String nagerApiBaseUrl;
    private String publicHolidayUrl;

    private final RestTemplate restTemplate;

    @Autowired
    public HolidayServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    @PostConstruct
    void init (){
        this.publicHolidayUrl = nagerApiBaseUrl + "/PublicHolidays/{year}/{countryCode}";
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
    public List<CommonHolidays> getCommonHolidays(int year, String countryCode1, String countryCode2) {
        List<Holiday> holidays1 = fetchHolidays(year, countryCode1);
        List<Holiday> holidays2 = fetchHolidays(year, countryCode2);
        Map<LocalDate, List<Holiday>> map1 = holidays1.stream().collect(Collectors.groupingBy(Holiday::getDate));
        Map<LocalDate, List<Holiday>> map2 = holidays2.stream().collect(Collectors.groupingBy(Holiday::getDate));
        Set<LocalDate> commonDates = new HashSet<>(map1.keySet());
        commonDates.retainAll(map2.keySet());
        List<CommonHolidays> result = new ArrayList<>();
        for (LocalDate date : commonDates) {
            List<String> localNames = new ArrayList<>();
            map1.get(date).forEach(h -> localNames.add(h.getLocalName()));
            map2.get(date).forEach(h -> localNames.add(h.getLocalName()));
            // Remove duplicates but preserve order
            List<String> dedupedLocalNames = localNames.stream().distinct().collect(Collectors.toList());
            result.add(new CommonHolidays(date.toString(), dedupedLocalNames));
        }
        result.sort(Comparator.comparing(CommonHolidays::getDate));
        return result;
    }

    List<Holiday> fetchHolidays(int year, String countryCode) {
        try {
            Holiday[] holidays = restTemplate.getForObject(publicHolidayUrl, Holiday[].class, year, countryCode);
            return holidays != null ? Arrays.asList(holidays) : Collections.emptyList();
        } catch (Exception e) {
            log.error("Error fetching holidays for year: {}, country: {}", year, countryCode, e);
            throw new RuntimeException("Failed to fetch holidays for year: " + year + ", country: " + countryCode, e);
        }
    }

    private boolean isWeekend(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }
}
