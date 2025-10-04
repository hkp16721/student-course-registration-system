package com.studentcourseregistration.app.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@DynamoDbBean
public class Course {
    private String courseId;
    private String courseCode;
    private String courseName;
    private String description;
    private Integer credits;
    private String department;
    private String instructor;
    private String semester;
    private Integer year;
    private String schedule; // e.g., "MWF 10:00-11:00"
    private String location;
    private Integer capacity;
    private Integer enrolled;
    private List<String> prerequisites;
    private String createdAt;
    private String updatedAt;

    public Course() {
        this.createdAt = Instant.now().toString();
        this.updatedAt = Instant.now().toString();
        this.enrolled = 0;
    }

    @DynamoDbPartitionKey
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = "course-code-index")
    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = "department-index")
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getEnrolled() {
        return enrolled;
    }

    public void setEnrolled(Integer enrolled) {
        this.enrolled = enrolled;
    }

    public List<String> getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(List<String> prerequisites) {
        this.prerequisites = prerequisites;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isAvailable() {
        return enrolled < capacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(courseId, course.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId);
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId='" + courseId + '\'' +
                ", courseCode='" + courseCode + '\'' +
                ", courseName='" + courseName + '\'' +
                ", department='" + department + '\'' +
                ", instructor='" + instructor + '\'' +
                ", credits=" + credits +
                ", capacity=" + capacity +
                ", enrolled=" + enrolled +
                '}';
    }
}