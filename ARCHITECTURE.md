# DynamoDB Architecture Overview

## System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                     Application Layer                        │
│                         (App.java)                           │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│                      Service Layer                           │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │   Student    │  │    Course    │  │ Registration │      │
│  │   Service    │  │   Service    │  │   Service    │      │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘      │
└─────────┼──────────────────┼──────────────────┼─────────────┘
          │                  │                  │
          ▼                  ▼                  ▼
┌─────────────────────────────────────────────────────────────┐
│                   Repository Layer                           │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │  DynamoDB    │  │  DynamoDB    │  │  DynamoDB    │      │
│  │   Student    │  │   Course     │  │ Registration │      │
│  │  Repository  │  │  Repository  │  │  Repository  │      │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘      │
└─────────┼──────────────────┼──────────────────┼─────────────┘
          │                  │                  │
          └──────────────────┼──────────────────┘
                             │
                             ▼
┌─────────────────────────────────────────────────────────────┐
│                  DynamoDB Configuration                      │
│                   (DynamoDbConfig.java)                      │
│                                                              │
│  • Enhanced Client                                           │
│  • Standard Client                                           │
│  • Endpoint: http://localhost:8000                          │
│  • Region: US_EAST_1                                        │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│              DynamoDB Local (Port 8000)                      │
│                   NoSQL Workbench                            │
│                                                              │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │   Students   │  │   Courses    │  │Registrations │      │
│  │    Table     │  │    Table     │  │    Table     │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
└─────────────────────────────────────────────────────────────┘
```

## Data Flow

### 1. Write Operation (Create Student)
```
User Input
    ↓
App.java (UI)
    ↓
StudentService (Business Logic)
    ↓
DynamoDbStudentRepository (Data Access)
    ↓
DynamoDbConfig (Connection)
    ↓
DynamoDB Local (Storage)
```

### 2. Read Operation (Find by Username)
```
User Input (username)
    ↓
App.java (UI)
    ↓
StudentService (Business Logic)
    ↓
DynamoDbStudentRepository
    ↓
Query username-index (GSI)
    ↓
DynamoDB Local
    ↓
Return Student object
```

## Database Schema

### Students Table
```
┌─────────────────────────────────────────────────┐
│                  Students                        │
├─────────────────────────────────────────────────┤
│ PK: studentId (String)                          │
├─────────────────────────────────────────────────┤
│ Attributes:                                      │
│  • firstName                                     │
│  • lastName                                      │
│  • email                                         │
│  • username                                      │
│  • passwordHash                                  │
│  • major                                         │
│  • year                                          │
│  • gpa                                           │
│  • phone                                         │
│  • address                                       │
│  • createdAt                                     │
│  • updatedAt                                     │
├─────────────────────────────────────────────────┤
│ GSI: username-index                              │
│  PK: username                                    │
├─────────────────────────────────────────────────┤
│ GSI: email-index                                 │
│  PK: email                                       │
└─────────────────────────────────────────────────┘
```

### Courses Table
```
┌─────────────────────────────────────────────────┐
│                   Courses                        │
├─────────────────────────────────────────────────┤
│ PK: courseId (String)                           │
├─────────────────────────────────────────────────┤
│ Attributes:                                      │
│  • courseCode                                    │
│  • courseName                                    │
│  • description                                   │
│  • credits                                       │
│  • department                                    │
│  • instructor                                    │
│  • semester                                      │
│  • year                                          │
│  • schedule                                      │
│  • location                                      │
│  • capacity                                      │
│  • enrolled                                      │
│  • prerequisites                                 │
│  • createdAt                                     │
│  • updatedAt                                     │
├─────────────────────────────────────────────────┤
│ GSI: course-code-index                           │
│  PK: courseCode                                  │
├─────────────────────────────────────────────────┤
│ GSI: department-index                            │
│  PK: department                                  │
└─────────────────────────────────────────────────┘
```

### Registrations Table
```
┌─────────────────────────────────────────────────┐
│                Registrations                     │
├─────────────────────────────────────────────────┤
│ PK: registrationId (String)                     │
├─────────────────────────────────────────────────┤
│ Attributes:                                      │
│  • studentId                                     │
│  • courseId                                      │
│  • status                                        │
│  • grade                                         │
│  • registeredAt                                  │
│  • droppedAt                                     │
├─────────────────────────────────────────────────┤
│ GSI: student-index                               │
│  PK: studentId                                   │
├─────────────────────────────────────────────────┤
│ GSI: course-index                                │
│  PK: courseId                                    │
└─────────────────────────────────────────────────┘
```

## Query Patterns

### 1. Authentication
```
Query: Find student by username
Index: username-index
Key: username = "hemant"
Result: Student object with all attributes
```

### 2. Course Search
```
Query: Find courses by department
Index: department-index
Key: department = "Computer Science"
Result: List of Course objects
```

### 3. Student Enrollments
```
Query: Find registrations by student
Index: student-index
Key: studentId = "S001"
Result: List of Registration objects
```

### 4. Course Roster
```
Query: Find registrations by course
Index: course-index
Key: courseId = "C001"
Result: List of Registration objects
```

## Component Responsibilities

### DynamoDbConfig
- Manages DynamoDB client instances
- Configures connection to local endpoint
- Provides singleton access to clients

### Repository Layer
- Encapsulates all DynamoDB operations
- Handles CRUD operations
- Manages index queries
- Provides clean API to service layer

### Service Layer
- Implements business logic
- Validates data
- Coordinates between repositories
- Handles transactions

### Application Layer
- User interface (console)
- Input validation
- Menu navigation
- Session management

## Connection Configuration

```java
DynamoDbClient.builder()
    .endpointOverride(URI.create("http://localhost:8000"))
    .region(Region.US_EAST_1)
    .credentialsProvider(StaticCredentialsProvider.create(
        AwsBasicCredentials.create("dummy", "dummy")))
    .build()
