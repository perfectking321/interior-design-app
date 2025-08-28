package com.interiordesign.service;

import com.interiordesign.dao.FurnitureDAO;
import com.interiordesign.model.Furniture;
import com.interiordesign.model.Room;
import com.interiordesign.model.RoomLayout;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * LayoutService coordinates DAO + RuleEngine.
 */
@Service
public class LayoutService {

    private final FurnitureDAO furnitureDAO;
    private final RuleEngine ruleEngine;

    public LayoutService(FurnitureDAO furnitureDAO, RuleEngine ruleEngine) {
        this.furnitureDAO = furnitureDAO;
        this.ruleEngine = ruleEngine;
    }

    public RoomLayout createLayout(Room room) {
        List<Furniture> list = furnitureDAO.findAll();
        return ruleEngine.generateLayout(room, list);
    }

    public List<Furniture> getAllFurniture() {
        return furnitureDAO.findAll();
    }
}
