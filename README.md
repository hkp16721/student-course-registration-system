# Student Course Registration System

A Java-based console application for managing student course registrations. This system allows students to register for courses, view available courses, manage their schedules, and handle course enrollment.

## Features

### Implemented Features

- **Student Management**: Create student accounts with secure password hashing
- **Course Management**: Create and manage course offerings
- **Registration System**: Register students for courses with validation
- **Schedule Management**: View student schedules and detect conflicts
- **Course Search**: Search courses by name, code, instructor, or department
- **Authentication**: Secure login system with password verification
- **Data Validation**: Prevent duplicate registrations and enforce capacity limits

### Core Functionality

- Student registration and authentication
- Course catalog browsing and searching
- Course enrollment with capacity and conflict checking
- Personal schedule viewing
- Course dropping functionality
- Profile management

## Technology Stack

- **Java 17**: Core programming language
- **Maven**: Build and dependency management
- **JUnit 5**: Unit testing framework
- **BCrypt**: Password hashing and security
- **DynamoDB SDK**: Database integration (with in-memory fallback)

## Project Structure

```
src/
├── main/java/com/studentcourseregistration/app/
│   ├── App.java                          # Main application entry point
│   ├── model/                            # Data models
│   │   ├── Student.java                  # Student entity
│   │   ├── Course.java                   # Course entity
│   │   └── Registration.java             # Registration entity
│   ├── service/                          # Business logic
│   │   ├── SimpleStudentService.java     # Student operations
│   │   ├── SimpleCourseService.java      # Course operations
│   │   ├── SimpleRegistrationService.java # Registration operations
│   │   ├── StudentService.java           # DynamoDB student service
│   │   ├── CourseService.java            # DynamoDB course service
│   │   └── RegistrationService.java      # DynamoDB registration service
│   ├── repository/                       # Data access layer
│   │   ├── InMemoryStudentRepository.java
│   │   ├── InMemoryCourseRepository.java
│   │   └── InMemoryRegistrationRepository.java
│   └── config/
│       └── DynamoDbConfig.java           # Database configuration
└── test/java/com/studentcourseregistration/app/
    └── AppTest.java                      # Unit tests
```

## Building and Running

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Build the Project

```bash
# Navigate to project directory
cd student-course-registration-app

# Clean and compile
mvn clean compile

# Run tests
mvn test

# Build JAR file
mvn package
```

### Run the Application

#### Option 1: Using Maven (may have input issues)

```bash
mvn exec:java -Dexec.mainClass="com.studentcourseregistration.app.App"
```

#### Option 2: Using Java directly (recommended)

```bash
# Build the JAR first
mvn package

# Run with dependencies on classpath
java -cp "target/classes:target/dependency/*" com.studentcourseregistration.app.App
```

#### Option 3: Create a fat JAR (easiest)

Add this plugin to your `pom.xml` and rebuild:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-shade-plugin</artifactId>
    <version>3.4.1</version>
    <executions>
        <execution>
            <phase>package</phase>
            <goals>
                <goal>shade</goal>
            </goals>
            <configuration>
                <transformers>
                    <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                        <mainClass>com.studentcourseregistration.app.App</mainClass>
                    </transformer>
                </transformers>
            </configuration>
        </execution>
    </executions>
</plugin>
```

Then run:

```bash
mvn package
java -jar target/student-course-registration-app-1.0-SNAPSHOT.jar
```

## Usage

### Sample Data

The application initializes with sample data:

- **Sample Student**: Username: `hemant`, Password: `password123`
- **Sample Courses**: CS101, MATH201, ENG101

### Application Menu

1. **Login Menu**:

   - Login with existing credentials
   - Register new student account
   - Exit application

2. **Main Menu** (after login):
   - View available courses
   - Search courses
   - Register for courses
   - View enrolled courses
   - Drop courses
   - View profile
   - Logout

### Example Usage Flow

1. Start the application
2. Login with `hemant` / `password123` or register a new account
3. View available courses to see what's offered
4. Register for a course using its course code (e.g., "CS101")
5. View your enrolled courses to confirm registration
6. Search for courses by keyword
7. Drop courses if needed

## Testing

The project includes comprehensive unit tests covering:

- Student creation and authentication
- Course management operations
- Registration validation
- Duplicate prevention
- Search functionality

Run tests with:

```bash
mvn test
```

## Database Configuration

### In-Memory Mode (Default)

The application uses in-memory repositories by default, which is perfect for development and testing. No external database setup required.

### DynamoDB Mode

The application now supports DynamoDB Local for persistent storage!

#### Quick Setup with NoSQL Workbench

1. **Start NoSQL Workbench** with DynamoDB Local on port 8000

2. **Test Connection and Initialize Tables**:
   ```bash
   # Linux/Mac
   ./test-db-connection.sh
   
   # Windows
   test-db-connection.bat
   ```

3. **Verify Setup**: The script will:
   - Test connection to DynamoDB Local
   - Create required tables (Students, Courses, Registrations)
   - Create necessary indexes for efficient queries
   - Display current tables

#### Manual Setup

```bash
# Build the project
mvn clean package

# Run the database connection test
mvn exec:java -Dexec.mainClass="com.studentcourseregistration.app.util.DatabaseConnectionTest"
```

#### Configuration Details

- **Endpoint**: `http://localhost:8000`
- **Region**: `US_EAST_1`
- **Credentials**: Dummy credentials (for local development)

For detailed setup instructions, troubleshooting, and migration guide, see [DYNAMODB_SETUP.md](DYNAMODB_SETUP.md)

## Architecture

### Design Patterns

- **Repository Pattern**: Separates data access logic
- **Service Layer Pattern**: Encapsulates business logic
- **Model-View-Controller**: Separates concerns

### Key Components

- **Models**: Define data structures (Student, Course, Registration)
- **Repositories**: Handle data persistence (in-memory or DynamoDB)
- **Services**: Implement business rules and validation
- **App**: Provides console-based user interface

## Security Features

- Password hashing using BCrypt
- Input validation and sanitization
- Duplicate prevention (username/email)
- Session management (login/logout)

## Future Enhancements

- Web-based user interface
- Email notifications
- Advanced scheduling algorithms
- Waitlist management
- Grade management
- Reporting and analytics
- REST API endpoints
- Database migration tools

## Contributing

1. Fork the repository
2. Create a feature branch
3. Add tests for new functionality
4. Ensure all tests pass
5. Submit a pull request

## License

This project is for educational purposes.
