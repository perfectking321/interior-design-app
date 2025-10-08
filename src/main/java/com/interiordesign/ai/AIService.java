package com.interiordesign.ai;

import com.interiordesign.model.AILayoutSuggestion;
import com.interiordesign.model.Furniture;
import com.interiordesign.model.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service for interacting with OpenRouter AI API to generate intelligent furniture layouts.
 * Uses WebClient for asynchronous HTTP communication and handles API failures gracefully.
 */
@Service
public class AIService {

    private static final Logger logger = LoggerFactory.getLogger(AIService.class);
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(30);

    private final WebClient webClient;
    private final AIPromptBuilder promptBuilder;
    private final AIResponseParser responseParser;
    private final String apiUrl;
    private final String apiKey;
    private final String model;

    public AIService(
            @Value("${openrouter.api.url}") String apiUrl,
            @Value("${openrouter.api.key}") String apiKey,
            @Value("${openrouter.model}") String model,
            AIPromptBuilder promptBuilder,
            AIResponseParser responseParser,
            WebClient.Builder webClientBuilder) {
        
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
        this.model = model;
        this.promptBuilder = promptBuilder;
        this.responseParser = responseParser;
        
        // Build WebClient with proper configuration
        this.webClient = webClientBuilder
                .baseUrl(apiUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("HTTP-Referer", "http://localhost:8080")
                .defaultHeader("X-Title", "Interior Design App")
                .build();
        
        logger.info("AIService initialized with model: {}", model);
    }

    /**
     * Generates furniture layout suggestions using AI.
     * Falls back to empty suggestions if AI call fails.
     * 
     * @param room The room specifications
     * @param availableFurniture List of furniture available for placement
     * @return AILayoutSuggestion with furniture placements and reasoning
     */
    public AILayoutSuggestion suggestLayout(Room room, List<Furniture> availableFurniture) {
        logger.info("Requesting AI layout suggestion for {}m x {}m room with ${} budget", 
                room.getLength(), room.getWidth(), room.getBudget());
        
        try {
            // Build the prompt
            String prompt = promptBuilder.buildLayoutPrompt(room, availableFurniture);
            logger.debug("Generated prompt: {}", prompt);
            
            // Prepare the request body for OpenRouter API
            Map<String, Object> requestBody = buildRequestBody(prompt);
            
            // Make the API call
            String response = webClient.post()
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(REQUEST_TIMEOUT)
                    .doOnError(error -> logger.error("AI API call failed: {}", error.getMessage()))
                    .onErrorResume(error -> {
                        logger.warn("AI service unavailable, will use fallback layout generation");
                        return Mono.just("{}"); // Return empty JSON on error
                    })
                    .block();
            
            logger.debug("Received AI response: {}", response);
            
            // Parse the response
            if (response != null && !response.equals("{}")) {
                // Try to extract JSON if response contains extra text
                String cleanedResponse = responseParser.extractJsonFromResponse(response);
                AILayoutSuggestion suggestion = responseParser.parseAIResponse(cleanedResponse, room);
                
                logger.info("AI successfully suggested {} furniture pieces", 
                        suggestion.getSuggestedFurniture().size());
                logger.info("AI reasoning: {}", suggestion.getReasoning());
                
                return suggestion;
            }
            
        } catch (WebClientResponseException e) {
            logger.error("OpenRouter API error - Status: {}, Response: {}", 
                    e.getStatusCode(), e.getResponseBodyAsString());
        } catch (Exception e) {
            logger.error("Unexpected error calling AI service: {}", e.getMessage(), e);
        }
        
        // Return empty suggestion on any failure
        logger.info("Returning empty AI suggestion, will fallback to rule-based generation");
        return new AILayoutSuggestion();
    }

    /**
     * Builds the request body for OpenRouter API according to their specification.
     */
    private Map<String, Object> buildRequestBody(String prompt) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);
        
        // Create messages array with user prompt
        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", prompt);
        
        requestBody.put("messages", List.of(userMessage));
        
        // Optional parameters for better responses
        requestBody.put("temperature", 0.7);
        requestBody.put("max_tokens", 2000);
        requestBody.put("top_p", 0.9);
        
        return requestBody;
    }

    /**
     * Checks if AI service is properly configured.
     * 
     * @return true if API key is set and not placeholder
     */
    public boolean isConfigured() {
        boolean configured = apiKey != null && 
                !apiKey.isEmpty() && 
                !apiKey.equals("your-api-key-here");
        
        if (!configured) {
            logger.warn("AI service not properly configured. Please set OPENROUTER_API_KEY environment variable.");
        }
        
        return configured;
    }

    /**
     * Tests the AI service connection.
     * 
     * @return true if connection is successful
     */
    public boolean testConnection() {
        if (!isConfigured()) {
            return false;
        }
        
        try {
            // Send a simple test request
            Map<String, Object> testRequest = new HashMap<>();
            testRequest.put("model", model);
            
            Map<String, String> testMessage = new HashMap<>();
            testMessage.put("role", "user");
            testMessage.put("content", "Test connection. Reply with 'OK'.");
            
            testRequest.put("messages", List.of(testMessage));
            testRequest.put("max_tokens", 10);
            
            String response = webClient.post()
                    .bodyValue(testRequest)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(10))
                    .block();
            
            logger.info("AI service connection test successful");
            return response != null;
            
        } catch (Exception e) {
            logger.error("AI service connection test failed: {}", e.getMessage());
            return false;
        }
    }
}
