package com.example.cm2601_cw_javafx;

public enum Category {
    WORLD,
    SPORTS,
    TECHNOLOGY,
    AI,
    HEALTH,
    BUSINESS,
    NATURE,
    POLITICS,
    SCIENCE,
    EDUCATION,
    ENTERTAINMENT,
    UNKNOWN;

    public String getName() {
        return this.name(); // Returns the category name in lowercase
    }



}
