package com.example.taskmanagement;

import java.util.Objects;

public class Language {  // Pojo Class
    private String language;

    public Language(Language language) {
    }

    public Language(String language) {
        this.language = language;
    }
    //_____________________________________________________________________________________________________________________________________

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
    //_____________________________________________________________________________________________________________________________________

    @Override
    public String toString() {
        return language;
    }
}
