package com.example.taskmanagement;

import java.util.Objects;

public class Pray {  // Pojo Class
    private String name;

    public Pray(Pray pray) {
    }

    public Pray(String name) {
        this.name = name;
    }
    //_____________________________________________________________________________________________________________________________________

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //_____________________________________________________________________________________________________________________________________

    @Override
    public String toString() {
        return name;
    }
}
