package com.example.taskmanagement;

public class Proj_and_assign_POJO { //POJO Class
    private String name;

    public static final Proj_and_assign_POJO[] proj_assign = {
            new Proj_and_assign_POJO("Projects"),
            new Proj_and_assign_POJO("Assignments")
    };

    public Proj_and_assign_POJO() {
    }

    public Proj_and_assign_POJO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
