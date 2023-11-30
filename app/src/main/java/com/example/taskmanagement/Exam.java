package com.example.taskmanagement;

public class Exam {  //POJO Class
    private String name;

    public static final Exam[] exams = {
            new Exam("Quizes"),
            new Exam("Midterms"),
            new Exam("Finals")
    };

    public Exam() {
    }

    public Exam(String name) {
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
