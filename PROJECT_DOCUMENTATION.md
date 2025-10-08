# Interior Design Application - Project Documentation

## 📋 Table of Contents
1. [Project Overview](#project-overview)
2. [Features](#features)
3. [Technology Stack](#technology-stack)
4. [Project Structure](#project-structure)
5. [Prerequisites](#prerequisites)
6. [Installation & Setup](#installation--setup)
7. [Running the Application](#running-the-application)
8. [Application Architecture](#application-architecture)
9. [Database Schema](#database-schema)
10. [API Endpoints](#api-endpoints)
11. [Business Logic](#business-logic)
12. [Configuration](#configuration)
13. [Testing](#testing)
14. [Deployment](#deployment)
15. [Troubleshooting](#troubleshooting)
16. [Future Enhancements](#future-enhancements)
17. [Contributing](#contributing)
18. [License](#license)

---

## 🏠 Project Overview

**Interior Design Application** is a rule-based room planning system built with Spring Boot. The application helps users design room layouts by automatically placing furniture based on room dimensions and budget constraints.

### Key Objectives
- Provide automated furniture layout generation
- Apply design rules for optimal furniture placement
- Work within user-defined budget constraints
- Validate room dimensions and inputs
- Visualize furniture arrangements

### Project Details
- **Group ID**: `com.interiordesign`
- **Artifact ID**: `interior-design-app`
- **Version**: `0.0.1-SNAPSHOT`
- **Name**: Simplified Interior Design App
- **Description**: Rule-based room planning system

---

## ✨ Features

### Core Features
1. **Room Configuration**
   - Define room dimensions (length and width in meters)
   - Set budget constraints
   - Input validation for realistic room sizes

2. **Automated Layout Generation**
   - Rule-based furniture placement algorithm
   - Considers room dimensions and available space
   - Budget-aware furniture selection

3. **Furniture Database**
   - Pre-populated furniture catalog
   - Categories: Sofa, Coffee Table, TV Stand, Bookshelf, Side Table, Armchair
   - Dimensions and pricing information

4. **Visual Layout Display**
   - Graphical representation of room layout
   - Scaled furniture positioning
   - Cost breakdown and summary

5. **Validation & Error Handling**
   - Input validation with user-friendly error messages
   - Custom error pages
   - Global exception handling

---

## 🛠 Technology Stack

### Backend
- **Java**: 17
- **Spring Boot**: 3.2.2
- **Spring Framework Components**:
  - Spring Web MVC
  - Spring JDBC
  - Spring Boot Validation

### Frontend
- **Thymeleaf**: Template engine for server-side rendering
- **HTML5 & CSS3**: User interface
- **Bootstrap** (if applicable): Responsive design

### Database
- **H2 Database**: In-memory database (embedded)
- **JDBC**: Database connectivity

### Build Tool
- **Apache Maven**: 3.x
- **Maven Compiler Plugin**: 3.11.0

### Development Tools
- **VS Code**: IDE
- **Git**: Version control

---

## 📁 Project Structure

```
interior-design-app/
├── pom.xml                          # Maven configuration
├── PROJECT_DOCUMENTATION.md         # This file
├── .gitattributes                   # Git configuration
├── .vscode/                         # VS Code settings
│   └── settings.json
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── interiordesign/
│       │           ├── InteriorDesignApplication.java  # Main application class
│       │           ├── config/
│       │           │   └── DatabaseConfig.java         # Database configuration
│       │           ├── controller/
│       │           │   ├── HomeController.java         # Main web controller
│       │           │   ├── CustomErrorController.java  # Error handling
│       │           │   └── GlobalExceptionHandler.java # Global exception handler
│       │           ├── dao/
│       │           │   └── FurnitureDAO.java           # Data access object
│       │           ├── model/
│       │           │   ├── Furniture.java              # Furniture entity
│       │           │   ├── FurniturePosition.java      # Furniture position
│       │           │   ├── Room.java                   # Room input model
│       │           │   └── RoomLayout.java             # Layout result model
│       │           └── service/
│       │               ├── LayoutService.java          # Layout orchestration
│       │               └── RuleEngine.java             # Layout generation logic
│       └── resources/
│           ├── application.properties    # Application configuration
│           ├── schema.sql               # Database schema
│           ├── data.sql                 # Sample data
│           ├── static/
│           │   └── style.css            # Custom CSS
│           └── templates/
│               ├── room-form.html       # Input form
│               ├── layout-result.html   # Layout display
│               └── error-page.html      # Error page
└── target/                              # Compiled artifacts (generated)
    └── interior-design-app-0.0.1-SNAPSHOT.jar
```

---

## 📋 Prerequisites

Before running the application, ensure you have the following installed:

### Required
- **Java Development Kit (JDK)**: Version 17 or higher
  - Download: [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://adoptium.net/)
- **Apache Maven**: Version 3.6 or higher
  - Download: [Maven](https://maven.apache.org/download.cgi)

### Optional
- **Git**: For version control
- **VS Code**: With Java Extension Pack
- **Web Browser**: Chrome, Firefox, Edge, or Safari

### Verify Installation
```powershell
# Check Java version
java -version

# Check Maven version
mvn -version

# Expected output: Java 17+ and Maven 3.6+
```

---

## 🚀 Installation & Setup

### Step 1: Clone the Repository
```powershell
git clone https://github.com/perfectking321/interior-design-app.git
cd interior-design-app
```

### Step 2: Build the Project
```powershell
# Clean and build the project
mvn clean install

# This will:
# - Download all dependencies
# - Compile source code
# - Run tests (if any)
# - Package into JAR file
```

### Step 3: Verify Build
```powershell
# Check if JAR file is created
ls target/interior-design-app-0.0.1-SNAPSHOT.jar
```

---

## ▶️ Running the Application

### Method 1: Using Maven (Recommended)
```powershell
mvn spring-boot:run
```

### Method 2: Using Java JAR
```powershell
java -jar target/interior-design-app-0.0.1-SNAPSHOT.jar
```

### Method 3: Using VS Code
1. Open `InteriorDesignApplication.java`
2. Click the **Run** or **Debug** button above the `main` method
3. Application will start automatically

### Access the Application
Once started, access the application at:
- **URL**: http://localhost:8080
- **H2 Console**: http://localhost:8080/h2-console

### Verify Application is Running
Look for this message in the console:
```
Started InteriorDesignApplication in X.XXX seconds
```

---

## 🏗 Application Architecture

### Architecture Pattern
The application follows a **Layered Architecture** pattern:

```
┌─────────────────────────────────────┐
│         Presentation Layer          │
│    (Controllers + Thymeleaf)        │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│         Service Layer               │
│   (Business Logic + Rules)          │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│         Data Access Layer           │
│         (DAO + JDBC)                │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│         Database Layer              │
│         (H2 In-Memory)              │
└─────────────────────────────────────┘
```

### Component Descriptions

#### 1. Presentation Layer (Controllers)
**HomeController.java**
- Handles HTTP requests
- Displays room input form
- Processes layout generation requests
- Renders layout results

**CustomErrorController.java**
- Custom error page handling
- Graceful error display

**GlobalExceptionHandler.java**
- Centralized exception handling
- Consistent error responses

#### 2. Service Layer
**LayoutService.java**
- Orchestrates layout generation
- Coordinates between DAO and RuleEngine
- Business logic coordination

**RuleEngine.java**
- Core layout generation algorithm
- Applies design rules
- Furniture placement logic
- Budget constraint management

#### 3. Data Access Layer
**FurnitureDAO.java**
- JDBC-based data access
- CRUD operations for furniture
- Database query execution

#### 4. Model Layer
**Room.java**
- User input model
- Validation constraints
- Room dimensions and budget

**Furniture.java**
- Furniture entity representation
- Properties: name, dimensions, price, category

**FurniturePosition.java**
- Furniture placement details
- X, Y coordinates
- Associated furniture reference

**RoomLayout.java**
- Complete layout representation
- List of furniture positions
- Warnings and messages
- Total cost calculation

#### 5. Configuration
**DatabaseConfig.java**
- Database connection configuration
- DataSource bean setup
- JDBC template configuration

---

## 🗄 Database Schema

### Furniture Table
```sql
CREATE TABLE furniture (
  id IDENTITY PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  width DOUBLE NOT NULL,
  depth DOUBLE NOT NULL,
  price INT NOT NULL,
  category VARCHAR(50) NOT NULL
);
```

### Sample Data
| ID | Name         | Width | Depth | Price | Category   |
|----|--------------|-------|-------|-------|------------|
| 1  | Sofa         | 2.0   | 0.9   | 800   | sofa       |
| 2  | Coffee Table | 1.2   | 0.6   | 200   | coffee     |
| 3  | TV Stand     | 1.5   | 0.4   | 300   | tvstand    |
| 4  | Bookshelf    | 0.8   | 0.3   | 150   | bookshelf  |
| 5  | Side Table   | 0.5   | 0.5   | 100   | sidetable  |
| 6  | Armchair     | 0.8   | 0.8   | 400   | armchair   |

---

## 🔌 API Endpoints

### 1. Home Page (Form)
- **URL**: `/`
- **Method**: `GET`
- **Description**: Displays room input form
- **Response**: `room-form.html`

### 2. Generate Layout
- **URL**: `/layout`
- **Method**: `POST`
- **Description**: Processes room data and generates layout
- **Request Body**: Form data with room parameters
  - `length` (Double): 3-15 meters
  - `width` (Double): 3-15 meters
  - `budget` (Integer): $500-$10,000
- **Response**: `layout-result.html` with generated layout
- **Validation Rules**:
  - Length: 3-15 meters
  - Width: 3-15 meters
  - Budget: $500-$10,000

### 3. H2 Console (Development)
- **URL**: `/h2-console`
- **Method**: `GET`
- **Description**: Database management console
- **Credentials**:
  - JDBC URL: `jdbc:h2:mem:interiordb`
  - Username: `sa`
  - Password: (empty)

### 4. Error Page
- **URL**: `/error`
- **Method**: `GET`
- **Description**: Custom error page
- **Response**: `error-page.html`

---

## 🧠 Business Logic

### Rule Engine Algorithm

The `RuleEngine` class implements the core furniture placement algorithm:

#### 1. **Furniture Selection**
- Filters furniture based on budget
- Prioritizes essential items (sofa, coffee table, TV stand)
- Considers room dimensions

#### 2. **Placement Rules**
- **Wall Clearance**: Minimum 0.5m from walls
- **Spacing**: Minimum gap between furniture items
- **Focal Point**: TV stand typically on longest wall
- **Conversation Area**: Sofa and chairs facing each other
- **Traffic Flow**: Maintains walkways

#### 3. **Budget Management**
- Tracks cumulative cost
- Stops adding furniture when budget exceeded
- Prioritizes by category importance

#### 4. **Validation**
- Checks for furniture overlap
- Ensures items fit within room boundaries
- Validates spacing requirements

#### 5. **Output Generation**
- Creates `FurniturePosition` objects
- Calculates total cost
- Generates warnings if needed
- Returns complete `RoomLayout`

---

## ⚙️ Configuration

### application.properties

```properties
# Database Configuration
spring.datasource.url=jdbc:h2:mem:interiordb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.datasource.platform=h2

# Database Initialization
spring.sql.init.mode=always
spring.sql.init.platform=h2
spring.sql.init.schema-locations=classpath:schema.sql
spring.sql.init.data-locations=classpath:data.sql

# Thymeleaf Configuration
spring.thymeleaf.cache=false

# Server Configuration
server.port=8080

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

### Customization Options

#### Change Server Port
```properties
server.port=9090
```

#### Disable H2 Console (Production)
```properties
spring.h2.console.enabled=false
```

#### Use File-based Database
```properties
spring.datasource.url=jdbc:h2:file:./data/interiordb
```

---

## 🧪 Testing

### Manual Testing

#### Test Case 1: Small Room with Low Budget
```
Input:
- Length: 4m
- Width: 3m
- Budget: $800

Expected:
- Minimal furniture (sofa only or sofa + coffee table)
- Warning about limited budget
```

#### Test Case 2: Large Room with High Budget
```
Input:
- Length: 10m
- Width: 8m
- Budget: $5,000

Expected:
- Full furniture set
- Well-distributed layout
- Total cost under budget
```

#### Test Case 3: Invalid Inputs
```
Input:
- Length: 2m (too small)
- Width: 20m (too large)
- Budget: $100 (too low)

Expected:
- Validation errors
- Form redisplay with error messages
```

### Automated Testing

Create unit tests in `src/test/java`:

```java
@SpringBootTest
class LayoutServiceTest {
    @Autowired
    private LayoutService layoutService;
    
    @Test
    void testLayoutGeneration() {
        Room room = new Room(5.0, 4.0, 2000);
        RoomLayout layout = layoutService.createLayout(room);
        
        assertNotNull(layout);
        assertTrue(layout.getTotalCost() <= 2000);
        assertFalse(layout.getPositions().isEmpty());
    }
}
```

Run tests:
```powershell
mvn test
```

---

## 🚢 Deployment

### Local Deployment
Already covered in [Running the Application](#running-the-application)

### Production Deployment

#### 1. Package Application
```powershell
mvn clean package -DskipTests
```

#### 2. Deploy to Server
```powershell
# Copy JAR to server
scp target/interior-design-app-0.0.1-SNAPSHOT.jar user@server:/opt/app/

# Run on server
java -jar /opt/app/interior-design-app-0.0.1-SNAPSHOT.jar
```

#### 3. Run as Service (Linux)
Create systemd service file: `/etc/systemd/system/interior-design.service`
```ini
[Unit]
Description=Interior Design Application
After=network.target

[Service]
Type=simple
User=appuser
ExecStart=/usr/bin/java -jar /opt/app/interior-design-app-0.0.1-SNAPSHOT.jar
Restart=always

[Install]
WantedBy=multi-user.target
```

Enable and start:
```bash
sudo systemctl enable interior-design
sudo systemctl start interior-design
```

#### 4. Docker Deployment

Create `Dockerfile`:
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/interior-design-app-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

Build and run:
```powershell
docker build -t interior-design-app .
docker run -p 8080:8080 interior-design-app
```

---

## 🔧 Troubleshooting

### Common Issues

#### 1. "The import org.springframework cannot be resolved"
**Solution**: Run Maven install to download dependencies
```powershell
mvn clean install
```

#### 2. "Port 8080 already in use"
**Solution**: Change port in `application.properties` or kill existing process
```powershell
# Find process using port 8080
netstat -ano | findstr :8080

# Kill process (replace PID)
taskkill /PID <PID> /F

# Or change port in application.properties
server.port=9090
```

#### 3. "Java version mismatch"
**Solution**: Ensure Java 17 is installed and set as default
```powershell
java -version
# Should show Java 17
```

#### 4. "H2 Console not accessible"
**Solution**: Verify H2 console is enabled
```properties
spring.h2.console.enabled=true
```

#### 5. "Application fails to start"
**Solution**: Check logs for specific error
```powershell
# Run with debug logging
java -jar target/interior-design-app-0.0.1-SNAPSHOT.jar --debug
```

#### 6. "Database initialization failed"
**Solution**: Check schema.sql and data.sql syntax
```powershell
# Verify SQL files exist
ls src/main/resources/*.sql
```

---

## 🚀 Future Enhancements

### Planned Features
1. **User Authentication**
   - User registration and login
   - Save multiple room designs
   - User profile management

2. **Advanced Layout Options**
   - Multiple room types (bedroom, kitchen, office)
   - Custom furniture addition
   - 3D visualization

3. **AI-Powered Recommendations**
   - Machine learning for optimal layouts
   - Style preference learning
   - Personalized suggestions

4. **Export Functionality**
   - PDF report generation
   - Shopping list creation
   - Share designs via email

5. **Mobile Application**
   - React Native mobile app
   - AR room visualization
   - Camera-based room measurement

6. **Database Enhancement**
   - PostgreSQL/MySQL for production
   - Furniture image storage
   - User design history

7. **API Enhancement**
   - RESTful API for mobile apps
   - GraphQL support
   - API documentation (Swagger)

8. **Testing**
   - Comprehensive unit tests
   - Integration tests
   - End-to-end tests with Selenium

---

## 🤝 Contributing

### How to Contribute

1. **Fork the Repository**
   ```powershell
   git clone https://github.com/perfectking321/interior-design-app.git
   ```

2. **Create Feature Branch**
   ```powershell
   git checkout -b feature/your-feature-name
   ```

3. **Make Changes**
   - Write clean, documented code
   - Follow existing code style
   - Add tests for new features

4. **Commit Changes**
   ```powershell
   git add .
   git commit -m "Add: Description of your changes"
   ```

5. **Push to Branch**
   ```powershell
   git push origin feature/your-feature-name
   ```

6. **Create Pull Request**
   - Describe your changes
   - Reference any related issues
   - Wait for review

### Code Style Guidelines
- Use proper indentation (4 spaces)
- Follow Java naming conventions
- Add JavaDoc comments for public methods
- Keep methods focused and concise
- Write meaningful variable names

---

## 📄 License

This project is licensed under the MIT License.

```
MIT License

Copyright (c) 2025 Interior Design App

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---

## 📞 Contact & Support

### Project Maintainer
- **GitHub**: [@perfectking321](https://github.com/perfectking321)
- **Repository**: [interior-design-app](https://github.com/perfectking321/interior-design-app)

### Getting Help
- Open an issue on GitHub
- Check existing documentation
- Review troubleshooting section

### Reporting Bugs
When reporting bugs, please include:
- Operating system and version
- Java version
- Maven version
- Complete error message
- Steps to reproduce

---

## 📚 Additional Resources

### Spring Boot Documentation
- [Spring Boot Reference](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring MVC Guide](https://spring.io/guides/gs/serving-web-content/)
- [Thymeleaf Documentation](https://www.thymeleaf.org/documentation.html)

### Java Resources
- [Java 17 Documentation](https://docs.oracle.com/en/java/javase/17/)
- [Maven Documentation](https://maven.apache.org/guides/)

### Development Tools
- [VS Code Java Extension Pack](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack)
- [H2 Database Documentation](https://www.h2database.com/html/main.html)

---

## 🎉 Acknowledgments

- Spring Boot team for the excellent framework
- H2 Database for lightweight database solution
- Thymeleaf team for template engine
- Open source community

---

**Document Version**: 1.0  
**Last Updated**: October 8, 2025  
**Status**: Active Development

---

*This documentation is maintained by the project team. For updates or corrections, please submit a pull request.*
