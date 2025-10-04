# Sample Data Documentation

## Overview

The sample data loader populates your DynamoDB tables with realistic test data including 8 students, 10 courses, and 21 registrations.

## How to Load Sample Data

### Quick Method

**Linux/Mac:**
```bash
./load-sample-data.sh
```

**Windows:**
```bash
load-sample-data.bat
```

### Manual Method

```bash
mvn exec:java -Dexec.mainClass="com.studentcourseregistration.app.util.SampleDataLoader"
```

## Sample Data Details

### Students (8 total)

All student accounts use the password: `password123`

| ID | Username | Name | Email | Major | Year | GPA | Phone | Address |
|----|----------|------|-------|-------|------|-----|-------|---------|
| S001 | johndoe | John Doe | john.doe@university.edu | Computer Science | 3 | 3.8 | 555-0101 | 123 Main St |
| S002 | janesmith | Jane Smith | jane.smith@university.edu | Mathematics | 2 | 3.9 | 555-0102 | 456 Oak Ave |
| S003 | bobjohnson | Bob Johnson | bob.johnson@university.edu | Physics | 4 | 3.5 | 555-0103 | 789 Pine Rd |
| S004 | alicew | Alice Williams | alice.williams@university.edu | Computer Science | 1 | 3.7 | 555-0104 | 321 Elm St |
| S005 | charlieb | Charlie Brown | charlie.brown@university.edu | Engineering | 3 | 3.6 | 555-0105 | 654 Maple Dr |
| S006 | dianad | Diana Davis | diana.davis@university.edu | Biology | 2 | 3.85 | 555-0106 | 987 Cedar Ln |
| S007 | evem | Eve Martinez | eve.martinez@university.edu | Chemistry | 4 | 3.4 | 555-0107 | 147 Birch Way |
| S008 | frankg | Frank Garcia | frank.garcia@university.edu | Mathematics | 1 | 3.75 | 555-0108 | 258 Spruce Ct |

### Courses (10 total)

| ID | Code | Name | Description | Credits | Department | Instructor | Schedule | Location | Capacity | Prerequisites |
|----|------|------|-------------|---------|------------|------------|----------|----------|----------|---------------|
| C001 | CS101 | Introduction to Programming | Learn the fundamentals of programming using Java | 3 | Computer Science | Dr. Alan Turing | MWF 09:00-10:00 | Room 101 | 30 | None |
| C002 | CS201 | Data Structures and Algorithms | Advanced programming concepts and algorithm design | 4 | Computer Science | Dr. Donald Knuth | TTh 10:30-12:00 | Room 102 | 25 | CS101 |
| C003 | MATH201 | Calculus I | Differential and integral calculus | 4 | Mathematics | Dr. Isaac Newton | MWF 11:00-12:00 | Room 201 | 35 | None |
| C004 | MATH202 | Calculus II | Advanced calculus and series | 4 | Mathematics | Dr. Leonhard Euler | TTh 13:00-14:30 | Room 202 | 30 | MATH201 |
| C005 | PHYS101 | Physics I | Mechanics and thermodynamics | 4 | Physics | Dr. Albert Einstein | MWF 14:00-15:00 | Room 301 | 28 | None |
| C006 | ENG101 | English Composition | Academic writing and critical thinking | 3 | English | Dr. William Shakespeare | TTh 09:00-10:30 | Room 401 | 25 | None |
| C007 | CHEM101 | General Chemistry | Introduction to chemical principles | 4 | Chemistry | Dr. Marie Curie | MWF 10:00-11:00 | Lab 101 | 24 | None |
| C008 | BIO101 | Introduction to Biology | Cell biology and genetics | 4 | Biology | Dr. Charles Darwin | TTh 11:00-12:30 | Lab 201 | 30 | None |
| C009 | CS301 | Database Systems | Database design and SQL programming | 3 | Computer Science | Dr. Edgar Codd | MW 15:00-16:30 | Room 103 | 20 | CS201 |
| C010 | CS401 | Software Engineering | Software development methodologies and practices | 3 | Computer Science | Dr. Fred Brooks | TTh 15:00-16:30 | Room 104 | 22 | CS201 |

### Registrations (21 total)

#### By Student

**John Doe (S001)** - Computer Science, Year 3
- CS101 - Introduction to Programming
- CS201 - Data Structures and Algorithms
- CS301 - Database Systems

**Jane Smith (S002)** - Mathematics, Year 2
- MATH201 - Calculus I
- MATH202 - Calculus II
- CS101 - Introduction to Programming

**Bob Johnson (S003)** - Physics, Year 4
- PHYS101 - Physics I
- MATH201 - Calculus I
- CHEM101 - General Chemistry

**Alice Williams (S004)** - Computer Science, Year 1
- CS101 - Introduction to Programming
- MATH201 - Calculus I
- ENG101 - English Composition

