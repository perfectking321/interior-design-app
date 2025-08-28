package com.interiordesign.model;

/**
 * Furniture entity loaded from DB.
 * width = size along X axis (meters)
 * depth = size along Y axis (meters)
 */
public class Furniture {
    private Long id;
    private String name;
    private double width;   // meters
    private double depth;   // meters
    private int price;      // USD
    private String category; // e.g., sofa, table, tvstand

    public Furniture() {}

    public Furniture(Long id, String name, double width, double depth, int price, String category) {
        this.id = id;
        this.name = name;
        this.width = width;
        this.depth = depth;
        this.price = price;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getWidth() {
        return width;
    }

    public double getDepth() {
        return depth;
    }

    public int getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
