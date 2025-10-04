# DynamoDB Local Setup Guide

## Overview
This application now supports DynamoDB as a persistent database backend. You can run it with a local DynamoDB instance (NoSQL Workbench) for development and testing.

## Prerequisites
- NoSQL Workbench running on port 8000
- Java 17 or higher
- Maven 3.6 or higher

## Configuration

### DynamoDB Connection
The application is configured to connect to DynamoDB Local at `http://localhost:8000`.

Configuration file: `src/main/java/com/studentcourseregistration/app/config/DynamoDbConfig.java`

```java
// Local DynamoDB endpoint
endpointOverride(URI.create("http://localhost:8000"))
region(Region.US_EAST_1)
credentialsProvider(StaticCredentialsProvider.create(
    AwsBasicCredentials.create("dummy", "dummy")))
```

## Database Schema

### Tables Created

1. **Students**
   - Partition Key: `studentId` (String)
   - Global Secondary Indexes:
     - `username-index` on `username`
     - `email-index` on `email`

2. **Courses**
   - Partition Key: `courseId` (String)
   - Global Secondary Indexes:
     - `course-code-index` on `courseCode`
     - `department-index` on `department`

3. **Registrations**
   - Partition Key: `registrationId` (String)
   - Global Secondary Indexes:
     - `student-index` on `studentId`
     - `course-index` on `courseId`

## Setup Instructions

### Step 1: Start NoSQL Workbench
Ensure NoSQL Workbench is running with DynamoDB Local on port 8000.

### Step 2: Test Connection and Initialize Tables

```bash
# Build the project
mvn clean package

# Run the database connection test
mvn exec:java -Dexec.mainClass="com.studentcourseregistration.app.util.DatabaseConnectionTest"
```

This will:
- Test the connection to DynamoDB Local
- Create the required tables with indexes
- Display the current tables in the database

Expected output:
```
Testing DynamoDB connection to localhost:8000...

Attempting to connect to DynamoDB...
✓ Successfully connected to DynamoDB!

==================================================
Initializing application tables...
==================================================

Students table created successfully
Courses table created successfully
Registrations table created successfully
DynamoDB tables initialized successfully!

==================================================
Current tables in database:
==================================================
  ✓ Students
  ✓ Courses
  ✓ Registrations

✓ Database setup completed successfully!
```

### Step 3: Update Application to Use DynamoDB

The application currently uses in-memory repositories. To switch to DynamoDB:

1. Open `src/main/java/com/studentcourseregistration/app/App.java`
2. Replace the service implementations with DynamoDB-backed versions

Example changes needed in `App.java`:
```java
// Replace in-memory repositories with DynamoDB repositories
private static final DynamoDbStudentRepository studentRepo = new DynamoDbStudentRepository();
private static final DynamoDbCourseRepository courseRepo = new DynamoDbCourseRepository();
private static final DynamoDbRegistrationRepository registrationRepo = new DynamoDbRegistrationRepository();
```

## Repository Classes

### DynamoDB Repositories
- `DynamoDbStudentRepository` - Student data access
- `DynamoDbCourseRepository` - Course data access
- `DynamoDbRegistrationRepository` - Registration data access

### Available Methods

#### DynamoDbStudentRepository
```java
Student save(Student student)
Optional<Student> findById(String studentId)
Optional<Student> findByUsername(String username)
Optional<Student> findByEmail(String email)
List<Student> findAll()
void delete(String studentId)
```

#### DynamoDbCourseRepository
```java
Course save(Course course)
Optional<Course> findById(String courseId)
Optional<Course> findByCourseCode(String courseCode)
List<Course> findByDepartment(String department)
List<Course> findAll()
List<Course> searchCourses(String searchTerm)
void delete(String courseId)
```

#### DynamoDbRegistrationRepository
```java
Registration save(Registration registration)
Optional<Registration> findById(String registrationId)
List<Registration> findByStudentId(String studentId)
List<Registration> findByCourseId(String courseId)
List<Registration> findAll()
void delete(String registrationId)
```

## Troubleshooting

### Connection Issues

**Problem**: Cannot connect to DynamoDB
```
✗ Failed to connect to DynamoDB!
```

**Solutions**:
1. Verify NoSQL Workbench is running
2. Check that DynamoDB Local is started on port 8000
3. Ensure no other application is using port 8000
4. Check firewall settings

### Table Creation Issues

**Problem**: Tables already exist
```
Students table already exists
```

**Solution**: This is normal if tables were created previously. The application will use existing tables.

### Index Issues

**Problem**: Query fails on secondary index
```
ResourceNotFoundException: Requested resource not found
```

**Solution**: 
1. Delete existing tables in NoSQL Workbench
2. Re-run the DatabaseConnectionTest to recreate tables with proper indexes

## Viewing Data in NoSQL Workbench

1. Open NoSQL Workbench
2. Go to "Operation Builder"
3. Select your connection (localhost:8000)
4. Browse tables: Students, Courses, Registrations
5. Run queries and scans to view data

## Migration from In-Memory to DynamoDB

To migrate existing in-memory data:

1. Export data from in-memory repositories
2. Use the DynamoDB repositories to save the data
3. Verify data in NoSQL Workbench

Example migration code:
```java
// Get data from in-memory
List<Student> students = inMemoryRepo.findAll();

// Save to DynamoDB
DynamoDbStudentRepository dynamoRepo = new DynamoDbStudentRepository();
students.forEach(dynamoRepo::save);
```

## Performance Considerations

- **Local Development**: DynamoDB Local is suitable for development and testing
- **Production**: Configure AWS credentials and endpoint for production DynamoDB
- **Indexes**: All secondary indexes are configured with 5 RCU/WCU for local testing
- **Batch Operations**: Consider using batch operations for bulk inserts

## Next Steps

1. ✓ Configure DynamoDB connection
2. ✓ Create repository implementations
3. ✓ Initialize tables and indexes
4. ⏳ Update service layer to use DynamoDB repositories
5. ⏳ Test application with persistent storage
6. ⏳ Add sample data initialization

## Additional Resources

- [AWS DynamoDB SDK Documentation](https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/examples-dynamodb.html)
- [NoSQL Workbench User Guide](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/workbench.html)
- [DynamoDB Best Practices](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/best-practices.html)
