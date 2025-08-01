package com.example.holiday.vo;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonHolidayVO {
    private String date; // ISO date string (yyyy-MM-dd)
    private List<String> localNames; // Ordered list of local names
} 