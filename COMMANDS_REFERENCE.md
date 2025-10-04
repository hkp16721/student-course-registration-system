# Commands Reference Guide

Quick reference for all available commands in the Student Course Registration System.

## Setup Commands

### 1. Initialize Database Tables

Creates the required DynamoDB tables with indexes.

**Linux/Mac:**
```bash
./test-db-connection.sh
```

**Windows:**
```bash
test-db-connection.bat
```

**Manual:**
```bash
mvn exec:java -Dexec.mainClass="com.studentcourseregistration.app.util.DatabaseConnectionTest"
```

### 2. Load Sample Data

Populates tables with 8 students, 10 courses, and 21 registrations.

**Linux/Mac:**
```bash
./load-sample-data.sh
```

**Windows:**
```bash
load-sample-data.bat
```

**Manual:**
```bash
mvn exec:java -Dexec.mainClass="com.studentcourseregistration.app.util.SampleDataLoader"
```

### 3. Verify Data

Checks if data was loaded successfully and tests all indexes.

**Linux/Mac:**
```bash
./verify-data.sh
```

**Windows:**
```bash
verify-data.bat
```

**Manual:**
```bash
mvn exec:java -Dexec.mainClass="com.studentcourseregistration.app.util.VerifyData"
```

## Build Commands

### Clean Build
```bash
mvn clean
```

### Compile Only
```bash
mvn compile
```

### Run Tests
```bash
mvn test
```

### Package (Create JAR)
```bash
mvn package
```

### Full Build with Tests
```bash
mvn clean package
```

### Build Without Tests
```bash
mvn clean package -DskipTests
```

## Run Commands

### Run Main Application

**Option 1: Using JAR (Recommended)**
```bash
mvn clean package
java -jar target/student-course-registration-app-1.0-SNAPSHOT.jar
```

**Option 2: Using Maven Exec**
```bash
mvn exec:java -Dexec.mainClass="com.studentcourseregistration.app.App"
```

**Option 3: Direct Java**
```bash
mvn compile
java -cp target/classes:target/dependency/* com.studentcourseregistration.app.App
```

### Run Utilities

**Database Connection Test:**
```bash
mvn exec:java -Dexec.mainClass="com.studentcourseregistration.app.util.DatabaseConnectionTest"
```

**Sample Data Loader:**
```bash
mvn exec:java -Dexec.mainClass="com.studentcourseregistration.app.util.SampleDataLoader"
```

## Complete Workflows

### First Time Setup

```bash
# 1. Navigate to project
cd student-course-registration-app

# 2. Initialize database
./test-db-connection.sh

# 3. Load sample data
./load-sample-data.sh

# 4. Build application
mvn clean package

# 5. Run application
java -jar target/student-course-registration-app-1.0-SNAPSHOT.jar
```

### Daily Development

```bash
# Quick rebuild and run
mvn package -DskipTests && java -jar target/student-course-registration-app-1.0-SNAPSHOT.jar
```

### Reset Everything

```bash
# 1. Delete tables in NoSQL Workbench (or delete all items)

# 2. Reinitialize
./test-db-connection.sh

# 3. Reload data
./load-sample-data.sh

# 4. Rebuild and run
mvn clean package && java -jar target/student-course-registration-app-1.0-SNAPSHOT.jar
```

## Maven Lifecycle Phases

```bash
mvn validate      # Validate project structure
mvn compile       # Compile source code
mvn test          # Run unit tests
mvn package       # Create JAR file
mvn verify        # Run integration tests
mvn install       # Install to local repository
mvn deploy        # Deploy to remote repository
mvn clean         # Remove target directory
```

## Useful Maven Options

```bash
-q                # Quiet mode (less output)
-X                # Debug mode (more output)
-DskipTests       # Skip running tests
-Dmaven.test.skip=true  # Skip compiling and running tests
-U                # Force update of dependencies
-o                # Offline mode
-T 4              # Use 4 threads for parallel build
```

## Testing Commands

### Run All Tests
```bash
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=AppTest
```

### Run Specific Test Method
```bash
mvn test -Dtest=AppTest#testStudentCreation
```

