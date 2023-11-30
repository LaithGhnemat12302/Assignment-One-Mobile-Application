package com.example.taskmanagement;

public class Day {  //POJO Class
    private String day;

    public static final Day[] days = {
            new Day("Saturday"),
            new Day("Monday"),
            new Day("Tuesday"),
            new Day("Wednesday"),
            new Day("Thursday")
    };

    public Day() {
    }

    public Day(String day) {
        this.day = day;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    @Override
    public String toString() {
        return day;
    }
}
