# Student Course Registration System - UML Class Diagram

## Complete System Architecture

```mermaid
classDiagram
    %% Model Classes (Domain Entities)
    class Student {
        -String studentId
        -String firstName
        -String lastName
        -String email
        -String username
        -String passwordHash
        -String major
        -Integer year
        -Double gpa
        -String phone
        -String address
        -String createdAt
        -String updatedAt
        
        +Student()
        +getStudentId() String
        +setStudentId(String)
        +getFirstName() String
        +setFirstName(String)
        +getLastName() String
        +setLastName(String)
        +getEmail() String
        +setEmail(String)
        +getUsername() String
        +setUsername(String)
        +getPasswordHash() String
        +setPasswordHash(String)
        +getMajor() String
        +setMajor(String)
        +getYear() Integer
        +setYear(Integer)
        +getGpa() Double
        +setGpa(Double)
        +getPhone() String
        +setPhone(String)
        +getAddress() String
        +setAddress(String)
        +getCreatedAt() String
        +setCreatedAt(String)
        +getUpdatedAt() String
        +setUpdatedAt(String)
        +equals(Object) boolean
        +hashCode() int
        +toString() String
    }

    class Course {
        -String courseId
        -String courseCode
        -String courseName
        -String description
        -Integer credits
        -String department
        -String instructor
        -String semester
        -Integer year
        -String schedule
        -String location
        -Integer capacity
        -Integer enrolled
        -List~String~ prerequisites
        -String createdAt
        -String updatedAt
        
        +Course()
        +getCourseId() String
        +setCourseId(String)
        +getCourseCode() String
        +setCourseCode(String)
        +getCourseName() String
        +setCourseName(String)
        +getDescription() String
        +setDescription(String)
        +getCredits() Integer
        +setCredits(Integer)
        +getDepartment() String
        +setDepartment(String)
        +getInstructor() String
        +setInstructor(String)
        +getSemester() String
        +setSemester(String)
        +getYear() Integer
        +setYear(Integer)
        +getSchedule() String
        +setSchedule(String)
        +getLocation() String
        +setLocation(String)
        +getCapacity() Integer
        +setCapacity(Integer)
        +getEnrolled() Integer
        +setEnrolled(Integer)
        +getPrerequisites() List~String~
        +setPrerequisites(List~String~)
        +getCreatedAt() String
        +setCreatedAt(String)
        +getUpdatedAt() String
        +setUpdatedAt(String)
        +isAvailable() boolean
        +equals(Object) boolean
        +hashCode() int
        +toString() String
    }

    class Registration {
        -String registrationId
        -String studentId
        -String courseId
        -String status
        -String grade
        -String registeredAt
        -String droppedAt
        
        +Registration()
        +getRegistrationId() String
        +setRegistrationId(String)
        +getStudentId() String
        +setStudentId(String)
        +getCourseId() String
        +setCourseId(String)
        +getStatus() String
        +setStatus(String)
        +getGrade() String
        +setGrade(String)
        +getRegisteredAt() String
        +setRegisteredAt(String)
        +getDroppedAt() String
        +setDroppedAt(String)
        +equals(Object) boolean
        +hashCode() int
        +toString() String
    }

    %% Repository Classes (Data Access Layer)
    class InMemoryStudentRepository {
        -Map~String, Student~ studentsById
        -Map~String, Student~ studentsByUsername
        -Map~String, Student~ studentsByEmail
        
        +save(Student)
        +findById(String) Optional~Student~
        +findByUsername(String) Optional~Student~
        +findByEmail(String) Optional~Student~
        +delete(String)
        +clear()
    }

    class InMemoryCourseRepository {
        -Map~String, Course~ coursesById
        -Map~String, Course~ coursesByCode
        
        +save(Course)
        +findById(String) Optional~Course~
        +findByCourseCode(String) Optional~Course~
        +findByDepartment(String) List~Course~
        +findAll() List~Course~
        +searchCourses(String) List~Course~
        +delete(String)
        +clear()
    }

    class InMemoryRegistrationRepository {
        -Map~String, Registration~ registrationsById
        
        +save(Registration)
        +findById(String) Optional~Registration~
        +findByStudentId(String) List~Registration~
        +findByCourseId(String) List~Registration~
        +findActiveByStudentId(String) List~Registration~
        +findActiveByCourseId(String) List~Registration~
        +findActiveRegistration(String, String) Optional~Registration~
        +delete(String)
        +clear()
    }

    %% Service Classes (Business Logic Layer)
    class SimpleStudentService {
        -InMemoryStudentRepository repository
        
        +SimpleStudentService()
        +createStudent(String, String, String, String, String, String, Integer) Student
        +findById(String) Student
        +findByUsername(String) Student
        +findByEmail(String) Student
        +authenticate(String, String) boolean
        +updateStudent(Student) Student
        +deleteStudent(String)
        +clearAll()
    }

    class SimpleCourseService {
        -InMemoryCourseRepository repository
        
        +SimpleCourseService()
        +createCourse(String, String, String, Integer, String, String, String, Integer, String, String, Integer, List~String~) Course
        +findById(String) Course
        +findByCourseCode(String) Course
        +findByDepartment(String) List~Course~
        +getAllCourses() List~Course~
        +getAvailableCourses() List~Course~
        +searchCourses(String) List~Course~
        +updateCourse(Course) Course
        +deleteCourse(String)
        +incrementEnrollment(String) boolean
        +decrementEnrollment(String) boolean
        +clearAll()
    }

    class SimpleRegistrationService {
        -InMemoryRegistrationRepository repository
        -SimpleCourseService courseService
        -SimpleStudentService studentService
        
        +SimpleRegistrationService(SimpleCourseService, SimpleStudentService)
        +registerStudentForCourse(String, String) Registration
        +dropCourse(String, String) boolean
        +getStudentRegistrations(String) List~Registration~
        +getCourseRegistrations(String) List~Registration~
        +getStudentCourses(String) List~Course~
        -hasScheduleConflict(String, Course) boolean
        +updateGrade(String, String) Registration
        +clearAll()
    }

    %% DynamoDB Service Classes (Alternative Implementation)
    class StudentService {
        -DynamoDbTable~Student~ studentTable
        
        +StudentService()
        +createStudent(String, String, String, String, String, String, Integer) Student
        +findById(String) Student
        +findByUsername(String) Student
        +findByEmail(String) Student
        +authenticate(String, String) boolean
        +updateStudent(Student) Student
        +deleteStudent(String)
    }

    class CourseService {
        -DynamoDbTable~Course~ courseTable
        
        +CourseService()
        +createCourse(String, String, String, Integer, String, String, String, Integer, String, String, Integer, List~String~) Course
        +findById(String) Course
        +findByCourseCode(String) Course
        +findByDepartment(String) List~Course~
        +getAllCourses() List~Course~
        +getAvailableCourses() List~Course~
        +searchCourses(String) List~Course~
        +updateCourse(Course) Course
        +deleteCourse(String)
        +incrementEnrollment(String) boolean
        +decrementEnrollment(String) boolean
    }

    class RegistrationService {
        -DynamoDbTable~Registration~ registrationTable
        -CourseService courseService
        -StudentService studentService
        
        +RegistrationService()
        +registerStudentForCourse(String, String) Registration
        +dropCourse(String, String) boolean
        +getStudentRegistrations(String) List~Registration~
        +getCourseRegistrations(String) List~Registration~
        +getStudentCourses(String) List~Course~
        -hasScheduleConflict(String, Course) boolean
        +updateGrade(String, String) Registration
    }

    %% Configuration Class
    class DynamoDbConfig {
        -DynamoDbEnhancedClient enhancedClient$
        
        +getEnhancedClient()$ DynamoDbEnhancedClient
    }

    %% Main Application Class
    class App {
        -SimpleStudentService studentService$
        -SimpleCourseService courseService$
        -SimpleRegistrationService registrationService$
        -Scanner scanner$
        -Student currentStudent$
        
        +main(String[])$
        -showLoginMenu()$
        -showMainMenu()$
        -login()$
        -registerNewStudent()$
        -viewAvailableCourses()$
        -searchCourses()$
        -registerForCourse()$
        -viewMyCourses()$
        -dropCourse()$
        -viewProfile()$
        -initializeSampleData()$
    }

    %% Relationships
    
    %% Model Relationships
    Student ||--o{ Registration : "has many"
    Course ||--o{ Registration : "has many"
    Registration }o--|| Student : "belongs to"
    Registration }o--|| Course : "belongs to"

    %% Service-Repository Relationships
    SimpleStudentService --> InMemoryStudentRepository : "uses"
    SimpleCourseService --> InMemoryCourseRepository : "uses"
    SimpleRegistrationService --> InMemoryRegistrationRepository : "uses"
    SimpleRegistrationService --> SimpleCourseService : "depends on"
    SimpleRegistrationService --> SimpleStudentService : "depends on"

    %% DynamoDB Service Relationships
    StudentService --> DynamoDbConfig : "uses"
    CourseService --> DynamoDbConfig : "uses"
    RegistrationService --> DynamoDbConfig : "uses"
    RegistrationService --> CourseService : "depends on"
    RegistrationService --> StudentService : "depends on"

    %% Repository-Model Relationships
    InMemoryStudentRepository --> Student : "manages"
    InMemoryCourseRepository --> Course : "manages"
    InMemoryRegistrationRepository --> Registration : "manages"

    %% Service-Model Relationships
    SimpleStudentService --> Student : "creates/manages"
    SimpleCourseService --> Course : "creates/manages"
    SimpleRegistrationService --> Registration : "creates/manages"
    StudentService --> Student : "creates/manages"
    CourseService --> Course : "creates/manages"
    RegistrationService --> Registration : "creates/manages"

    %% Application Relationships
    App --> SimpleStudentService : "uses"
    App --> SimpleCourseService : "uses"
    App --> SimpleRegistrationService : "uses"
    App --> Student : "manages current"
```

