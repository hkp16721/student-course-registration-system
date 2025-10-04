package com.studentcourseregistration.app.repository;

import com.studentcourseregistration.app.config.DynamoDbConfig;
import com.studentcourseregistration.app.model.Course;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DynamoDbCourseRepository {
    private final DynamoDbTable<Course> courseTable;
    
    public DynamoDbCourseRepository() {
        DynamoDbEnhancedClient enhancedClient = DynamoDbConfig.getEnhancedClient();
        this.courseTable = enhancedClient.table("Courses", TableSchema.fromBean(Course.class));
    }
    
    public Course save(Course course) {
        courseTable.putItem(course);
        return course;
    }
    
    public Optional<Course> findById(String courseId) {
        try {
            Course course = courseTable.getItem(Key.builder().partitionValue(courseId).build());
            return Optional.ofNullable(course);
        } catch (ResourceNotFoundException e) {
            return Optional.empty();
        }
    }
    
    public Optional<Course> findByCourseCode(String courseCode) {
        try {
            return courseTable.index("course-code-index")
                    .query(QueryConditional.keyEqualTo(Key.builder().partitionValue(courseCode).build()))
                    .stream()
                    .flatMap(page -> page.items().stream())
                    .findFirst();
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    public List<Course> findByDepartment(String department) {
        try {
            return courseTable.index("department-index")
                    .query(QueryConditional.keyEqualTo(Key.builder().partitionValue(department).build()))
                    .stream()
                    .flatMap(page -> page.items().stream())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    
    public List<Course> findAll() {
        List<Course> courses = new ArrayList<>();
        courseTable.scan().items().forEach(courses::add);
        return courses;
    }
    
    public List<Course> searchCourses(String searchTerm) {
        String lowerSearchTerm = searchTerm.toLowerCase();
        return findAll().stream()
                .filter(course -> 
                    course.getCourseName().toLowerCase().contains(lowerSearchTerm) ||
                    course.getCourseCode().toLowerCase().contains(lowerSearchTerm) ||
                    course.getInstructor().toLowerCase().contains(lowerSearchTerm) ||
                    course.getDepartment().toLowerCase().contains(lowerSearchTerm))
                .collect(Collectors.toList());
    }
    
    public void delete(String courseId) {
        courseTable.deleteItem(Key.builder().partitionValue(courseId).build());
    }
}
