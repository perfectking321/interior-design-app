package com.interiordesign.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interiordesign.model.AILayoutSuggestion;
import com.interiordesign.model.AILayoutSuggestion.SuggestedFurniture;
import com.interiordesign.model.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Component responsible for parsing and validating AI responses from OpenRouter API.
 * Extracts furniture suggestions and validates coordinates against room bounds.
 */
@Component
public class AIResponseParser {

    private static final Logger logger = LoggerFactory.getLogger(AIResponseParser.class);
    private static final double WALL_CLEARANCE = 0.5; // meters
    private final ObjectMapper objectMapper;

    public AIResponseParser() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Parses AI response and extracts layout suggestions.
     * 
     * @param aiJsonResponse Raw JSON response from OpenRouter API
     * @param room Room specifications for coordinate validation
     * @return AILayoutSuggestion object with parsed and validated data
     * @throws Exception if parsing fails or response is invalid
     */
    public AILayoutSuggestion parseAIResponse(String aiJsonResponse, Room room) throws Exception {
        logger.info("Parsing AI response...");
        
        try {
            // Parse the JSON response
            JsonNode rootNode = objectMapper.readTree(aiJsonResponse);
            
            // Extract the actual layout data
            // OpenRouter wraps the response in choices[0].message.content
            JsonNode contentNode = extractContentNode(rootNode);
            
            if (contentNode == null || contentNode.isNull()) {
                throw new IllegalArgumentException("No content found in AI response");
            }
            
            // If content is a string (sometimes AI returns JSON as string), parse it again
            String contentString = contentNode.isTextual() ? contentNode.asText() : contentNode.toString();
            JsonNode layoutNode = contentNode.isTextual() ? objectMapper.readTree(contentString) : contentNode;
            
            // Parse suggested furniture
            List<SuggestedFurniture> suggestedFurniture = new ArrayList<>();
            JsonNode furnitureArray = layoutNode.get("suggestedFurniture");
            
            if (furnitureArray != null && furnitureArray.isArray()) {
                for (JsonNode furnitureNode : furnitureArray) {
                    SuggestedFurniture furniture = parseSuggestedFurniture(furnitureNode, room);
                    if (furniture != null) {
                        suggestedFurniture.add(furniture);
                    }
                }
            }
            
            // Extract total cost
            int totalCost = 0;
            if (layoutNode.has("totalEstimatedCost")) {
                totalCost = layoutNode.get("totalEstimatedCost").asInt();
            }
            
            // Extract reasoning
            String reasoning = "";
            if (layoutNode.has("reasoning")) {
                reasoning = layoutNode.get("reasoning").asText();
            }
            
            logger.info("Successfully parsed {} furniture suggestions", suggestedFurniture.size());
            
            AILayoutSuggestion suggestion = new AILayoutSuggestion(suggestedFurniture, totalCost, reasoning);
            return suggestion;
            
        } catch (Exception e) {
            logger.error("Error parsing AI response: {}", e.getMessage());
            logger.debug("Response content: {}", aiJsonResponse);
            throw new Exception("Failed to parse AI response: " + e.getMessage(), e);
        }
    }

    /**
     * Extracts the content node from OpenRouter's response structure.
     * Handles different response formats.
     */
    private JsonNode extractContentNode(JsonNode rootNode) {
        // Try OpenRouter format: choices[0].message.content
        if (rootNode.has("choices") && rootNode.get("choices").isArray()) {
            JsonNode firstChoice = rootNode.get("choices").get(0);
            if (firstChoice.has("message")) {
                JsonNode message = firstChoice.get("message");
                if (message.has("content")) {
                    return message.get("content");
                }
            }
        }
        
        // If the root node itself contains the layout data
        if (rootNode.has("suggestedFurniture")) {
            return rootNode;
        }
        
        return null;
    }

    /**
     * Parses a single furniture suggestion and validates its coordinates.
     */
    private SuggestedFurniture parseSuggestedFurniture(JsonNode furnitureNode, Room room) {
        try {
            String name = furnitureNode.get("name").asText();
            double x = furnitureNode.get("x").asDouble();
            double y = furnitureNode.get("y").asDouble();
            String reasoning = furnitureNode.has("reasoning") ? 
                furnitureNode.get("reasoning").asText() : "";
            
            // Validate coordinates are within room bounds
            if (!isValidCoordinate(x, y, room)) {
                logger.warn("Invalid coordinates for {}: ({}, {}). Adjusting to fit room bounds.", 
                    name, x, y);
                // Adjust coordinates to fit within bounds
                x = Math.max(WALL_CLEARANCE, Math.min(x, room.getLength() - WALL_CLEARANCE));
                y = Math.max(WALL_CLEARANCE, Math.min(y, room.getWidth() - WALL_CLEARANCE));
            }
            
            return new SuggestedFurniture(name, x, y, reasoning);
            
        } catch (Exception e) {
            logger.error("Error parsing furniture item: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Validates that coordinates are within room boundaries with proper clearance.
     */
    private boolean isValidCoordinate(double x, double y, Room room) {
        return x >= WALL_CLEARANCE && 
               x <= (room.getLength() - WALL_CLEARANCE) &&
               y >= WALL_CLEARANCE && 
               y <= (room.getWidth() - WALL_CLEARANCE);
    }

    /**
     * Attempts to extract JSON from a response that may contain additional text.
     * Useful when AI returns explanation before/after the JSON.
     */
    public String extractJsonFromResponse(String response) {
        // Find the first { and last }
        int start = response.indexOf('{');
        int end = response.lastIndexOf('}');
        
        if (start != -1 && end != -1 && end > start) {
            return response.substring(start, end + 1);
        }
        
        return response;
    }
}
