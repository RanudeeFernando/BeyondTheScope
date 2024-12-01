package com.example.cm2601_cw_javafx.model;

public enum Category {
    ENTERTAINMENT,
    BUSINESS,
    EDUCATION,
    HEALTH_LIFESTYLE,
    SCIENCE_TECH,
    SPORTS,
    POLITICS,
    UNKNOWN;


    public String getName() {
        return this.name();
    }

    public static Category fromString(String categoryName) {
        for (Category category : Category.values()) {
            if (category.name().equalsIgnoreCase(categoryName)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Unexpected category: " + categoryName);
    }



}
