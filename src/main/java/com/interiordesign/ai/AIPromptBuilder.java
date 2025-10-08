package com.interiordesign.ai;

import com.interiordesign.model.Furniture;
import com.interiordesign.model.Room;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Component responsible for building detailed prompts for AI layout generation.
 * Constructs prompts with room specifications, furniture options, and design constraints.
 */
@Component
public class AIPromptBuilder {

    private static final double WALL_CLEARANCE = 0.5; // meters

    /**
     * Builds a comprehensive prompt for AI to generate furniture layout.
     * 
     * @param room The room specifications (length, width, budget)
     * @param furniture List of available furniture with dimensions and prices
     * @return Formatted prompt string for AI
     */
    public String buildLayoutPrompt(Room room, List<Furniture> furniture) {
        StringBuilder prompt = new StringBuilder();
        
        // Introduction and context
        prompt.append("You are an expert interior designer. Your task is to create an optimal furniture layout for a room.\n\n");
        
        // Room specifications
        prompt.append("ROOM SPECIFICATIONS:\n");
        prompt.append(String.format("- Dimensions: %.1f meters (length) x %.1f meters (width)\n", 
                room.getLength(), room.getWidth()));
        prompt.append(String.format("- Budget: $%d\n", room.getBudget()));
        prompt.append(String.format("- Wall clearance required: %.1f meters from all walls\n", WALL_CLEARANCE));
        prompt.append(String.format("- Usable space: %.1f meters x %.1f meters (accounting for wall clearance)\n\n", 
                room.getLength() - (2 * WALL_CLEARANCE), room.getWidth() - (2 * WALL_CLEARANCE)));
        
        // Available furniture
        prompt.append("AVAILABLE FURNITURE OPTIONS:\n");
        for (Furniture f : furniture) {
            prompt.append(String.format("- %s (Category: %s)\n", f.getName(), f.getCategory()));
            prompt.append(String.format("  Dimensions: %.2f m (width) x %.2f m (depth)\n", f.getWidth(), f.getDepth()));
            prompt.append(String.format("  Price: $%d\n", f.getPrice()));
        }
        prompt.append("\n");
        
        // Design principles
        prompt.append("INTERIOR DESIGN PRINCIPLES TO FOLLOW:\n");
        prompt.append("1. Create conversation areas and focal points\n");
        prompt.append("2. Ensure proper traffic flow (minimum 1 meter pathways)\n");
        prompt.append("3. Place larger furniture (sofas, beds) against walls when possible\n");
        prompt.append("4. Consider natural light and room function\n");
        prompt.append("5. Balance the room visually\n");
        prompt.append("6. Stay within budget constraint\n");
        prompt.append("7. Maintain minimum " + WALL_CLEARANCE + " meters clearance from walls\n\n");
        
        // Coordinate system explanation
        prompt.append("COORDINATE SYSTEM:\n");
        prompt.append("- Origin (0, 0) is at the bottom-left corner of the room\n");
        prompt.append("- X-axis runs along the length (0 to " + room.getLength() + " meters)\n");
        prompt.append("- Y-axis runs along the width (0 to " + room.getWidth() + " meters)\n");
        prompt.append("- Coordinates (x, y) represent the CENTER of each furniture piece\n");
        prompt.append("- All furniture must fit within the room boundaries with proper clearance\n\n");
        
        // Output format instructions
        prompt.append("REQUIRED OUTPUT FORMAT (JSON only, no additional text):\n");
        prompt.append("{\n");
        prompt.append("  \"suggestedFurniture\": [\n");
        prompt.append("    {\n");
        prompt.append("      \"name\": \"Exact furniture name from the available list\",\n");
        prompt.append("      \"x\": <x-coordinate in meters>,\n");
        prompt.append("      \"y\": <y-coordinate in meters>,\n");
        prompt.append("      \"reasoning\": \"Brief explanation for this placement\"\n");
        prompt.append("    }\n");
        prompt.append("  ],\n");
        prompt.append("  \"totalEstimatedCost\": <total cost of selected furniture>,\n");
        prompt.append("  \"reasoning\": \"Overall design strategy and layout explanation\"\n");
        prompt.append("}\n\n");
        
        // Important constraints
        prompt.append("IMPORTANT:\n");
        prompt.append("- Only use furniture names EXACTLY as listed above\n");
        prompt.append("- Do NOT exceed the budget of $" + room.getBudget() + "\n");
        prompt.append("- Ensure all coordinates are within room bounds with clearance\n");
        prompt.append("- Provide ONLY the JSON response, no additional commentary\n");
        prompt.append("- Select 3-7 pieces of furniture for a balanced room\n");
        
        return prompt.toString();
    }

    /**
     * Builds a simplified fallback prompt if the main prompt fails.
     * 
     * @param room The room specifications
     * @param furniture List of available furniture
     * @return Simplified prompt string
     */
    public String buildSimplifiedPrompt(Room room, List<Furniture> furniture) {
        return String.format(
            "Create a furniture layout for a %.1fm x %.1fm room with a $%d budget. " +
            "Available furniture: %s. " +
            "Return JSON with suggestedFurniture array containing name, x, y coordinates, and reasoning.",
            room.getLength(), room.getWidth(), room.getBudget(),
            furniture.stream().map(Furniture::getName).reduce((a, b) -> a + ", " + b).orElse("none")
        );
    }
}
