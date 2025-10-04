package com.studentcourseregistration.app.service;

import com.studentcourseregistration.app.model.Course;
import com.studentcourseregistration.app.repository.InMemoryCourseRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SimpleCourseService {
    private final InMemoryCourseRepository repository;

    public SimpleCourseService() {
        this.repository = new InMemoryCourseRepository();
    }

    public Course createCourse(String courseCode, String courseName, String description,
                             Integer credits, String department, String instructor,
                             String semester, Integer year, String schedule,
                             String location, Integer capacity, List<String> prerequisites) {
        Course course = new Course();
        course.setCourseId(UUID.randomUUID().toString());
        course.setCourseCode(courseCode);
        course.setCourseName(courseName);
        course.setDescription(description);
        course.setCredits(credits);
        course.setDepartment(department);
        course.setInstructor(instructor);
        course.setSemester(semester);
        course.setYear(year);
        course.setSchedule(schedule);
        course.setLocation(location);
        course.setCapacity(capacity);
        course.setPrerequisites(prerequisites);

        repository.save(course);
        return course;
    }

    public Course findById(String courseId) {
        return repository.findById(courseId).orElse(null);
    }

    public Course findByCourseCode(String courseCode) {
        return repository.findByCourseCode(courseCode).orElse(null);
    }

    public List<Course> findByDepartment(String department) {
        return repository.findByDepartment(department);
    }

    public List<Course> getAllCourses() {
        return repository.findAll();
    }

    public List<Course> getAvailableCourses() {
        return getAllCourses().stream()
                .filter(Course::isAvailable)
                .collect(Collectors.toList());
    }

    public List<Course> searchCourses(String searchTerm) {
        return repository.searchCourses(searchTerm);
    }

    public Course updateCourse(Course course) {
        course.setUpdatedAt(Instant.now().toString());
        repository.save(course);
        return course;
    }

    public void deleteCourse(String courseId) {
        repository.delete(courseId);
    }

    public boolean incrementEnrollment(String courseId) {
        Course course = findById(courseId);
        if (course != null && course.isAvailable()) {
            course.setEnrolled(course.getEnrolled() + 1);
            updateCourse(course);
            return true;
        }
        return false;
    }

    public boolean decrementEnrollment(String courseId) {
        Course course = findById(courseId);
        if (course != null && course.getEnrolled() > 0) {
            course.setEnrolled(course.getEnrolled() - 1);
            updateCourse(course);
            return true;
        }
        return false;
    }

    public void clearAll() {
        repository.clear();
    }
}