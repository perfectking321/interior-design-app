package com.interiordesign.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class to hold AI-generated layout suggestions.
 * Contains suggested furniture placements with coordinates and reasoning.
 */
public class AILayoutSuggestion {
    
    private List<SuggestedFurniture> suggestedFurniture = new ArrayList<>();
    private int totalEstimatedCost;
    private String reasoning;

    public AILayoutSuggestion() {}

    public AILayoutSuggestion(List<SuggestedFurniture> suggestedFurniture, int totalEstimatedCost, String reasoning) {
        this.suggestedFurniture = suggestedFurniture;
        this.totalEstimatedCost = totalEstimatedCost;
        this.reasoning = reasoning;
    }

    public List<SuggestedFurniture> getSuggestedFurniture() {
        return suggestedFurniture;
    }

    public void setSuggestedFurniture(List<SuggestedFurniture> suggestedFurniture) {
        this.suggestedFurniture = suggestedFurniture;
    }

    public int getTotalEstimatedCost() {
        return totalEstimatedCost;
    }

    public void setTotalEstimatedCost(int totalEstimatedCost) {
        this.totalEstimatedCost = totalEstimatedCost;
    }

    public String getReasoning() {
        return reasoning;
    }

    public void setReasoning(String reasoning) {
        this.reasoning = reasoning;
    }

    /**
     * Nested class representing a single furniture item suggested by AI
     * with its position coordinates and design reasoning.
     */
    public static class SuggestedFurniture {
        private String name;
        private double x;
        private double y;
        private String reasoning;

        public SuggestedFurniture() {}

        public SuggestedFurniture(String name, double x, double y, String reasoning) {
            this.name = name;
            this.x = x;
            this.y = y;
            this.reasoning = reasoning;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

        public String getReasoning() {
            return reasoning;
        }

        public void setReasoning(String reasoning) {
            this.reasoning = reasoning;
        }

        @Override
        public String toString() {
            return "SuggestedFurniture{" +
                    "name='" + name + '\'' +
                    ", x=" + x +
                    ", y=" + y +
                    ", reasoning='" + reasoning + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "AILayoutSuggestion{" +
                "suggestedFurniture=" + suggestedFurniture +
                ", totalEstimatedCost=" + totalEstimatedCost +
                ", reasoning='" + reasoning + '\'' +
                '}';
    }
}
