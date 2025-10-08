# Prompt for AI Agent to Modify Existing Spring Boot Interior Design Project

## Project Context
You are modifying an existing Spring Boot interior design application. The project currently uses Thymeleaf templates for the frontend and needs to be converted to a REST API backend that will serve a separate React frontend. Additionally, AI capabilities using OpenRouter's Llama 3.1 70B model need to be integrated for intelligent furniture layout generation.

## Current Project Structure
```
interior-design-app/
├── pom.xml
├── src/main/java/com/interiordesign/
│   ├── InteriorDesignApplication.java
│   ├── config/DatabaseConfig.java
│   ├── controller/
│   │   ├── HomeController.java
│   │   ├── CustomErrorController.java
│   │   └── GlobalExceptionHandler.java
│   ├── dao/FurnitureDAO.java
│   ├── model/
│   │   ├── Furniture.java
│   │   ├── FurniturePosition.java
│   │   ├── Room.java
│   │   └── RoomLayout.java
│   └── service/
│       ├── LayoutService.java
│       └── RuleEngine.java
└── src/main/resources/
    ├── application.properties
    ├── schema.sql
    ├── data.sql
    ├── static/style.css
    └── templates/ (room-form.html, layout-result.html, error-page.html)
```

## Required Modifications

### PHASE 1: Backend Conversion to REST API

#### 1.1 Update pom.xml Dependencies
- Remove spring-boot-starter-thymeleaf dependency completely
- Add spring-boot-starter-webflux dependency for WebClient (to call OpenRouter API)
- Keep all existing dependencies: spring-boot-starter-web, spring-boot-starter-jdbc, h2, validation
- Ensure Maven compiler plugin targets Java 17

#### 1.2 Create CORS Configuration
- Create new file: `src/main/java/com/interiordesign/config/CorsConfig.java`
- Implement WebMvcConfigurer interface
- Configure CORS to allow:
  - Origin: http://localhost:3000 (React dev server)
  - Methods: GET, POST, PUT, DELETE, OPTIONS
  - Headers: All headers allowed
  - Credentials: true
- Use @Configuration annotation

#### 1.3 Convert Controllers to REST
- **Delete** the following files entirely:
  - `controller/HomeController.java`
  - `controller/CustomErrorController.java`
  
- **Keep but modify** `controller/GlobalExceptionHandler.java`:
  - Change it to return JSON responses instead of view names
  - Use @RestControllerAdvice instead of @ControllerAdvice
  - Return ResponseEntity with error details in JSON format
  - Handle validation errors with proper HTTP status codes

- **Create new file**: `src/main/java/com/interiordesign/controller/LayoutRestController.java`
  - Use @RestController annotation
  - Use @RequestMapping("/api") for base path
  - Create endpoint: GET /api/furniture
    - Returns List<Furniture> as JSON
    - Calls FurnitureDAO.getAllFurniture()
  - Create endpoint: POST /api/layout
    - Accepts @RequestBody Room object
    - Validates input using @Valid annotation
    - Calls LayoutService.createLayoutWithAI(room)
    - Returns ResponseEntity<RoomLayout> as JSON
    - Include proper error handling with try-catch
  - Use @CrossOrigin annotation on class level as backup

