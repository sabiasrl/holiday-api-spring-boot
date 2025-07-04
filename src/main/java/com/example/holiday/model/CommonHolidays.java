package com.example.holiday.model;

import lombok.Data;
import java.util.List;

@Data
public class CommonHolidays {
    private String date; // ISO date string (yyyy-MM-dd)
    private List<String> localNames; // Ordered list of local names

    public CommonHolidays() {}

    public CommonHolidays(String date, List<String> localNames) {
        this.date = date;
        this.localNames = localNames;
    }
}
