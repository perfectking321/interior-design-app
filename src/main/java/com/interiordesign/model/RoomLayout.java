package com.interiordesign.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Container for result layout and cost details.
 */
public class RoomLayout {
    private Room room;
    private List<FurniturePosition> placed = new ArrayList<>();
    private int totalCost;
    private int remainingBudget;
    private List<String> errors = new ArrayList<>();

    public RoomLayout() {}

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public List<FurniturePosition> getPlaced() {
        return placed;
    }

    public void add(FurniturePosition fp) {
        placed.add(fp);
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    public int getRemainingBudget() {
        return remainingBudget;
    }

    public void setRemainingBudget(int remainingBudget) {
        this.remainingBudget = remainingBudget;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void addError(String e) {
        errors.add(e);
    }

    public void setPlaced(List<FurniturePosition> placed) {
        this.placed = placed;
    }
}
