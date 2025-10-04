package com.studentcourseregistration.app.util;

import com.studentcourseregistration.app.model.Course;
import com.studentcourseregistration.app.model.Registration;
import com.studentcourseregistration.app.model.Student;
import com.studentcourseregistration.app.repository.DynamoDbCourseRepository;
import com.studentcourseregistration.app.repository.DynamoDbRegistrationRepository;
import com.studentcourseregistration.app.repository.DynamoDbStudentRepository;
import org.mindrot.jbcrypt.BCrypt;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class SampleDataLoader {
    
    private static final DynamoDbStudentRepository studentRepo = new DynamoDbStudentRepository();
    private static final DynamoDbCourseRepository courseRepo = new DynamoDbCourseRepository();
    private static final DynamoDbRegistrationRepository registrationRepo = new DynamoDbRegistrationRepository();
    
    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("Loading Sample Data into DynamoDB");
        System.out.println("=".repeat(60));
        System.out.println();
        
        try {
            loadStudents();
            loadCourses();
            loadRegistrations();
            
            System.out.println();
            System.out.println("=".repeat(60));
            System.out.println("✓ Sample data loaded successfully!");
            System.out.println("=".repeat(60));
            System.out.println();
            printSummary();
            
        } catch (Exception e) {
            System.err.println("✗ Error loading sample data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void loadStudents() {
        System.out.println("Loading Students...");
        
        List<Student> students = Arrays.asList(
            createStudent("S001", "John", "Doe", "johndoe", "john.doe@university.edu", 
                "password123", "Computer Science", 3, 3.8, "555-0101", "123 Main St"),
            createStudent("S002", "Jane", "Smith", "janesmith", "jane.smith@university.edu",
                "password123", "Mathematics", 2, 3.9, "555-0102", "456 Oak Ave"),
            createStudent("S003", "Bob", "Johnson", "bobjohnson", "bob.johnson@university.edu",
                "password123", "Physics", 4, 3.5, "555-0103", "789 Pine Rd"),
            createStudent("S004", "Alice", "Williams", "alicew", "alice.williams@university.edu",
                "password123", "Computer Science", 1, 3.7, "555-0104", "321 Elm St"),
            createStudent("S005", "Charlie", "Brown", "charlieb", "charlie.brown@university.edu",
                "password123", "Engineering", 3, 3.6, "555-0105", "654 Maple Dr"),
            createStudent("S006", "Diana", "Davis", "dianad", "diana.davis@university.edu",
                "password123", "Biology", 2, 3.85, "555-0106", "987 Cedar Ln"),
            createStudent("S007", "Eve", "Martinez", "evem", "eve.martinez@university.edu",
                "password123", "Chemistry", 4, 3.4, "555-0107", "147 Birch Way"),
            createStudent("S008", "Frank", "Garcia", "frankg", "frank.garcia@university.edu",
                "password123", "Mathematics", 1, 3.75, "555-0108", "258 Spruce Ct")
        );
        
        students.forEach(student -> {
            studentRepo.save(student);
            System.out.println("  ✓ Added: " + student.getFirstName() + " " + student.getLastName() + 
                " (" + student.getUsername() + ")");
        });
        
        System.out.println("  Total students: " + students.size());
        System.out.println();
    }
    
    private static void loadCourses() {
        System.out.println("Loading Courses...");
        
        List<Course> courses = Arrays.asList(
            createCourse("C001", "CS101", "Introduction to Programming", 
                "Learn the fundamentals of programming using Java", 3,
                "Computer Science", "Dr. Alan Turing", "Fall", 2024,
                "MWF 09:00-10:00", "Room 101", 30, 0, null),
            createCourse("C002", "CS201", "Data Structures and Algorithms",
                "Advanced programming concepts and algorithm design", 4,
                "Computer Science", "Dr. Donald Knuth", "Fall", 2024,
                "TTh 10:30-12:00", "Room 102", 25, 0, Arrays.asList("CS101")),
            createCourse("C003", "MATH201", "Calculus I",
                "Differential and integral calculus", 4,
                "Mathematics", "Dr. Isaac Newton", "Fall", 2024,
                "MWF 11:00-12:00", "Room 201", 35, 0, null),
            createCourse("C004", "MATH202", "Calculus II",
                "Advanced calculus and series", 4,
                "Mathematics", "Dr. Leonhard Euler", "Fall", 2024,
                "TTh 13:00-14:30", "Room 202", 30, 0, Arrays.asList("MATH201")),
            createCourse("C005", "PHYS101", "Physics I",
                "Mechanics and thermodynamics", 4,
                "Physics", "Dr. Albert Einstein", "Fall", 2024,
                "MWF 14:00-15:00", "Room 301", 28, 0, null),
            createCourse("C006", "ENG101", "English Composition",
                "Academic writing and critical thinking", 3,
                "English", "Dr. William Shakespeare", "Fall", 2024,
                "TTh 09:00-10:30", "Room 401", 25, 0, null),
            createCourse("C007", "CHEM101", "General Chemistry",
                "Introduction to chemical principles", 4,
                "Chemistry", "Dr. Marie Curie", "Fall", 2024,
                "MWF 10:00-11:00", "Lab 101", 24, 0, null),
            createCourse("C008", "BIO101", "Introduction to Biology",
                "Cell biology and genetics", 4,
                "Biology", "Dr. Charles Darwin", "Fall", 2024,
                "TTh 11:00-12:30", "Lab 201", 30, 0, null),
            createCourse("C009", "CS301", "Database Systems",
                "Database design and SQL programming", 3,
                "Computer Science", "Dr. Edgar Codd", "Fall", 2024,
                "MW 15:00-16:30", "Room 103", 20, 0, Arrays.asList("CS201")),
            createCourse("C010", "CS401", "Software Engineering",
                "Software development methodologies and practices", 3,
                "Computer Science", "Dr. Fred Brooks", "Fall", 2024,
                "TTh 15:00-16:30", "Room 104", 22, 0, Arrays.asList("CS201"))
        );
        
        courses.forEach(course -> {
            courseRepo.save(course);
            System.out.println("  ✓ Added: " + course.getCourseCode() + " - " + course.getCourseName());
        });
        
        System.out.println("  Total courses: " + courses.size());
        System.out.println();
    }
    
    private static void loadRegistrations() {
        System.out.println("Loading Registrations...");
        
        List<Registration> registrations = Arrays.asList(
            // John Doe (S001) - CS major, year 3
            createRegistration("S001", "C001", "ENROLLED"),
            createRegistration("S001", "C002", "ENROLLED"),
            createRegistration("S001", "C009", "ENROLLED"),
            
            // Jane Smith (S002) - Math major, year 2
            createRegistration("S002", "C003", "ENROLLED"),
            createRegistration("S002", "C004", "ENROLLED"),
            createRegistration("S002", "C001", "ENROLLED"),
            
            // Bob Johnson (S003) - Physics major, year 4
            createRegistration("S003", "C005", "ENROLLED"),
            createRegistration("S003", "C003", "ENROLLED"),
            createRegistration("S003", "C007", "ENROLLED"),
            
            // Alice Williams (S004) - CS major, year 1
            createRegistration("S004", "C001", "ENROLLED"),
            createRegistration("S004", "C003", "ENROLLED"),
            createRegistration("S004", "C006", "ENROLLED"),
            
            // Charlie Brown (S005) - Engineering, year 3
            createRegistration("S005", "C005", "ENROLLED"),
            createRegistration("S005", "C003", "ENROLLED"),
            createRegistration("S005", "C007", "ENROLLED"),
            
            // Diana Davis (S006) - Biology, year 2
            createRegistration("S006", "C008", "ENROLLED"),
            createRegistration("S006", "C007", "ENROLLED"),
            createRegistration("S006", "C003", "ENROLLED"),
            
            // Eve Martinez (S007) - Chemistry, year 4
            createRegistration("S007", "C007", "ENROLLED"),
            createRegistration("S007", "C005", "ENROLLED"),
            
            // Frank Garcia (S008) - Math, year 1
            createRegistration("S008", "C003", "ENROLLED"),
            createRegistration("S008", "C006", "ENROLLED")
        );
        
        registrations.forEach(registration -> {
            registrationRepo.save(registration);
            
            // Update course enrollment count
            courseRepo.findById(registration.getCourseId()).ifPresent(course -> {
                course.setEnrolled(course.getEnrolled() + 1);
                courseRepo.save(course);
            });
            
            System.out.println("  ✓ Registered: Student " + registration.getStudentId() + 
                " -> Course " + registration.getCourseId());
        });
        
        System.out.println("  Total registrations: " + registrations.size());
        System.out.println();
    }
    
    private static Student createStudent(String id, String firstName, String lastName,
                                        String username, String email, String password,
                                        String major, int year, double gpa,
                                        String phone, String address) {
        Student student = new Student();
        student.setStudentId(id);
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setUsername(username);
        student.setEmail(email);
        student.setPasswordHash(BCrypt.hashpw(password, BCrypt.gensalt()));
        student.setMajor(major);
        student.setYear(year);
        student.setGpa(gpa);
        student.setPhone(phone);
        student.setAddress(address);
        student.setCreatedAt(Instant.now().toString());
        student.setUpdatedAt(Instant.now().toString());
        return student;
    }
    
    private static Course createCourse(String id, String code, String name, String description,
                                      int credits, String department, String instructor,
                                      String semester, int year, String schedule,
                                      String location, int capacity, int enrolled,
                                      List<String> prerequisites) {
        Course course = new Course();
        course.setCourseId(id);
        course.setCourseCode(code);
        course.setCourseName(name);
        course.setDescription(description);
        course.setCredits(credits);
        course.setDepartment(department);
        course.setInstructor(instructor);
        course.setSemester(semester);
        course.setYear(year);
        course.setSchedule(schedule);
        course.setLocation(location);
        course.setCapacity(capacity);
        course.setEnrolled(enrolled);
        course.setPrerequisites(prerequisites);
        course.setCreatedAt(Instant.now().toString());
        course.setUpdatedAt(Instant.now().toString());
        return course;
    }
    
    private static Registration createRegistration(String studentId, String courseId, String status) {
        Registration registration = new Registration();
        registration.setRegistrationId(UUID.randomUUID().toString());
        registration.setStudentId(studentId);
        registration.setCourseId(courseId);
        registration.setStatus(status);
        registration.setRegisteredAt(Instant.now().toString());
        return registration;
    }
    
    private static void printSummary() {
        System.out.println("Sample Data Summary:");
        System.out.println("-".repeat(60));
        
        System.out.println("\nStudents (8 total):");
        System.out.println("  • johndoe (CS, Year 3) - password123");
        System.out.println("  • janesmith (Math, Year 2) - password123");
        System.out.println("  • bobjohnson (Physics, Year 4) - password123");
        System.out.println("  • alicew (CS, Year 1) - password123");
        System.out.println("  • charlieb (Engineering, Year 3) - password123");
        System.out.println("  • dianad (Biology, Year 2) - password123");
        System.out.println("  • evem (Chemistry, Year 4) - password123");
        System.out.println("  • frankg (Math, Year 1) - password123");
        
        System.out.println("\nCourses (10 total):");
        System.out.println("  • CS101 - Introduction to Programming");
        System.out.println("  • CS201 - Data Structures and Algorithms");
        System.out.println("  • CS301 - Database Systems");
        System.out.println("  • CS401 - Software Engineering");
        System.out.println("  • MATH201 - Calculus I");
        System.out.println("  • MATH202 - Calculus II");
        System.out.println("  • PHYS101 - Physics I");
        System.out.println("  • CHEM101 - General Chemistry");
        System.out.println("  • BIO101 - Introduction to Biology");
        System.out.println("  • ENG101 - English Composition");
        
        System.out.println("\nRegistrations: 21 total");
        System.out.println("  • Students enrolled in 2-3 courses each");
        System.out.println("  • Course enrollments updated automatically");
        
        System.out.println("\nYou can now:");
        System.out.println("  1. Login with any username above (password: password123)");
        System.out.println("  2. View the data in NoSQL Workbench");
        System.out.println("  3. Run the main application to interact with the data");
        System.out.println();
    }
}
