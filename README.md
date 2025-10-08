# 🏠 Interior Design Application

A rule-based room planning system built with Spring Boot that automatically generates furniture layouts based on room dimensions and budget constraints.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.2-green)
![Maven](https://img.shields.io/badge/Maven-3.x-blue)
![License](https://img.shields.io/badge/License-MIT-yellow)

---

## 🚀 Quick Start

### Prerequisites

Ensure you have the following installed:

- **Java JDK 17+** - [Download here](https://adoptium.net/)
- **Apache Maven 3.6+** - [Download here](https://maven.apache.org/download.cgi)

Verify installation:
```powershell
java -version    # Should show Java 17 or higher
mvn -version     # Should show Maven 3.6 or higher
```

---

## 📥 Installation

### 1. Clone the Repository
```powershell
git clone https://github.com/perfectking321/interior-design-app.git
cd interior-design-app
```

### 2. Build the Project
```powershell
mvn clean install
```

This will:
- Download all dependencies
- Compile the source code
- Create an executable JAR file in the `target/` directory

---

## ▶️ Running the Application

### Option 1: Using Maven (Recommended)
```powershell
mvn spring-boot:run
```

### Option 2: Using Java JAR
```powershell
java -jar target/interior-design-app-0.0.1-SNAPSHOT.jar
```

### Option 3: Using VS Code
1. Open `src/main/java/com/interiordesign/InteriorDesignApplication.java`
2. Click the **Run** button above the `main()` method
3. The application will start automatically

---

## 🌐 Access the Application

Once the application starts successfully, you'll see:
```
Started InteriorDesignApplication in X.XXX seconds
```

Open your web browser and navigate to:

- **Main Application**: [http://localhost:8080](http://localhost:8080)
- **H2 Database Console**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

### H2 Console Credentials
- **JDBC URL**: `jdbc:h2:mem:interiordb`
- **Username**: `sa`
- **Password**: *(leave empty)*

---

## 📖 How to Use

1. **Enter Room Details**
   - Room Length: 3-15 meters
   - Room Width: 3-15 meters
   - Budget: $500-$10,000

2. **Generate Layout**
   - Click "Generate Layout" button
   - View the automatically generated furniture arrangement

3. **Review Results**
   - See visual layout with furniture positions
   - Check total cost and budget utilization
   - Review any warnings or suggestions

---

## 🛠 Project Structure

```
interior-design-app/
├── src/
│   ├── main/
│   │   ├── java/com/interiordesign/
│   │   │   ├── InteriorDesignApplication.java  # Main application
│   │   │   ├── controller/                     # Web controllers
│   │   │   ├── service/                        # Business logic
│   │   │   ├── dao/                            # Data access
│   │   │   ├── model/                          # Data models
│   │   │   └── config/                         # Configuration
│   │   └── resources/
│   │       ├── application.properties          # App config
│   │       ├── schema.sql                      # Database schema
│   │       ├── data.sql                        # Sample data
│   │       ├── static/                         # CSS files
│   │       └── templates/                      # HTML templates
├── pom.xml                                     # Maven config
└── README.md                                   # This file
```

---

## ⚙️ Configuration

### Default Configuration
The application runs with these default settings:

| Setting | Value |
|---------|-------|
| Server Port | 8080 |
| Database | H2 (In-Memory) |
| Database URL | jdbc:h2:mem:interiordb |
| H2 Console | Enabled |

### Custom Port
To change the port, edit `src/main/resources/application.properties`:
```properties
server.port=9090
```

Or run with command line argument:
```powershell
java -jar target/interior-design-app-0.0.1-SNAPSHOT.jar --server.port=9090
```

---

## 🧪 Testing

### Manual Testing
1. Start the application
2. Navigate to http://localhost:8080
3. Try different room dimensions and budgets:
   - Small room (3m x 3m) with low budget ($800)
   - Medium room (6m x 5m) with medium budget ($2,500)
   - Large room (10m x 8m) with high budget ($5,000)

### Run Unit Tests
```powershell
mvn test
```

---

## 🐛 Troubleshooting

### Port Already in Use
**Error**: `Port 8080 is already in use`

**Solution 1**: Kill the process using port 8080
```powershell
# Find the process
netstat -ano | findstr :8080

# Kill it (replace <PID> with actual process ID)
taskkill /PID <PID> /F
```

**Solution 2**: Use a different port
```powershell
java -jar target/interior-design-app-0.0.1-SNAPSHOT.jar --server.port=9090
```

### Build Fails
**Error**: `The import org.springframework cannot be resolved`

**Solution**: Clean and rebuild
```powershell
mvn clean install -U
```

### Java Version Mismatch
**Error**: `invalid target release: 17`

**Solution**: Ensure Java 17+ is installed and set as default
```powershell
java -version  # Must show version 17 or higher
```

### Application Won't Start
**Solution**: Run with debug logging
```powershell
java -jar target/interior-design-app-0.0.1-SNAPSHOT.jar --debug
```

---

## 🎯 Features

- ✅ **Automated Layout Generation** - Rule-based furniture placement algorithm
- ✅ **Budget Management** - Furniture selection within budget constraints
- ✅ **Input Validation** - Validates room dimensions and budget
- ✅ **Visual Display** - Graphical representation of room layout
- ✅ **Furniture Database** - Pre-loaded with common furniture items
- ✅ **Responsive Design** - Works on desktop and mobile browsers
- ✅ **Error Handling** - User-friendly error messages

---

## 📦 Technologies Used

- **Backend**: Spring Boot 3.2.2, Spring MVC, Spring JDBC
- **Frontend**: Thymeleaf, HTML5, CSS3
- **Database**: H2 (In-Memory)
- **Build Tool**: Maven 3.x
- **Java Version**: 17

---

## 📚 Documentation

For detailed documentation, see [PROJECT_DOCUMENTATION.md](PROJECT_DOCUMENTATION.md)

Topics covered:
- Complete architecture overview
- Database schema details
- API endpoints documentation
- Business logic explanation
- Deployment guide
- Contributing guidelines

---

## 🚢 Deployment

### Docker Deployment

1. **Create Dockerfile** (if not exists):
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/interior-design-app-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

2. **Build and Run**:
```powershell
docker build -t interior-design-app .
docker run -p 8080:8080 interior-design-app
```

### Production Deployment

1. **Build for production**:
```powershell
mvn clean package -DskipTests
```

2. **Copy JAR to server**:
```powershell
scp target/interior-design-app-0.0.1-SNAPSHOT.jar user@server:/opt/app/
```

3. **Run on server**:
```bash
java -jar /opt/app/interior-design-app-0.0.1-SNAPSHOT.jar
```

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Author

**perfectking321**
- GitHub: [@perfectking321](https://github.com/perfectking321)
- Repository: [interior-design-app](https://github.com/perfectking321/interior-design-app)

---

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- H2 Database for lightweight database solution
- Thymeleaf for the powerful template engine
- Open source community

---

## 📞 Support

If you encounter any issues or have questions:

1. Check the [Troubleshooting](#-troubleshooting) section
2. Review [PROJECT_DOCUMENTATION.md](PROJECT_DOCUMENTATION.md)
3. Open an issue on [GitHub](https://github.com/perfectking321/interior-design-app/issues)

---

## 🗺 Roadmap

- [ ] User authentication and profiles
- [ ] Save and load room designs
- [ ] Custom furniture addition
- [ ] 3D visualization
- [ ] Export to PDF
- [ ] Mobile application
- [ ] AI-powered recommendations

---

**Made with ❤️ using Spring Boot**

*Last Updated: October 8, 2025*
