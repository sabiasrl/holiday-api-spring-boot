package com.example.holiday.model;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class Holiday {
    private LocalDate date;
    private String localName;
    private String name;
    private String countryCode;
    private boolean fixed;
    private boolean global;
    private List<String> counties;
    private int launchYear;
    private List<String> types;
}
