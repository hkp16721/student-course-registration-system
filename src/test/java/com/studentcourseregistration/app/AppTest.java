package com.studentcourseregistration.app;

import com.studentcourseregistration.app.model.Course;
import com.studentcourseregistration.app.model.Student;
import com.studentcourseregistration.app.service.SimpleCourseService;
import com.studentcourseregistration.app.service.SimpleStudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Unit tests for Student Course Registration System
 */
public class AppTest {

    private SimpleStudentService studentService;
    private SimpleCourseService courseService;

    @BeforeEach
    public void setUp() {
        studentService = new SimpleStudentService();
        courseService = new SimpleCourseService();
        // Clear any existing data before each test
        studentService.clearAll();
        courseService.clearAll();
    }

    @Test
    public void testStudentCreation() {
        // Test creating a new student
        String testEmail = "test" + System.currentTimeMillis() + "@test.com";
        String testUsername = "testuser" + System.currentTimeMillis();
        
        Student student = studentService.createStudent(
            "Test", "User", testEmail, testUsername, 
            "password123", "Computer Science", 2
        );
        
        assertNotNull(student);
        assertNotNull(student.getStudentId());
        assertEquals("Test", student.getFirstName());
        assertEquals("User", student.getLastName());
        assertEquals(testEmail, student.getEmail());
        assertEquals(testUsername, student.getUsername());
        assertEquals("Computer Science", student.getMajor());
        assertEquals(2, student.getYear());
        assertNotNull(student.getPasswordHash());
        assertTrue(student.getPasswordHash().startsWith("$2a$")); // BCrypt hash format
    }

    @Test
    public void testStudentAuthentication() {
        String testEmail = "auth" + System.currentTimeMillis() + "@test.com";
        String testUsername = "authuser" + System.currentTimeMillis();
        String password = "testpassword";
        
        // Create student
        studentService.createStudent(
            "Auth", "Test", testEmail, testUsername, 
            password, "Mathematics", 1
        );
        
        // Test successful authentication
        assertTrue(studentService.authenticate(testUsername, password));
        
        // Test failed authentication
        assertFalse(studentService.authenticate(testUsername, "wrongpassword"));
        assertFalse(studentService.authenticate("nonexistentuser", password));
    }

    @Test
    public void testCourseCreation() {
        Course course = courseService.createCourse(
            "TEST101", "Test Course", "A test course description",
            3, "Test Department", "Test Instructor",
            "Fall", 2024, "MWF 10:00-11:00", "Room 100", 25, List.of()
        );
        
        assertNotNull(course);
        assertNotNull(course.getCourseId());
        assertEquals("TEST101", course.getCourseCode());
        assertEquals("Test Course", course.getCourseName());
        assertEquals(3, course.getCredits());
        assertEquals("Test Department", course.getDepartment());
        assertEquals(25, course.getCapacity());
        assertEquals(0, course.getEnrolled());
        assertTrue(course.isAvailable());
    }

    @Test
    public void testCourseSearch() {
        // Create a test course
        String uniqueCode = "SEARCH" + System.currentTimeMillis();
        courseService.createCourse(
            uniqueCode, "Searchable Course", "A course for search testing",
            3, "Search Department", "Search Instructor",
            "Fall", 2024, "TTh 14:00-15:30", "Room 200", 20, List.of()
        );
        
        // Test search by course code
        List<Course> results = courseService.searchCourses(uniqueCode);
        assertFalse(results.isEmpty());
        assertTrue(results.stream().anyMatch(c -> c.getCourseCode().equals(uniqueCode)));
        
        // Test search by course name
        results = courseService.searchCourses("Searchable");
        assertFalse(results.isEmpty());
        assertTrue(results.stream().anyMatch(c -> c.getCourseName().contains("Searchable")));
    }

    @Test
    public void testDuplicateUsernameValidation() {
        String testUsername = "duplicate" + System.currentTimeMillis();
        
        // Create first student
        studentService.createStudent(
            "First", "Student", "first@test.com", testUsername, 
            "password1", "Major1", 1
        );
        
        // Try to create second student with same username
        assertThrows(IllegalArgumentException.class, () -> {
            studentService.createStudent(
                "Second", "Student", "second@test.com", testUsername, 
                "password2", "Major2", 2
            );
        });
    }

    @Test
    public void testDuplicateEmailValidation() {
        String testEmail = "duplicate" + System.currentTimeMillis() + "@test.com";
        
        // Create first student
        studentService.createStudent(
            "First", "Student", testEmail, "username1", 
            "password1", "Major1", 1
        );
        
        // Try to create second student with same email
        assertThrows(IllegalArgumentException.class, () -> {
            studentService.createStudent(
                "Second", "Student", testEmail, "username2", 
                "password2", "Major2", 2
            );
        });
    }

    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }
}