```

### Configuration Details
- **Endpoint**: Local DynamoDB on port 8000
- **Region**: US_EAST_1 (required but not used locally)
- **Credentials**: Dummy values (local Kumarsn't validate)
- **Client Type**: Enhanced Client for bean mapping

## Initialization Flow

```
1. Application Start
   ↓
2. Run DatabaseConnectionTest
   ↓
3. Test Connection
   ↓
4. List Existing Tables
   ↓
5. Create Missing Tables
   ├─ Students (with 2 GSIs)
   ├─ Courses (with 2 GSIs)
   └─ Registrations (with 2 GSIs)
   ↓
6. Verify Table Creation
   ↓
7. Ready for Use
```

## Error Handling

### Connection Errors
```
Try: Connect to DynamoDB
Catch: Connection refused
Action: Display helpful error message
       Check NoSQL Workbench status
       Verify port 8000 availability
```

### Table Creation Errors
```
Try: Create table
Catch: ResourceInUseException
Action: Table already exists (OK)
       Continue with existing table
```

### Query Errors
```
Try: Query index
Catch: ResourceNotFoundException
Action: Index Kumarsn't exist
       Recreate tables with indexes
```

## Performance Considerations

### Read Operations
- Primary key reads: O(1) - Direct lookup
- GSI queries: O(log n) - Index scan
- Full table scans: O(n) - Avoid in production

### Write Operations
- Put item: O(1) - Direct write
- Update item: O(1) - Direct update
- Batch writes: More efficient for bulk operations

### Index Strategy
- Use GSIs for frequent query patterns
- All indexes have ALL projection type
- Provisioned at 5 RCU/WCU for local testing

## Scalability

### Local Development
- Single instance
- Limited by local resources
- Perfect for testing

### Production Deployment
- Change endpoint to AWS DynamoDB
- Add proper AWS credentials
- Adjust provisioned capacity
- Enable auto-scaling
- Add monitoring and alarms

## Security

### Local Development
- Dummy credentials
- No authentication required
- Localhost only access

### Production
- IAM roles and policies
- VPC endpoints
- Encryption at rest
- Encryption in transit
- CloudTrail logging

## Monitoring

### Local Development
- NoSQL Workbench UI
- Console logging
- Manual verification

### Production
- CloudWatch metrics
- DynamoDB Insights
- X-Ray tracing
- Custom application metrics
