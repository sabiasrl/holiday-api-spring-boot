package com.example.holiday.service;

import com.example.holiday.model.CommonHolidays;
import com.example.holiday.model.Holiday;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HolidayServiceImpl implements HolidayService {
    @Value("${NAGER_API_URL}")
    private String nagerApiBaseUrl;

    private final WebClient webClient;

    private String publicHolidayUrl;

    @Autowired
    public HolidayServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }
    
    @PostConstruct
    void init (){
        this.publicHolidayUrl = nagerApiBaseUrl + "/PublicHolidays/{year}/{countryCode}";
    }

    @Override
    public Mono<List<Holiday>> getLast3Holidays(String countryCode) {
        int year = LocalDate.now().getYear();
        return fetchHolidays(year, countryCode)
            .flatMap(holidays -> {
                if (holidays.isEmpty()) {
                    return fetchHolidays(year - 1, countryCode);
                }
                return Mono.just(holidays);
            })
            .map(holidays -> holidays.stream()
                .sorted(Comparator.comparing(Holiday::getDate).reversed())
                .limit(3)
                .collect(Collectors.toList()));
    }

    @Override
    public Mono<Map<String, Integer>> getCountNotWeekend(int year, List<String> countryCodes) {
        return Flux.fromIterable(countryCodes)
            .flatMap(code -> fetchHolidays(year, code)
                .map(holidays -> Map.entry(code, (int) holidays.stream()
                    .filter(h -> !isWeekend(h.getDate()))
                    .count())))
            .collectList()
            .map(entries -> entries.stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new)));
    }

    @Override
    public Flux<CommonHolidays> getCommonHolidays(int year, String countryCode1, String countryCode2) {
        Mono<List<Holiday>> holidays1 = fetchHolidays(year, countryCode1);
        Mono<List<Holiday>> holidays2 = fetchHolidays(year, countryCode2);
        return Mono.zip(holidays1, holidays2)
            .flatMapMany(tuple -> {
                Map<LocalDate, List<Holiday>> map1 = tuple.getT1().stream().collect(Collectors.groupingBy(Holiday::getDate));
                Map<LocalDate, List<Holiday>> map2 = tuple.getT2().stream().collect(Collectors.groupingBy(Holiday::getDate));
                Set<LocalDate> commonDates = new HashSet<>(map1.keySet());
                commonDates.retainAll(map2.keySet());
                return Flux.fromIterable(commonDates)
                    .sort()
                    .map(date -> {
                        List<String> localNames = new ArrayList<>();
                        map1.get(date).forEach(h -> localNames.add(h.getLocalName()));
                        map2.get(date).forEach(h -> localNames.add(h.getLocalName()));
                        List<String> dedupedLocalNames = localNames.stream().distinct().collect(Collectors.toList());
                        return new CommonHolidays(date.toString(), dedupedLocalNames);
                    });
            });
    }

    Mono<List<Holiday>> fetchHolidays(int year, String countryCode) {
        String url = publicHolidayUrl
            .replace("{year}", String.valueOf(year))
            .replace("{countryCode}", countryCode);
        return webClient.get()
            .uri(url)
            .retrieve()
            .bodyToFlux(Holiday.class)
            .collectList()
            .onErrorResume(e -> {
                log.error("Error fetching holidays for year: {}, country: {}", year, countryCode, e);
                return Mono.just(Collections.emptyList());
            });
    }

    private boolean isWeekend(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }
}