#### 1.4 Update application.properties
- Add CORS configuration properties if needed
- Keep all existing H2 database configuration
- Keep existing Thymeleaf configuration (will be removed later but won't cause issues)
- Add OpenRouter API configuration placeholder:
  - openrouter.api.url=https://openrouter.ai/api/v1/chat/completions
  - openrouter.api.key=${OPENROUTER_API_KEY:your-api-key-here}
  - openrouter.model=meta-llama/llama-3.1-70b-instruct

### PHASE 2: AI Integration Package

#### 2.1 Create AI Package Structure
Create new package: `src/main/java/com/interiordesign/ai/`

#### 2.2 Create AIService.java
- Location: `src/main/java/com/interiordesign/ai/AIService.java`
- Use @Service annotation
- Inject WebClient using constructor injection
- Inject @Value for OpenRouter API key and URL from application.properties
- Create method: `AILayoutSuggestion suggestLayout(Room room, List<Furniture> availableFurniture)`
  - Build prompt asking AI to suggest furniture types and exact coordinates
  - Format room specs: "Room dimensions: [length]m x [width]m, Budget: $[budget]"
  - Include available furniture list in prompt with their dimensions and prices
  - Request AI to return JSON with exact coordinates for each furniture piece
  - Make HTTP POST call to OpenRouter API using WebClient
  - Parse AI response and extract furniture suggestions with coordinates
  - Handle API errors gracefully with try-catch and return fallback suggestions
  - Set timeout of 30 seconds for AI API call

#### 2.3 Create AIPromptBuilder.java
- Location: `src/main/java/com/interiordesign/ai/AIPromptBuilder.java`
- Use @Component annotation
- Create method: `String buildLayoutPrompt(Room room, List<Furniture> furniture)`
  - Construct detailed prompt for AI including:
    - Room dimensions with wall clearance rules (0.5m from walls)
    - Budget constraint
    - Available furniture with dimensions and prices
    - Request specific JSON format output
    - Interior design rules: traffic flow, conversation areas, focal points
  - Prompt should ask AI to return JSON with structure:
    ```
    {
      "suggestedFurniture": [
        {
          "name": "Sofa",
          "x": 1.0,
          "y": 2.5,
          "reasoning": "Placed for optimal room flow"
        }
      ],
      "totalEstimatedCost": 1500
    }
    ```

#### 2.4 Create AIResponseParser.java
- Location: `src/main/java/com/interiordesign/ai/AIResponseParser.java`
- Use @Component annotation
- Create method: `AILayoutSuggestion parseAIResponse(String aiJsonResponse)`
  - Parse JSON response from OpenRouter API
  - Extract furniture suggestions with coordinates
  - Validate that coordinates are within room bounds
  - Validate that suggested furniture exists in database
  - Handle malformed JSON gracefully with error logging
  - Return structured AILayoutSuggestion object

#### 2.5 Create AILayoutSuggestion.java (Model)
- Location: `src/main/java/com/interiordesign/model/AILayoutSuggestion.java`
- Create POJO class with fields:
  - List<SuggestedFurniture> suggestedFurniture
  - int totalEstimatedCost
  - String reasoning (optional design tips from AI)
- Create nested class SuggestedFurniture with fields:
  - String name
  - double x
  - double y
  - String reasoning
- Include getters, setters, and constructors

### PHASE 3: Modify Existing Services

#### 3.1 Modify LayoutService.java
- Keep existing createLayout(Room room) method as-is
- Create new method: `RoomLayout createLayoutWithAI(Room room)`
  - Call AIService.suggestLayout() to get AI suggestions
  - Pass AI suggestions to RuleEngine.generateLayoutFromAI()
  - If AI fails, fallback to original RuleEngine.generateLayout()
  - Log AI suggestions for debugging
  - Return final RoomLayout

#### 3.2 Modify RuleEngine.java
- Keep existing generateLayout(Room room) method unchanged
- Create new method: `RoomLayout generateLayoutFromAI(Room room, AILayoutSuggestion aiSuggestion, List<Furniture> allFurniture)`
  - Take AI suggested furniture with coordinates
  - Validate each furniture placement:
    - Check if coordinates are within room boundaries
    - Verify wall clearance (0.5m minimum)
    - Check for overlaps with other furniture
    - Ensure budget is not exceeded
  - If AI suggestion is valid, use it directly
  - If AI suggestion has issues, apply your existing rule-based corrections
  - Create FurniturePosition objects for each valid placement
  - Calculate total cost
  - Generate warnings if AI suggestions were modified
  - Return complete RoomLayout object

### PHASE 4: React Frontend Setup

#### 4.1 Create Separate React Project
- Create new directory at same level as Spring Boot project: `interior-design-frontend/`
- Initialize React project using Vite with command: `npm create vite@latest interior-design-frontend -- --template react`
- Install dependencies:
  - Tailwind CSS: `npm install -D tailwindcss postcss autoprefixer`
  - Axios for API calls: `npm install axios`
  - React Icons: `npm install react-icons`

#### 4.2 Configure Tailwind CSS
- Create tailwind.config.js with warm color scheme:
  - Extend colors with custom palette:
    - Primary: warm beige/cream (#F5F5DC, #FFF8DC)
    - Secondary: brown tones (#8B4513, #A0522D)
    - Accent: gold (#DAA520)
    - Background: soft gray (#F5F5F5)
    - Furniture colors: various warm browns, woods, neutrals
- Configure content paths to scan all JSX files
- Create postcss.config.js with Tailwind and Autoprefixer

#### 4.3 Project Structure for React
```
src/
├── App.jsx (main component with routing logic)
├── index.css (Tailwind imports)
├── components/
│   ├── RoomForm.jsx
│   ├── LayoutDisplay.jsx
│   ├── FurnitureItem.jsx
│   ├── LoadingSpinner.jsx
│   └── CostSummary.jsx
├── services/
│   └── api.js (Axios configuration for backend calls)
└── utils/
    └── helpers.js (utility functions)
```

#### 4.4 Create API Service (api.js)
- Create Axios instance configured for http://localhost:8080
- Create functions:
  - `getAllFurniture()` - GET /api/furniture
  - `generateLayout(roomData)` - POST /api/layout with room object
- Include error handling with try-catch
- Add request interceptors for logging
- Add response interceptors for error formatting

#### 4.5 Create RoomForm Component
- Design: Centered card with glassmorphism effect
- Background: Warm gradient (beige to light gold)
- Card styling: Frosted glass effect with backdrop-blur
- Form fields:
  - Length input (3-15 meters) with icons
  - Width input (3-15 meters) with icons
  - Budget input ($500-$10,000) with icons
- Validation: Client-side validation matching backend constraints
- Submit button: Large, prominent with hover animations
- Use React useState for form state management
- Call api.generateLayout() on submit
- Show loading spinner during API call
- Handle errors with user-friendly messages

#### 4.6 Create LayoutDisplay Component
- Display room as SVG canvas with proper scaling
- Show room boundaries as border
- Render furniture as rounded rectangles with:
  - Flat colors (different color per furniture category)
  - Furniture icons on top (from react-icons)
  - Labels with furniture names
- Position furniture using absolute positioning based on x, y coordinates
- Scale furniture dimensions proportionally to room size
- Colors: Sofa (brown), Tables (wood tone), TV Stand (dark), etc.
- Include grid lines for visual reference
- Make furniture non-interactive (visual preview only, drag disabled for now)

#### 4.7 Create FurnitureItem Component
- Reusable component for individual furniture pieces in SVG
- Props: name, x, y, width, depth, color, category
- Render as SVG rect with rounded corners
- Add furniture icon based on category
- Display furniture label
- Apply appropriate colors and styling

#### 4.8 Create LoadingSpinner Component
- Simple animated spinner with message "Generating your room..."
- Use Tailwind animations for smooth rotation
- Center on screen with overlay
- Warm color scheme matching app theme

#### 4.9 Create CostSummary Component
- Display below layout as card
- Show list of furniture with individual prices
- Calculate and display total cost
- Show budget remaining
- Use table or list format with proper styling
- Highlight if over budget with warning colors

#### 4.10 Create App.jsx
- Main application component
- Manage state:
  - roomData (form inputs)
  - layout (API response)
  - loading status
  - error messages
- Conditional rendering:
  - Show RoomForm initially
  - Show LoadingSpinner during API call
  - Show LayoutDisplay + CostSummary after successful generation
  - Show error message if API fails
- Include "Design Another Room" button to reset form

### PHASE 5: Configuration and Documentation

#### 5.1 Update README or Create MIGRATION_GUIDE.md
- Add section "Running the Application"
  - Backend: `mvn spring-boot:run` (port 8080)
  - Frontend: `cd interior-design-frontend && npm run dev` (port 3000)
- Add section "Getting OpenRouter API Key"
  - Sign up at openrouter.ai
  - Create API key
  - Set environment variable: `export OPENROUTER_API_KEY=your-key`
  - Alternative: Add to application.properties (not recommended for production)
- Add section "Testing the Application"
  - Access frontend at http://localhost:3000
  - Enter room dimensions and budget
  - View AI-generated layout

#### 5.2 Environment Variables Setup
- Create .env.example file in Spring Boot project root:
  ```
  OPENROUTER_API_KEY=your-api-key-here
  ```
- Add .env to .gitignore
- Document how to set environment variables on different OS

#### 5.3 Delete Unused Files
After confirming everything works:
- Delete `src/main/resources/templates/` directory entirely
- Delete `src/main/resources/static/style.css` if not used elsewhere
- Remove Thymeleaf dependency from pom.xml
- Clean Maven project: `mvn clean`

### PHASE 6: Testing Instructions

#### 6.1 Backend Testing
- Start Spring Boot application
- Test GET http://localhost:8080/api/furniture using Postman or browser
  - Should return JSON array of furniture
- Test POST http://localhost:8080/api/layout using Postman
  - Send JSON body: `{"length": 5.0, "width": 4.0, "budget": 2000}`
  - Should return JSON with layout including furniture positions
- Verify CORS headers are present in response
- Check application logs for AI API calls and responses

#### 6.2 Frontend Testing
- Start React development server
- Open http://localhost:3000 in browser
- Test form validation with invalid inputs
- Submit valid room data
- Verify layout displays correctly with furniture
- Check browser console for errors
- Verify furniture colors and positions are correct
- Test responsive behavior (should work on desktop only as per requirements)

#### 6.3 Integration Testing
- Test complete flow: Form → API → Layout Display
- Verify AI suggestions are being used
- Check that layout respects room boundaries
- Confirm budget constraints are enforced
- Test error scenarios:
  - Invalid room dimensions
  - Budget too low
  - OpenRouter API failure (should fallback to rule engine)

## Important Implementation Notes

### Error Handling Strategy
- Backend should always return valid JSON responses
- Use proper HTTP status codes (200, 400, 500)
- Frontend should display user-friendly error messages
- AI failures should fallback to original rule-based layout generation
- Log all AI interactions for debugging

### AI Prompt Engineering Tips
- Be very specific about output format in prompt
- Request JSON format explicitly
- Include interior design best practices in prompt
- Mention wall clearance, traffic flow, conversation areas
- Provide furniture dimensions so AI can calculate properly
- Ask AI to explain its reasoning for transparency

### Performance Considerations
- AI API calls may take 5-10 seconds, show loading spinner
- Cache furniture list in React to avoid repeated API calls
- Consider adding timeout handling for AI requests
- Use async/await properly to avoid blocking

### Security Considerations
- Never commit OpenRouter API key to repository
- Use environment variables for sensitive data
- Validate all user inputs on backend
- Sanitize AI responses before using coordinates
- Implement rate limiting if deploying publicly

### Future Enhancement Preparation
- Structure code to easily add fine-tuned model later
- Keep original RuleEngine as fallback
- Log all AI suggestions and actual layouts for training data collection
- Design database schema to store user ratings of layouts (for future fine-tuning)

## Execution Order
Execute modifications in this exact order to avoid breaking dependencies:
1. Update pom.xml dependencies first
2. Create CORS configuration
3. Create AI package structure and classes
4. Modify controllers to REST
5. Modify services to use AI
6. Test backend with Postman
7. Create React project and configure Tailwind
8. Build React components
9. Integrate frontend with backend
10. Test complete application
11. Delete unused Thymeleaf files
12. Update documentation

## Validation Checklist
After implementation, verify:
- [ ] Backend starts without errors on port 8080
- [ ] Frontend starts without errors on port 3000
- [ ] CORS allows requests from frontend to backend
- [ ] GET /api/furniture returns furniture list
- [ ] POST /api/layout generates layout using AI
- [ ] AI suggestions are logged in console
- [ ] Layout displays furniture correctly in React
- [ ] Colors match warm & cozy theme
- [ ] Loading spinner appears during generation
- [ ] Error handling works for invalid inputs
- [ ] Budget constraints are enforced
- [ ] Furniture fits within room boundaries
- [ ] Application works end-to-end without errors

---

**Execute all modifications carefully, test each phase before moving to the next, and maintain backward compatibility during transition.**