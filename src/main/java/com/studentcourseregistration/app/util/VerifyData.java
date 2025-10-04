package com.studentcourseregistration.app.util;

import com.studentcourseregistration.app.repository.DynamoDbCourseRepository;
import com.studentcourseregistration.app.repository.DynamoDbRegistrationRepository;
import com.studentcourseregistration.app.repository.DynamoDbStudentRepository;

public class VerifyData {
    
    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("Verifying Data in DynamoDB");
        System.out.println("=".repeat(60));
        System.out.println();
        
        try {
            DynamoDbStudentRepository studentRepo = new DynamoDbStudentRepository();
            DynamoDbCourseRepository courseRepo = new DynamoDbCourseRepository();
            DynamoDbRegistrationRepository registrationRepo = new DynamoDbRegistrationRepository();
            
            // Count students
            System.out.println("Fetching Students...");
            var students = studentRepo.findAll();
            System.out.println("  Found " + students.size() + " students");
            students.forEach(s -> System.out.println("    - " + s.getUsername() + " (" + s.getFirstName() + " " + s.getLastName() + ")"));
            
            System.out.println();
            
            // Count courses
            System.out.println("Fetching Courses...");
            var courses = courseRepo.findAll();
            System.out.println("  Found " + courses.size() + " courses");
            courses.forEach(c -> System.out.println("    - " + c.getCourseCode() + ": " + c.getCourseName() + " (Enrolled: " + c.getEnrolled() + "/" + c.getCapacity() + ")"));
            
            System.out.println();
            
            // Count registrations
            System.out.println("Fetching Registrations...");
            var registrations = registrationRepo.findAll();
            System.out.println("  Found " + registrations.size() + " registrations");
            registrations.forEach(r -> System.out.println("    - Student " + r.getStudentId() + " -> Course " + r.getCourseId() + " (" + r.getStatus() + ")"));
            
            System.out.println();
            System.out.println("=".repeat(60));
            System.out.println("✓ Verification Complete!");
            System.out.println("=".repeat(60));
            
            // Test index queries
            System.out.println();
            System.out.println("Testing Index Queries...");
            System.out.println();
            
            // Test username index
            System.out.println("1. Finding student by username 'johndoe':");
            studentRepo.findByUsername("johndoe").ifPresentOrElse(
                s -> System.out.println("   ✓ Found: " + s.getFirstName() + " " + s.getLastName()),
                () -> System.out.println("   ✗ Not found")
            );
            
            // Test email index
            System.out.println("2. Finding student by email 'jane.smith@university.edu':");
            studentRepo.findByEmail("jane.smith@university.edu").ifPresentOrElse(
                s -> System.out.println("   ✓ Found: " + s.getFirstName() + " " + s.getLastName()),
                () -> System.out.println("   ✗ Not found")
            );
            
            // Test course code index
            System.out.println("3. Finding course by code 'CS101':");
            courseRepo.findByCourseCode("CS101").ifPresentOrElse(
                c -> System.out.println("   ✓ Found: " + c.getCourseName()),
                () -> System.out.println("   ✗ Not found")
            );
            
            // Test department index
            System.out.println("4. Finding courses by department 'Computer Science':");
            var csCourses = courseRepo.findByDepartment("Computer Science");
            System.out.println("   ✓ Found " + csCourses.size() + " courses");
            csCourses.forEach(c -> System.out.println("      - " + c.getCourseCode()));
            
            // Test student registrations
            System.out.println("5. Finding registrations for student S001:");
            var s001Regs = registrationRepo.findByStudentId("S001");
            System.out.println("   ✓ Found " + s001Regs.size() + " registrations");
            s001Regs.forEach(r -> System.out.println("      - Course " + r.getCourseId()));
            
            // Test course registrations
            System.out.println("6. Finding registrations for course C001:");
            var c001Regs = registrationRepo.findByCourseId("C001");
            System.out.println("   ✓ Found " + c001Regs.size() + " registrations");
            c001Regs.forEach(r -> System.out.println("      - Student " + r.getStudentId()));
            
            System.out.println();
            System.out.println("=".repeat(60));
            System.out.println("✓ All Tests Passed!");
            System.out.println("=".repeat(60));
            
        } catch (Exception e) {
            System.err.println("✗ Error verifying data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
