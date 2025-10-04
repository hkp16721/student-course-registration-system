package com.studentcourseregistration.app.service;

import com.studentcourseregistration.app.model.Course;
import com.studentcourseregistration.app.model.Registration;
import com.studentcourseregistration.app.model.Student;
import com.studentcourseregistration.app.repository.InMemoryRegistrationRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SimpleRegistrationService {
    private final InMemoryRegistrationRepository repository;
    private final SimpleCourseService courseService;
    private final SimpleStudentService studentService;

    public SimpleRegistrationService(SimpleCourseService courseService, SimpleStudentService studentService) {
        this.repository = new InMemoryRegistrationRepository();
        this.courseService = courseService;
        this.studentService = studentService;
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
        if (repository.findActiveRegistration(studentId, courseId).isPresent()) {
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
        repository.save(registration);
        courseService.incrementEnrollment(courseId);

        return registration;
    }

    public boolean dropCourse(String studentId, String courseId) {
        Registration registration = repository.findActiveRegistration(studentId, courseId).orElse(null);
        if (registration == null) {
            return false;
        }

        registration.setStatus("DROPPED");
        registration.setDroppedAt(Instant.now().toString());
        repository.save(registration);
        courseService.decrementEnrollment(courseId);

        return true;
    }

    public List<Registration> getStudentRegistrations(String studentId) {
        return repository.findActiveByStudentId(studentId);
    }

    public List<Registration> getCourseRegistrations(String courseId) {
        return repository.findActiveByCourseId(courseId);
    }

    public List<Course> getStudentCourses(String studentId) {
        return getStudentRegistrations(studentId).stream()
                .map(reg -> courseService.findById(reg.getCourseId()))
                .filter(course -> course != null)
                .collect(Collectors.toList());
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
        Registration registration = repository.findById(registrationId).orElse(null);
        
        if (registration != null) {
            registration.setGrade(grade);
            repository.save(registration);
        }
        
        return registration;
    }

    public void clearAll() {
        repository.clear();
    }
}