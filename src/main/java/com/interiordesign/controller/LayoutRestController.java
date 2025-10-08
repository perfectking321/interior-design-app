package com.interiordesign.controller;

import com.interiordesign.model.Furniture;
import com.interiordesign.model.Room;
import com.interiordesign.model.RoomLayout;
import com.interiordesign.service.LayoutService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST API Controller for furniture and layout generation.
 * Provides endpoints for React frontend to interact with backend services.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class LayoutRestController {

    private final LayoutService layoutService;

    public LayoutRestController(LayoutService layoutService) {
        this.layoutService = layoutService;
    }

    /**
     * GET /api/furniture
     * Returns list of all available furniture as JSON.
     */
    @GetMapping("/furniture")
    public ResponseEntity<List<Furniture>> getAllFurniture() {
        try {
            List<Furniture> furniture = layoutService.getAllFurniture();
            return ResponseEntity.ok(furniture);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * POST /api/layout
     * Accepts room data and generates furniture layout.
     * Returns layout with furniture positions as JSON.
     */
    @PostMapping("/layout")
    public ResponseEntity<?> generateLayout(@Valid @RequestBody Room room) {
        try {
            RoomLayout layout = layoutService.createLayout(room);
            
            if (layout == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Unable to generate layout");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
            }
            
            return ResponseEntity.ok(layout);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "An unexpected error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
