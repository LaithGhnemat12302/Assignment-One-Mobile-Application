package com.example.taskmanagement;

import java.util.Objects;

public class Match {  // Pojo Class
    private String firstTeam;
    private String secondTeam;
    private String time;

    public Match(Match match) {
    }

    public Match(String firstTeam, String secondTeam, String time) {
        this.firstTeam = firstTeam;
        this.secondTeam = secondTeam;
        this.time = time;
    }
    //_____________________________________________________________________________________________________________________________________

    public String getFirstTeam() {
        return firstTeam;
    }

    public void setFirstTeam(String firstTeam) {
        this.firstTeam = firstTeam;
    }

    public String getSecondTeam() {
        return secondTeam;
    }

    public void setSecondTeam(String secondTeam) {
        this.secondTeam = secondTeam;
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
        return firstTeam + " VS " + secondTeam + " at " + time;
    }
}
