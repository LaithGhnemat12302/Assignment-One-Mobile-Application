package com.example.taskmanagement;

import java.util.Objects;

public class Test {  // Pojo Class
    private String test;
    private String day;

    public Test(Test test) {
    }

    public Test(String test, String day) {
        this.test = test;
        this.day = day;
    }
    //_____________________________________________________________________________________________________________________________________

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
    //_____________________________________________________________________________________________________________________________________

    @Override
    public String toString() {
        return test + " on " + day;
    }
}