**Charlie Brown (S005)** - Engineering, Year 3
- PHYS101 - Physics I
- MATH201 - Calculus I
- CHEM101 - General Chemistry

**Diana Davis (S006)** - Biology, Year 2
- BIO101 - Introduction to Biology
- CHEM101 - General Chemistry
- MATH201 - Calculus I

**Eve Martinez (S007)** - Chemistry, Year 4
- CHEM101 - General Chemistry
- PHYS101 - Physics I

**Frank Garcia (S008)** - Mathematics, Year 1
- MATH201 - Calculus I
- ENG101 - English Composition

#### By Course

| Course | Code | Enrolled | Capacity | Available |
|--------|------|----------|----------|-----------|
| Introduction to Programming | CS101 | 3 | 30 | 27 |
| Data Structures and Algorithms | CS201 | 1 | 25 | 24 |
| Calculus I | MATH201 | 6 | 35 | 29 |
| Calculus II | MATH202 | 1 | 30 | 29 |
| Physics I | PHYS101 | 3 | 28 | 25 |
| English Composition | ENG101 | 2 | 25 | 23 |
| General Chemistry | CHEM101 | 4 | 24 | 20 |
| Introduction to Biology | BIO101 | 1 | 30 | 29 |
| Database Systems | CS301 | 1 | 20 | 19 |
| Software Engineering | CS401 | 0 | 22 | 22 |

## Data Characteristics

### Realistic Patterns

1. **Course Selection by Major**
   - CS students take CS and Math courses
   - Math students take Math and some CS courses
   - Science students take their major courses plus Math
   - Freshmen take introductory courses

2. **Course Load**
   - Students enrolled in 2-3 courses each
   - Realistic credit hour distribution
   - Appropriate for their year level

3. **Prerequisites**
   - CS201 requires CS101
   - CS301 and CS401 require CS201
   - MATH202 requires MATH201

4. **Enrollment Distribution**
   - Popular courses (MATH201) have higher enrollment
   - Advanced courses (CS301, CS401) have lower enrollment
   - All courses have available capacity

### Password Security

All passwords are hashed using BCrypt with salt:
- Plain text password: `password123`
- Stored as BCrypt hash in database
- Secure for testing purposes

## Testing Scenarios

### Login Testing

Test with any of these accounts:
```
Username: johndoe
Password: password123

Username: janesmith
Password: password123

Username: alicew
Password: password123
```

### Course Search Testing

Search for:
- "Programming" → finds CS101
- "Calculus" → finds MATH201, MATH202
- "Dr. Turing" → finds CS101
- "Computer Science" → finds all CS courses

### Registration Testing

Try registering:
- Alice (freshman) for CS201 → Should check prerequisites
- Any student for a full course → Should check capacity
- Student for conflicting time slots → Should detect conflicts

### Query Testing

Test indexes:
- Find student by username: `johndoe`
- Find student by email: `john.doe@university.edu`
- Find courses by department: `Computer Science`
- Find courses by code: `CS101`
- Find registrations by student: `S001`
- Find registrations by course: `C001`

## Resetting Data

To reset and reload sample data:

1. **Delete all data in NoSQL Workbench**
   - Open NoSQL Workbench
   - Delete all items from each table
   - Or delete and recreate tables

2. **Re-run initialization**
   ```bash
   ./test-db-connection.sh
   ./load-sample-data.sh
   ```

## Customizing Sample Data

To add your own data, edit `SampleDataLoader.java`:

```java
// Add more students
createStudent("S009", "Your", "Name", "youruser", 
    "your.email@university.edu", "password123", 
    "Your Major", 1, 4.0, "555-0109", "Your Address")

// Add more courses
createCourse("C011", "COURSE101", "Course Name", 
    "Description", 3, "Department", "Instructor",
    "Fall", 2024, "MWF 10:00-11:00", "Room 105", 
    30, 0, null)

// Add more registrations
createRegistration("S009", "C011", "ENROLLED")
```

## Verification

After loading data, verify in NoSQL Workbench:

1. **Students Table**
   - Should have 8 items
   - Check username-index works
   - Check email-index works

2. **Courses Table**
   - Should have 10 items
   - Check course-code-index works
   - Check department-index works
   - Verify enrolled counts are updated

3. **Registrations Table**
   - Should have 21 items
   - Check student-index works
   - Check course-index works

## Notes

- All timestamps are set to current time when data is loaded
- Registration IDs are randomly generated UUIDs
- Course enrollment counts are automatically updated
- Data is idempotent - running multiple times will overwrite existing data
- All data follows the schema defined in model classes

## Support

If you encounter issues:
1. Check DynamoDB connection is working
2. Verify tables exist with proper indexes
3. Check console output for error messages
4. Verify NoSQL Workbench is running on port 8000