## Architecture Layers

### 1. **Presentation Layer**
- **App**: Main console application providing user interface

### 2. **Business Logic Layer (Service Layer)**
- **SimpleStudentService**: Student management operations (in-memory)
- **SimpleCourseService**: Course management operations (in-memory)
- **SimpleRegistrationService**: Registration management operations (in-memory)
- **StudentService**: Student management operations (DynamoDB)
- **CourseService**: Course management operations (DynamoDB)
- **RegistrationService**: Registration management operations (DynamoDB)

### 3. **Data Access Layer (Repository Layer)**
- **InMemoryStudentRepository**: In-memory student data storage
- **InMemoryCourseRepository**: In-memory course data storage
- **InMemoryRegistrationRepository**: In-memory registration data storage

### 4. **Domain Model Layer**
- **Student**: Student entity with personal and academic information
- **Course**: Course entity with academic and scheduling information
- **Registration**: Registration entity linking students to courses

### 5. **Configuration Layer**
- **DynamoDbConfig**: Database configuration for AWS DynamoDB

## Key Design Patterns

### 1. **Repository Pattern**
- Abstracts data access logic
- Provides clean separation between business logic and data storage
- Enables easy switching between in-memory and database implementations

### 2. **Service Layer Pattern**
- Encapsulates business logic and rules
- Provides transaction boundaries
- Coordinates between multiple repositories

