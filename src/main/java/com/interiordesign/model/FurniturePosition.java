package com.interiordesign.model;

public class FurniturePosition {
    private Furniture furniture;
    private double x;
    private double y;

    public FurniturePosition(Furniture furniture, double x, double y) {
        this.furniture = furniture;
        this.x = x;
        this.y = y;
    }

    public Furniture getFurniture() {
        return furniture;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    // JSON-friendly convenience getters
    public String getName() {
        return furniture.getName();
    }

    public double getWidth() {
        return furniture.getWidth();
    }

    public double getDepth() {
        return furniture.getDepth();
    }
}
