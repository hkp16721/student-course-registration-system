package com.studentcourseregistration.app.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;
import java.time.Instant;
import java.util.Objects;

@DynamoDbBean
public class Registration {
    private String registrationId;
    private String studentId;
    private String courseId;
    private String status; // ENROLLED, DROPPED, WAITLISTED
    private String grade;
    private String registeredAt;
    private String droppedAt;

    public Registration() {
        this.registeredAt = Instant.now().toString();
        this.status = "ENROLLED";
    }

    @DynamoDbPartitionKey
    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = "student-index")
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = "course-index")
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(String registeredAt) {
        this.registeredAt = registeredAt;
    }

    public String getDroppedAt() {
        return droppedAt;
    }

    public void setDroppedAt(String droppedAt) {
        this.droppedAt = droppedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Registration that = (Registration) o;
        return Objects.equals(registrationId, that.registrationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registrationId);
    }

    @Override
    public String toString() {
        return "Registration{" +
                "registrationId='" + registrationId + '\'' +
                ", studentId='" + studentId + '\'' +
                ", courseId='" + courseId + '\'' +
                ", status='" + status + '\'' +
                ", registeredAt='" + registeredAt + '\'' +
                '}';
    }
}