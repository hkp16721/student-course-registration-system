package com.studentcourseregistration.app.service;

import com.studentcourseregistration.app.config.DynamoDbConfig;
import com.studentcourseregistration.app.model.Course;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CourseService {
    private final DynamoDbTable<Course> courseTable;

    public CourseService() {
        DynamoDbEnhancedClient enhancedClient = DynamoDbConfig.getEnhancedClient();
        this.courseTable = enhancedClient.table("Courses", TableSchema.fromBean(Course.class));
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

        courseTable.putItem(course);
        return course;
    }

    public Course findById(String courseId) {
        Key key = Key.builder().partitionValue(courseId).build();
        return courseTable.getItem(key);
    }

    public Course findByCourseCode(String courseCode) {
        try {
            return courseTable.index("course-code-index")
                    .query(QueryConditional.keyEqualTo(Key.builder().partitionValue(courseCode).build()))
                    .stream()
                    .flatMap(page -> page.items().stream())
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            return null;
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
            return List.of();
        }
    }

    public List<Course> getAllCourses() {
        return courseTable.scan(ScanEnhancedRequest.builder().build())
                .items()
                .stream()
                .collect(Collectors.toList());
    }

    public List<Course> getAvailableCourses() {
        return getAllCourses().stream()
                .filter(Course::isAvailable)
                .collect(Collectors.toList());
    }

    public List<Course> searchCourses(String searchTerm) {
        return getAllCourses().stream()
                .filter(course -> 
                    course.getCourseName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                    course.getCourseCode().toLowerCase().contains(searchTerm.toLowerCase()) ||
                    course.getInstructor().toLowerCase().contains(searchTerm.toLowerCase()) ||
                    course.getDepartment().toLowerCase().contains(searchTerm.toLowerCase())
                )
                .collect(Collectors.toList());
    }

    public Course updateCourse(Course course) {
        course.setUpdatedAt(Instant.now().toString());
        courseTable.putItem(course);
        return course;
    }

    public void deleteCourse(String courseId) {
        Key key = Key.builder().partitionValue(courseId).build();
        courseTable.deleteItem(key);
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
}