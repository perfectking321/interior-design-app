package com.interiordesign.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Represents room input by user.
 * Dimensions are in meters.
 */
public class Room {

    @NotNull(message = "Length is required")
    @Min(value = 3, message = "Length must be at least 3 meters")
    @Max(value = 15, message = "Length must be at most 15 meters")
    private Double length;

    @NotNull(message = "Width is required")
    @Min(value = 3, message = "Width must be at least 3 meters")
    @Max(value = 15, message = "Width must be at most 15 meters")
    private Double width;

    @NotNull(message = "Budget is required")
    @Min(value = 500, message = "Budget must be at least $500")
    @Max(value = 10000, message = "Budget must be at most $10000")
    private Integer budget;

    public Room() {}

    public Room(Double length, Double width, Integer budget) {
        this.length = length;
        this.width = width;
        this.budget = budget;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Integer getBudget() {
        return budget;
    }

    public void setBudget(Integer budget) {
        this.budget = budget;
    }
}
