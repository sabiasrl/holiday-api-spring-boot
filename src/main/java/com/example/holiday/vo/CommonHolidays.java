package com.example.holiday.vo;

import java.util.List;
import java.util.Objects;

public class CommonHolidays {
    private String date; // ISO date string (yyyy-MM-dd)
    private List<String> localNames; // Ordered list of local names

    public CommonHolidays() {}

    public CommonHolidays(String date, List<String> localNames) {
        this.date = date;
        this.localNames = localNames;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getLocalNames() {
        return localNames;
    }

    public void setLocalNames(List<String> localNames) {
        this.localNames = localNames;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommonHolidays that = (CommonHolidays) o;
        return Objects.equals(date, that.date) &&
                Objects.equals(localNames, that.localNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, localNames);
    }

    @Override
    public String toString() {
        return "CommonHolidays{" +
                "date='" + date + '\'' +
                ", localNames=" + localNames +
                '}';
    }
}
