package com.studentcourseregistration.app.repository;

import com.studentcourseregistration.app.model.Course;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryCourseRepository {
    private final Map<String, Course> coursesById = new HashMap<>();
    private final Map<String, Course> coursesByCode = new HashMap<>();

    public void save(Course course) {
        coursesById.put(course.getCourseId(), course);
        coursesByCode.put(course.getCourseCode(), course);
    }

    public Optional<Course> findById(String courseId) {
        return Optional.ofNullable(coursesById.get(courseId));
    }

    public Optional<Course> findByCourseCode(String courseCode) {
        return Optional.ofNullable(coursesByCode.get(courseCode));
    }

    public List<Course> findByDepartment(String department) {
        return coursesById.values().stream()
                .filter(course -> department.equals(course.getDepartment()))
                .collect(Collectors.toList());
    }

    public List<Course> findAll() {
        return new ArrayList<>(coursesById.values());
    }

    public List<Course> searchCourses(String searchTerm) {
        String lowerSearchTerm = searchTerm.toLowerCase();
        return coursesById.values().stream()
                .filter(course -> 
                    course.getCourseName().toLowerCase().contains(lowerSearchTerm) ||
                    course.getCourseCode().toLowerCase().contains(lowerSearchTerm) ||
                    course.getInstructor().toLowerCase().contains(lowerSearchTerm) ||
                    course.getDepartment().toLowerCase().contains(lowerSearchTerm)
                )
                .collect(Collectors.toList());
    }

    public void delete(String courseId) {
        Course course = coursesById.remove(courseId);
        if (course != null) {
            coursesByCode.remove(course.getCourseCode());
        }
    }

    public void clear() {
        coursesById.clear();
        coursesByCode.clear();
    }
}