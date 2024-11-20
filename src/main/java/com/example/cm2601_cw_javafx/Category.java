package com.example.cm2601_cw_javafx;

import org.jetbrains.annotations.NotNull;

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

    // You can also add a method to get the enum from a string
    public static Category fromString(String categoryName) {
        for (Category category : Category.values()) {
            if (category.name().equalsIgnoreCase(categoryName)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Unexpected category: " + categoryName);
    }



}
