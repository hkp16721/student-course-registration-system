package com.studentcourseregistration.app.service;

import com.studentcourseregistration.app.config.DynamoDbConfig;
import com.studentcourseregistration.app.model.Course;
import com.studentcourseregistration.app.model.Registration;
import com.studentcourseregistration.app.model.Student;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class RegistrationService {
    private final DynamoDbTable<Registration> registrationTable;
    private final CourseService courseService;
    private final StudentService studentService;

    public RegistrationService() {
        DynamoDbEnhancedClient enhancedClient = DynamoDbConfig.getEnhancedClient();
        this.registrationTable = enhancedClient.table("Registrations", TableSchema.fromBean(Registration.class));
        this.courseService = new CourseService();
        this.studentService = new StudentService();
    }

    public Registration registerStudentForCourse(String studentId, String courseId) {
        // Validate student exists
        Student student = studentService.findById(studentId);
        if (student == null) {
            throw new IllegalArgumentException("Student not found");
        }

        // Validate course exists
        Course course = courseService.findById(courseId);
        if (course == null) {
            throw new IllegalArgumentException("Course not found");
        }

        // Check if student is already registered for this course
        if (isStudentRegisteredForCourse(studentId, courseId)) {
            throw new IllegalArgumentException("Student is already registered for this course");
        }

        // Check course capacity
        if (!course.isAvailable()) {
            throw new IllegalArgumentException("Course is full");
        }

        // Check for schedule conflicts
        if (hasScheduleConflict(studentId, course)) {
            throw new IllegalArgumentException("Schedule conflict detected");
        }

        // Create registration
        Registration registration = new Registration();
        registration.setRegistrationId(UUID.randomUUID().toString());
        registration.setStudentId(studentId);
        registration.setCourseId(courseId);
        registration.setStatus("ENROLLED");

        // Save registration and update course enrollment
        registrationTable.putItem(registration);
        courseService.incrementEnrollment(courseId);

        return registration;
    }

    public boolean dropCourse(String studentId, String courseId) {
        Registration registration = findActiveRegistration(studentId, courseId);
        if (registration == null) {
            return false;
        }

        registration.setStatus("DROPPED");
        registration.setDroppedAt(Instant.now().toString());
        registrationTable.putItem(registration);
        courseService.decrementEnrollment(courseId);

        return true;
    }

    public List<Registration> getStudentRegistrations(String studentId) {
        try {
            return registrationTable.index("student-index")
                    .query(QueryConditional.keyEqualTo(Key.builder().partitionValue(studentId).build()))
                    .stream()
                    .flatMap(page -> page.items().stream())
                    .filter(reg -> "ENROLLED".equals(reg.getStatus()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return List.of();
        }
    }

    public List<Registration> getCourseRegistrations(String courseId) {
        try {
            return registrationTable.index("course-index")
                    .query(QueryConditional.keyEqualTo(Key.builder().partitionValue(courseId).build()))
                    .stream()
                    .flatMap(page -> page.items().stream())
                    .filter(reg -> "ENROLLED".equals(reg.getStatus()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return List.of();
        }
    }

    public List<Course> getStudentCourses(String studentId) {
        return getStudentRegistrations(studentId).stream()
                .map(reg -> courseService.findById(reg.getCourseId()))
                .filter(course -> course != null)
                .collect(Collectors.toList());
    }

    private boolean isStudentRegisteredForCourse(String studentId, String courseId) {
        return findActiveRegistration(studentId, courseId) != null;
    }

    private Registration findActiveRegistration(String studentId, String courseId) {
        return getStudentRegistrations(studentId).stream()
                .filter(reg -> reg.getCourseId().equals(courseId))
                .findFirst()
                .orElse(null);
    }

    private boolean hasScheduleConflict(String studentId, Course newCourse) {
        List<Course> studentCourses = getStudentCourses(studentId);
        
        // Simple schedule conflict check - in a real system, you'd parse the schedule strings
        // and check for actual time overlaps
        for (Course existingCourse : studentCourses) {
            if (existingCourse.getSchedule() != null && newCourse.getSchedule() != null) {
                // This is a simplified check - you'd want more sophisticated schedule parsing
                if (existingCourse.getSchedule().equals(newCourse.getSchedule())) {
                    return true;
                }
            }
        }
        return false;
    }

    public Registration updateGrade(String registrationId, String grade) {
        Key key = Key.builder().partitionValue(registrationId).build();
        Registration registration = registrationTable.getItem(key);
        
        if (registration != null) {
            registration.setGrade(grade);
            registrationTable.putItem(registration);
        }
        
        return registration;
    }
}