package com.studentcourseregistration.app;

import com.studentcourseregistration.app.model.Course;
import com.studentcourseregistration.app.model.Registration;
import com.studentcourseregistration.app.model.Student;
import com.studentcourseregistration.app.service.SimpleCourseService;
import com.studentcourseregistration.app.service.SimpleRegistrationService;
import com.studentcourseregistration.app.service.SimpleStudentService;

import java.util.List;
import java.util.Scanner;

/**
 * Student Course Registration System
 */
public class App {
    private static final SimpleStudentService studentService = new SimpleStudentService();
    private static final SimpleCourseService courseService = new SimpleCourseService();
    private static final SimpleRegistrationService registrationService = new SimpleRegistrationService(courseService, studentService);
    private static final Scanner scanner = new Scanner(System.in);
    private static Student currentStudent = null;

    public static void main(String[] args) {
        System.out.println("=== Welcome to Student Course Registration System ===");
        
        // Initialize with some sample data
        initializeSampleData();
        
        while (true) {
            if (currentStudent == null) {
                showLoginMenu();
            } else {
                showMainMenu();
            }
        }
    }

    private static void showLoginMenu() {
        System.out.println("\n=== Login Menu ===");
        System.out.println("1. Login");
        System.out.println("2. Register New Student");
        System.out.println("3. Exit");
        System.out.print("Choose an option: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        switch (choice) {
            case 1:
                login();
                break;
            case 2:
                registerNewStudent();
                break;
            case 3:
                System.out.println("Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    private static void showMainMenu() {
        System.out.println("\n=== Main Menu ===");
        System.out.println("Welcome, " + currentStudent.getFirstName() + " " + currentStudent.getLastName());
        System.out.println("1. View Available Courses");
        System.out.println("2. Search Courses");
        System.out.println("3. Register for Course");
        System.out.println("4. View My Courses");
        System.out.println("5. Drop Course");
        System.out.println("6. View Profile");
        System.out.println("7. Logout");
        System.out.print("Choose an option: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        switch (choice) {
            case 1:
                viewAvailableCourses();
                break;
            case 2:
                searchCourses();
                break;
            case 3:
                registerForCourse();
                break;
            case 4:
                viewMyCourses();
                break;
            case 5:
                dropCourse();
                break;
            case 6:
                viewProfile();
                break;
            case 7:
                currentStudent = null;
                System.out.println("Logged out successfully.");
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    private static void login() {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (studentService.authenticate(username, password)) {
            currentStudent = studentService.findByUsername(username);
            System.out.println("Login successful!");
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    private static void registerNewStudent() {
        System.out.println("\n=== Student Registration ===");
        System.out.print("First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Major: ");
        String major = scanner.nextLine();
        System.out.print("Year (1-4): ");
        int year = scanner.nextInt();
        scanner.nextLine(); // consume newline

        try {
            Student student = studentService.createStudent(firstName, lastName, email, username, password, major, year);
            System.out.println("Student registered successfully! Student ID: " + student.getStudentId());
        } catch (IllegalArgumentException e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    private static void viewAvailableCourses() {
        System.out.println("\n=== Available Courses ===");
        List<Course> courses = courseService.getAvailableCourses();
        
        if (courses.isEmpty()) {
            System.out.println("No courses available.");
            return;
        }

        for (Course course : courses) {
            System.out.printf("%-10s | %-30s | %-15s | %d credits | %d/%d enrolled%n",
                    course.getCourseCode(),
                    course.getCourseName(),
                    course.getDepartment(),
                    course.getCredits(),
                    course.getEnrolled(),
                    course.getCapacity());
        }
    }

    private static void searchCourses() {
        System.out.print("Enter search term: ");
        String searchTerm = scanner.nextLine();
        
        List<Course> courses = courseService.searchCourses(searchTerm);
        
        if (courses.isEmpty()) {
            System.out.println("No courses found matching: " + searchTerm);
            return;
        }

        System.out.println("\n=== Search Results ===");
        for (Course course : courses) {
            System.out.printf("%-10s | %-30s | %-15s | %d credits%n",
                    course.getCourseCode(),
                    course.getCourseName(),
                    course.getDepartment(),
                    course.getCredits());
        }
    }

    private static void registerForCourse() {
        System.out.print("Enter course code: ");
        String courseCode = scanner.nextLine();
        
        Course course = courseService.findByCourseCode(courseCode);
        if (course == null) {
            System.out.println("Course not found.");
            return;
        }

        try {
            Registration registration = registrationService.registerStudentForCourse(
                    currentStudent.getStudentId(), course.getCourseId());
            System.out.println("Successfully registered for " + course.getCourseName());
        } catch (IllegalArgumentException e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    private static void viewMyCourses() {
        System.out.println("\n=== My Courses ===");
        List<Course> courses = registrationService.getStudentCourses(currentStudent.getStudentId());
        
        if (courses.isEmpty()) {
            System.out.println("You are not registered for any courses.");
            return;
        }

        for (Course course : courses) {
            System.out.printf("%-10s | %-30s | %-15s | %d credits | %s%n",
                    course.getCourseCode(),
                    course.getCourseName(),
                    course.getDepartment(),
                    course.getCredits(),
                    course.getSchedule());
        }
    }

    private static void dropCourse() {
        System.out.print("Enter course code to drop: ");
        String courseCode = scanner.nextLine();
        
        Course course = courseService.findByCourseCode(courseCode);
        if (course == null) {
            System.out.println("Course not found.");
            return;
        }

        if (registrationService.dropCourse(currentStudent.getStudentId(), course.getCourseId())) {
            System.out.println("Successfully dropped " + course.getCourseName());
        } else {
            System.out.println("You are not registered for this course.");
        }
    }

    private static void viewProfile() {
        System.out.println("\n=== My Profile ===");
        System.out.println("Student ID: " + currentStudent.getStudentId());
        System.out.println("Name: " + currentStudent.getFirstName() + " " + currentStudent.getLastName());
        System.out.println("Email: " + currentStudent.getEmail());
        System.out.println("Username: " + currentStudent.getUsername());
        System.out.println("Major: " + currentStudent.getMajor());
        System.out.println("Year: " + currentStudent.getYear());
        System.out.println("GPA: " + currentStudent.getGpa());
    }

    private static void initializeSampleData() {
        System.out.println("Initializing sample data...");
        
        try {
            // Create sample courses
            courseService.createCourse("CS101", "Introduction to Computer Science", 
                    "Basic programming concepts", 3, "Computer Science", "Dr. Smith",
                    "Fall", 2024, "MWF 10:00-11:00", "Room 101", 30, List.of());
            
            courseService.createCourse("MATH201", "Calculus I", 
                    "Differential and integral calculus", 4, "Mathematics", "Dr. Hemantson",
                    "Fall", 2024, "TTh 14:00-15:30", "Room 201", 25, List.of());
            
            courseService.createCourse("ENG101", "English Composition", 
                    "Writing and communication skills", 3, "English", "Prof. Davis",
                    "Fall", 2024, "MWF 09:00-10:00", "Room 301", 20, List.of());

            // Create a sample student
            studentService.createStudent("Hemant", "Kumar", "Hemant.Kumar@university.edu", 
                    "hemant", "password123", "Computer Science", 2);
            
            System.out.println("Sample data initialized successfully!");
            System.out.println("You can login with username: hemant, password: password123");
            
        } catch (Exception e) {
            System.out.println("Sample data may already exist or there was an error: " + e.getMessage());
        }
    }
}
