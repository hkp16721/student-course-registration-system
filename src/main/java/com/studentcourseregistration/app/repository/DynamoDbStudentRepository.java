package com.studentcourseregistration.app.repository;

import com.studentcourseregistration.app.config.DynamoDbConfig;
import com.studentcourseregistration.app.model.Student;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DynamoDbStudentRepository {
    private final DynamoDbTable<Student> studentTable;
    
    public DynamoDbStudentRepository() {
        DynamoDbEnhancedClient enhancedClient = DynamoDbConfig.getEnhancedClient();
        this.studentTable = enhancedClient.table("Students", TableSchema.fromBean(Student.class));
    }
    
    public Student save(Student student) {
        studentTable.putItem(student);
        return student;
    }
    
    public Optional<Student> findById(String studentId) {
        try {
            Student student = studentTable.getItem(Key.builder().partitionValue(studentId).build());
            return Optional.ofNullable(student);
        } catch (ResourceNotFoundException e) {
            return Optional.empty();
        }
    }
    
    public Optional<Student> findByUsername(String username) {
        try {
            return studentTable.index("username-index")
                    .query(QueryConditional.keyEqualTo(Key.builder().partitionValue(username).build()))
                    .stream()
                    .flatMap(page -> page.items().stream())
                    .findFirst();
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    public Optional<Student> findByEmail(String email) {
        try {
            return studentTable.index("email-index")
                    .query(QueryConditional.keyEqualTo(Key.builder().partitionValue(email).build()))
                    .stream()
                    .flatMap(page -> page.items().stream())
                    .findFirst();
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();
        studentTable.scan().items().forEach(students::add);
        return students;
    }
    
    public void delete(String studentId) {
        studentTable.deleteItem(Key.builder().partitionValue(studentId).build());
    }
}