### 3. **Model-View-Controller (MVC)**
- **Model**: Domain entities (Student, Course, Registration)
- **View**: Console interface in App class
- **Controller**: Service classes handling business logic

### 4. **Dependency Injection**
- Services depend on repositories through constructor injection
- Enables loose coupling and testability

## Relationships

### **Entity Relationships**
- **Student** ↔ **Registration**: One-to-Many (A student can have multiple registrations)
- **Course** ↔ **Registration**: One-to-Many (A course can have multiple registrations)
- **Student** ↔ **Course**: Many-to-Many (through Registration entity)

### **Service Dependencies**
- **SimpleRegistrationService** depends on both **SimpleStudentService** and **SimpleCourseService**
- **RegistrationService** depends on both **StudentService** and **CourseService**

### **Data Flow**
1. **App** → **Service Layer** → **Repository Layer** → **Domain Models**
2. User interactions flow through the console interface to services
3. Services coordinate business logic and data validation
4. Repositories handle data persistence and retrieval

## Implementation Notes

### **Dual Implementation Strategy**
The system provides two complete implementations:
1. **In-Memory Implementation**: For development, testing, and demonstration
2. **DynamoDB Implementation**: For production deployment with cloud persistence

### **Security Features**
- Password hashing using BCrypt in service layer
- Input validation at service boundaries
- Duplicate prevention through repository constraints

### **Scalability Considerations**
- Repository pattern enables easy database switching
- Service layer provides transaction boundaries
- Stateless service design supports horizontal scaling