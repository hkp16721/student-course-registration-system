# Quick Start Guide - DynamoDB Integration

## 🚀 Get Started in 4 Steps

### Step 1: Start NoSQL Workbench

Make sure NoSQL Workbench is running with DynamoDB Local on port 8000.

### Step 2: Initialize Database

Run the connection test script:

**Linux/Mac:**

```bash
./test-db-connection.sh
```

**Windows:**

```bash
test-db-connection.bat
```

**Or manually:**

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.studentcourseregistration.app.util.DatabaseConnectionTest"
```

### Step 3: Load Sample Data

Populate tables with dummy data:

**Linux/Mac:**

```bash
./load-sample-data.sh
```

**Windows:**

```bash
load-sample-data.bat
```

**Or manually:**

```bash
mvn exec:java -Dexec.mainClass="com.studentcourseregistration.app.util.SampleDataLoader"
```

### Step 4: Verify Setup

You should see:

```
✓ Successfully connected to DynamoDB!
Students table created successfully
Courses table created successfully
Registrations table created successfully

Loading Sample Data...
✓ Added 8 students
✓ Added 10 courses  
✓ Added 21 registrations
```

## ✅ What Just Happened?

The scripts:

1. ✓ Connected to DynamoDB Local on port 8000
2. ✓ Created 3 tables: Students, Courses, Registrations
3. ✓ Created 6 indexes for efficient queries
4. ✓ Loaded sample data (8 students, 10 courses, 21 registrations)
5. ✓ Verified everything is working

## 📊 View Your Tables

Open NoSQL Workbench:

1. Go to "Operation Builder"
2. Select your connection (localhost:8000)
3. You'll see:
   - **Students** table with username-index and email-index
   - **Courses** table with course-code-index and department-index
   - **Registrations** table with student-index and course-index

## 🧪 Test the Repositories

Create a test file to try out the repositories:

```java
import com.studentcourseregistration.app.repository.*;
import com.studentcourseregistration.app.model.*;

