package com.example.taskmanagement;

import java.util.Objects;

public class Lecture {  // Pojo Class
    private String lecture;
    private String time;

    public Lecture(Lecture lecture) {
    }

    public Lecture(String lecture, String time) {
        this.lecture = lecture;
        this.time = time;
    }
    //_____________________________________________________________________________________________________________________________________

    public String getLecture() {
        return lecture;
    }

    public void setLecture(String lecture) {
        this.lecture = lecture;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    //_____________________________________________________________________________________________________________________________________

    @Override
    public String toString() {
        return lecture + " ==> " + time;
    }
}
