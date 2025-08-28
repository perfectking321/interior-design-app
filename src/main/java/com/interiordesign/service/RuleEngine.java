package com.interiordesign.service;

import com.interiordesign.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * RuleEngine encapsulates placement rules:
 * - Sofa against longest wall (0.5m from wall)
 * - Coffee table 0.8m in front of sofa
 * - TV stand on opposite wall
 * - Bookshelf in corner if space
 * - Side table next to sofa if budget allows
 *
 * Uses a simple rectangle occupancy / collision detection.
 */
@Component
public class RuleEngine {

    private static final double SOFA_WALL_OFFSET = 0.5; // meters from wall
    private static final double COFFEE_DISTANCE = 0.8;   // meters in front of sofa

    /**
     * Attempt to produce a layout given room and available furniture and budget.
     * This will pick one of each important category and optionally add extras.
     */
    public RoomLayout generateLayout(Room room, List<Furniture> furnitureList) {
        RoomLayout layout = new RoomLayout();
        layout.setRoom(room);

        // convert list to lookups
        Furniture sofa = findByCategory(furnitureList, "sofa");
        Furniture coffee = findByCategory(furnitureList, "coffee");
        Furniture tv = findByCategory(furnitureList, "tvstand");
        Furniture bookshelf = findByCategory(furnitureList, "bookshelf");
        Furniture sidetable = findByCategory(furnitureList, "sidetable");
        Furniture armchair = findByCategory(furnitureList, "armchair");

        int budget = room.getBudget();
        int totalCost = 0;
        List<FurniturePosition> placed = new ArrayList<>();

        double roomW = room.getLength(); // interpret length as horizontal
        double roomH = room.getWidth();  // interpret width as vertical

        // Determine longest wall: horizontal if length >= width
        boolean longestIsHorizontal = roomW >= roomH;

        // Place sofa against longest wall, centered if space allows
        if (sofa != null) {
            double x, y;
            if (longestIsHorizontal) {
                // sofa's depth aligns with vertical axis; place along top or bottom wall; choose top
                y = SOFA_WALL_OFFSET; // from top
                x = Math.max(0.1, (roomW - sofa.getWidth()) / 2.0); // center horizontally
            } else {
                // longest wall vertical => place along left wall (sofa rotated)
                x = SOFA_WALL_OFFSET;
                y = Math.max(0.1, (roomH - sofa.getDepth()) / 2.0); // center vertically
            }
            FurniturePosition sofaPos = new FurniturePosition(sofa, x, y);
            if (fitsInRoom(sofaPos, roomW, roomH)) {
                placed.add(sofaPos);
                totalCost += sofa.getPrice();
            } else {
                layout.addError("Sofa does not fit the room with the chosen orientation.");
            }
        } else {
            layout.addError("No sofa found in furniture database.");
        }

        // Place coffee table in front of sofa
        if (coffee != null && !placed.isEmpty()) {
            FurniturePosition sofaPos = placed.get(0);
            double x = sofaPos.getX() + (sofaPos.getWidth() - coffee.getWidth()) / 2.0;
            double y;
            if (longestIsHorizontal) {
                // sofa against top wall => coffee below sofa by COFFEE_DISTANCE
                y = sofaPos.getY() + sofaPos.getDepth() + COFFEE_DISTANCE;
            } else {
                // sofa along left wall => coffee to right of sofa
                x = sofaPos.getX() + sofaPos.getWidth() + COFFEE_DISTANCE;
                y = sofaPos.getY() + (sofaPos.getDepth() - coffee.getDepth()) / 2.0;
            }
            FurniturePosition coffeePos = new FurniturePosition(coffee, x, y);
            if (fitsInRoom(coffeePos, roomW, roomH) && !overlapsAny(coffeePos, placed)) {
                placed.add(coffeePos);
                totalCost += coffee.getPrice();
            } else {
                layout.addError("Coffee table could not be placed without overlap.");
            }
        } else {
            layout.addError("No coffee table found in furniture database.");
        }

        // Place TV stand on opposite wall from sofa
        if (tv != null && !placed.isEmpty()) {
            FurniturePosition sofaPos = placed.get(0);
            double x, y;
            if (longestIsHorizontal) {
                // sofa top => tv on bottom wall
                y = roomH - tv.getDepth() - SOFA_WALL_OFFSET;
                x = Math.max(0.1, (roomW - tv.getWidth()) / 2.0);
            } else {
                // sofa on left => tv on right wall
                x = roomW - tv.getWidth() - SOFA_WALL_OFFSET;
                y = Math.max(0.1, (roomH - tv.getDepth()) / 2.0);
            }
            FurniturePosition tvPos = new FurniturePosition(tv, x, y);
            if (fitsInRoom(tvPos, roomW, roomH) && !overlapsAny(tvPos, placed)) {
                placed.add(tvPos);
                totalCost += tv.getPrice();
            } else {
                layout.addError("TV stand could not be placed without overlap.");
            }
        } else {
            layout.addError("No TV stand found in furniture database.");
        }

        // Optionally place bookshelf in a corner if space and budget allow
        if (bookshelf != null) {
            FurniturePosition bookPos = new FurniturePosition(bookshelf, 0.1, 0.1);
            if (fitsInRoom(bookPos, roomW, roomH) && !overlapsAny(bookPos, placed)) {
                if (totalCost + bookshelf.getPrice() <= budget) {
                    placed.add(bookPos);
                    totalCost += bookshelf.getPrice();
                } else {
                    layout.addError("Bookshelf available but exceeds budget.");
                }
            } // if doesn't fit, skip quietly
        }

        // Optionally place side table next to sofa if budget allows
        if (sidetable != null && !placed.isEmpty()) {
            FurniturePosition sofaPos = placed.get(0);
            double x = sofaPos.getX() + sofaPos.getWidth() + 0.1; // to the right
            double y = sofaPos.getY();
            FurniturePosition sidePos = new FurniturePosition(sidetable, x, y);
            if (fitsInRoom(sidePos, roomW, roomH) && !overlapsAny(sidePos, placed)) {
                if (totalCost + sidetable.getPrice() <= budget) {
                    placed.add(sidePos);
                    totalCost += sidetable.getPrice();
                } // else don't add
            }
        }

        // If still budget remains, optionally add armchair
        if (armchair != null) {
            if (totalCost + armchair.getPrice() <= budget) {
                // attempt to place next to coffee table if possible
                FurniturePosition pos = null;
                for (FurniturePosition p : placed) {
                    if ("coffee".equalsIgnoreCase(p.getFurniture().getCategory())) {
                        double x = p.getX() + p.getWidth() + 0.3;
                        double y = p.getY();
                        FurniturePosition tryPos = new FurniturePosition(armchair, x, y);
                        if (fitsInRoom(tryPos, roomW, roomH) && !overlapsAny(tryPos, placed)) {
                            pos = tryPos;
                            break;
                        }
                    }
                }
                if (pos == null) {
                    // fallback: bottom-left corner
                    FurniturePosition tryPos = new FurniturePosition(armchair, 0.2, roomH - armchair.getDepth() - 0.2);
                    if (fitsInRoom(tryPos, roomW, roomH) && !overlapsAny(tryPos, placed)) {
                        pos = tryPos;
                    }
                }
                if (pos != null) {
                    placed.add(pos);
                    totalCost += armchair.getPrice();
                }
            }
        }

        // Set results
        layout.setPlaced(placed);
        layout.setTotalCost(totalCost);
        layout.setRemainingBudget(Math.max(0, budget - totalCost));
        return layout;
    }

    private Furniture findByCategory(List<Furniture> list, String category) {
        if (category == null) return null;
        for (Furniture f : list) {
            if (category.equalsIgnoreCase(f.getCategory())) {
                return f;
            }
        }
        return null;
    }

    private boolean fitsInRoom(FurniturePosition pos, double roomW, double roomH) {
        if (pos.getX() < 0 || pos.getY() < 0) return false;
        if (pos.getX() + pos.getWidth() > roomW + 1e-6) return false;
        if (pos.getY() + pos.getDepth() > roomH + 1e-6) return false;
        return true;
    }

    private boolean overlapsAny(FurniturePosition candidate, List<FurniturePosition> placed) {
        for (FurniturePosition p : placed) {
            if (rectOverlap(candidate.getX(), candidate.getY(), candidate.getWidth(), candidate.getDepth(),
                    p.getX(), p.getY(), p.getWidth(), p.getDepth())) {
                return true;
            }
        }
        return false;
    }

    private boolean rectOverlap(double ax, double ay, double aw, double ah,
                                double bx, double by, double bw, double bh) {
        boolean noOverlap = ax + aw <= bx || bx + bw <= ax || ay + ah <= by || by + bh <= ay;
        return !noOverlap;
    }
}