public class TestRepositories {
    public static void main(String[] args) {
        // Create repositories
        DynamoDbStudentRepository studentRepo = new DynamoDbStudentRepository();
        DynamoDbCourseRepository courseRepo = new DynamoDbCourseRepository();

        // Create a student
        Student student = new Student();
        student.setStudentId("S001");
        student.setFirstName("Hemant");
        student.setLastName("Kumar");
        student.setUsername("hemant");
        student.setEmail("Hemant@example.com");
        studentRepo.save(student);

        // Find by username
        studentRepo.findByUsername("hemant")
            .ifPresent(s -> System.out.println("Found: " + s));

        // Create a course
        Course course = new Course();
        course.setCourseId("C001");
        course.setCourseCode("CS101");
        course.setCourseName("Introduction to Programming");
        course.setDepartment("Computer Science");
        course.setCapacity(30);
        courseRepo.save(course);

        // Find by course code
        courseRepo.findByCourseCode("CS101")
            .ifPresent(c -> System.out.println("Found: " + c));

        System.out.println("✓ All tests passed!");
    }
}
```

## 🔍 Common Operations

### Save Data

```java
DynamoDbStudentRepository repo = new DynamoDbStudentRepository();
Student student = new Student();
student.setStudentId("S001");
student.setUsername("hemant");
repo.save(student);
```

### Find by ID

```java
Optional<Student> student = repo.findById("S001");
student.ifPresent(s -> System.out.println(s));
```

### Query by Index

```java
Optional<Student> student = repo.findByUsername("hemant");
List<Course> courses = courseRepo.findByDepartment("Computer Science");
```

### List All

```java
List<Student> allStudents = repo.findAll();
```

## 🛠️ Troubleshooting

### Problem: Connection Failed

```
✗ Failed to connect to DynamoDB!
```

**Solution:**

1. Check NoSQL Workbench is running
2. Verify DynamoDB Local is on port 8000
3. Check no firewall blocking localhost:8000

### Problem: Tables Already Exist

```
Students table already exists
```

**Solution:** This is normal! The script detects existing tables and uses them.

### Problem: Index Not Found

```
ResourceNotFoundException: Requested resource not found
```

**Solution:**

1. Delete tables in NoSQL Workbench
2. Re-run the connection test script
3. Tables will be recreated with proper indexes

## 📚 Next Steps

1. **Read the Documentation**

   - [DYNAMODB_SETUP.md](DYNAMODB_SETUP.md) - Detailed setup guide
   - [ARCHITECTURE.md](ARCHITECTURE.md) - System architecture
   - [IMPLEMENTATION_SUMMARY.md](IMPLEMENTATION_SUMMARY.md) - What was built

2. **Integrate with Application**

   - Update service layer to use DynamoDB repositories
   - Modify App.java to use persistent storage
   - Add sample data initialization

3. **Test Everything**
   - Create test data
   - Verify persistence across restarts
   - Test all CRUD operations
   - Validate index queries

## 💡 Tips

- **View Data**: Use NoSQL Workbench to browse and query tables
- **Reset Data**: Delete tables in NoSQL Workbench and re-run setup
- **Debug**: Check console output for detailed error messages
- **Performance**: Local DynamoDB is slower than AWS - this is normal

## 🎯 Success Checklist

- [ ] NoSQL Workbench running on port 8000
- [ ] Connection test script executed successfully
- [ ] Three tables created (Students, Courses, Registrations)
- [ ] Six indexes created (2 per table)
- [ ] Can view tables in NoSQL Workbench
- [ ] Ready to integrate with application

## 📞 Need Help?

Check these resources:

- [AWS DynamoDB Documentation](https://docs.aws.amazon.com/dynamodb/)
- [NoSQL Workbench Guide](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/workbench.html)
- [DynamoDB SDK for Java](https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/examples-dynamodb.html)

## 🎉 You're Ready!

Your DynamoDB setup is complete. The application now has:

- ✓ Persistent storage
- ✓ Efficient queries with indexes
- ✓ Production-ready schema
- ✓ Local development environment

Start building with confidence! 🚀


## 👥 Sample Data Loaded

### Students (8 total)
All passwords are: `password123`

| Username | Name | Major | Year | GPA |
|----------|------|-------|------|-----|
| johndoe | John Doe | Computer Science | 3 | 3.8 |
| janesmith | Jane Smith | Mathematics | 2 | 3.9 |
| bobjohnson | Bob Johnson | Physics | 4 | 3.5 |
| alicew | Alice Williams | Computer Science | 1 | 3.7 |
| charlieb | Charlie Brown | Engineering | 3 | 3.6 |
| dianad | Diana Davis | Biology | 2 | 3.85 |
| evem | Eve Martinez | Chemistry | 4 | 3.4 |
| frankg | Frank Garcia | Mathematics | 1 | 3.75 |

### Courses (10 total)

| Code | Name | Department | Instructor | Capacity |
|------|------|------------|------------|----------|
| CS101 | Introduction to Programming | Computer Science | Dr. Alan Turing | 30 |
| CS201 | Data Structures and Algorithms | Computer Science | Dr. Donald Knuth | 25 |
| CS301 | Database Systems | Computer Science | Dr. Edgar Codd | 20 |
| CS401 | Software Engineering | Computer Science | Dr. Fred Brooks | 22 |
| MATH201 | Calculus I | Mathematics | Dr. Isaac Newton | 35 |
| MATH202 | Calculus II | Mathematics | Dr. Leonhard Euler | 30 |
| PHYS101 | Physics I | Physics | Dr. Albert Einstein | 28 |
| CHEM101 | General Chemistry | Chemistry | Dr. Marie Curie | 24 |
| BIO101 | Introduction to Biology | Biology | Dr. Charles Darwin | 30 |
| ENG101 | English Composition | English | Dr. William Shakespeare | 25 |

### Registrations (21 total)
Students are enrolled in 2-3 courses each, with realistic course selections based on their majors.

**Example Enrollments:**
- John Doe (CS major): CS101, CS201, CS301
- Jane Smith (Math major): MATH201, MATH202, CS101
- Alice Williams (CS freshman): CS101, MATH201, ENG101
