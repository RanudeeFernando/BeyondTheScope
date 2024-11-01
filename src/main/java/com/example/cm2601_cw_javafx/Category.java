package com.example.cm2601_cw_javafx;

public enum Category {
    SPORTS,
    TECHNOLOGY,
    AI,
    HEALTH,
    BUSINESS,
    ENTERTAINMENT,
    POLITICS,
    SCIENCE,
    EDUCATION,
    NATURE;

    public String getName() {
        return this.name().toLowerCase(); // Returns the category name in lowercase
    }



}