### Run Tests with Coverage
```bash
mvn clean test jacoco:report
```

## Troubleshooting Commands

### Check Java Version
```bash
java -version
```

### Check Maven Version
```bash
mvn -version
```

### Verify DynamoDB Connection
```bash
curl http://localhost:8000
```

### List DynamoDB Tables
```bash
aws dynamodb list-tables --endpoint-url http://localhost:8000 --region us-east-1
```

### Clean Maven Cache
```bash
mvn dependency:purge-local-repository
```

### Force Dependency Update
```bash
mvn clean install -U
```

## Script Permissions (Linux/Mac)

If scripts aren't executable:

```bash
chmod +x test-db-connection.sh
chmod +x load-sample-data.sh
```

## Environment Variables

### Set Java Home (if needed)

**Linux/Mac:**
```bash
export JAVA_HOME=/path/to/java
export PATH=$JAVA_HOME/bin:$PATH
```

**Windows:**
```cmd
set JAVA_HOME=C:\Path\To\Java
set PATH=%JAVA_HOME%\bin;%PATH%
```

### Set Maven Options

```bash
export MAVEN_OPTS="-Xmx1024m -XX:MaxPermSize=256m"
```

## Quick Reference Table

| Task | Command |
|------|---------|
| Initialize DB | `./test-db-connection.sh` |
| Load Data | `./load-sample-data.sh` |
| Build | `mvn clean package` |
| Run App | `java -jar target/student-course-registration-app-1.0-SNAPSHOT.jar` |
| Run Tests | `mvn test` |
| Clean | `mvn clean` |
| Quick Rebuild | `mvn package -DskipTests` |

## Sample Login Credentials

After loading sample data, use any of these:

| Username | Password | Major | Year |
|----------|----------|-------|------|
| johndoe | password123 | Computer Science | 3 |
| janesmith | password123 | Mathematics | 2 |
| alicew | password123 | Computer Science | 1 |
| bobjohnson | password123 | Physics | 4 |

## Common Issues and Solutions

### Issue: "Port 8000 already in use"
```bash
# Find process using port 8000
lsof -i :8000  # Mac/Linux
netstat -ano | findstr :8000  # Windows

# Kill the process or use different port
```

### Issue: "Cannot find main manifest attribute"
```bash
# Rebuild with shade plugin
mvn clean package
```

### Issue: "Connection refused to localhost:8000"
```bash
# Check NoSQL Workbench is running
# Verify DynamoDB Local is started
# Check firewall settings
```

### Issue: "Tests failing"
```bash
# Skip tests during build
mvn package -DskipTests
```

### Issue: "Out of memory"
```bash
# Increase Maven memory
export MAVEN_OPTS="-Xmx2048m"
mvn clean package
```

## IDE Integration

### IntelliJ IDEA
```bash
# Import as Maven project
File → Open → Select pom.xml

# Run configuration
Main class: com.studentcourseregistration.app.App
```

### Eclipse
```bash
# Import Maven project
File → Import → Maven → Existing Maven Projects

# Run configuration
Run As → Java Application
Main class: com.studentcourseregistration.app.App
```

### VS Code
```bash
# Open folder
# Install Java Extension Pack
# Run from Run menu or F5
```

## Documentation Files

- `README.md` - Main project documentation
- `QUICKSTART.md` - Quick start guide
- `DYNAMODB_SETUP.md` - Detailed DynamoDB setup
- `SAMPLE_DATA.md` - Sample data documentation
- `ARCHITECTURE.md` - System architecture
- `IMPLEMENTATION_SUMMARY.md` - Implementation details
- `COMMANDS_REFERENCE.md` - This file

## Getting Help

1. Check documentation files above
2. Review error messages in console
3. Verify NoSQL Workbench is running
4. Check Java and Maven versions
5. Review logs in target/surefire-reports/

## Next Steps

After setup:
1. ✓ Database initialized
2. ✓ Sample data loaded
3. ✓ Application built
4. → Run the application
5. → Login with sample credentials
6. → Explore features
7. → View data in NoSQL Workbench
