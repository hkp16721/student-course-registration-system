package com.studentcourseregistration.app.service;

import com.studentcourseregistration.app.config.DynamoDbConfig;
import com.studentcourseregistration.app.model.Student;
import org.mindrot.jbcrypt.BCrypt;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.time.Instant;
import java.util.UUID;

public class StudentService {
    private final DynamoDbTable<Student> studentTable;

    public StudentService() {
        DynamoDbEnhancedClient enhancedClient = DynamoDbConfig.getEnhancedClient();
        this.studentTable = enhancedClient.table("Students", TableSchema.fromBean(Student.class));
    }

    public Student createStudent(String firstName, String lastName, String email, 
                               String username, String password, String major, Integer year) {
        // Check if username or email already exists
        if (findByUsername(username) != null) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (findByEmail(email) != null) {
            throw new IllegalArgumentException("Email already exists");
        }

        Student student = new Student();
        student.setStudentId(UUID.randomUUID().toString());
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setEmail(email);
        student.setUsername(username);
        student.setPasswordHash(BCrypt.hashpw(password, BCrypt.gensalt()));
        student.setMajor(major);
        student.setYear(year);
        student.setGpa(0.0);

        studentTable.putItem(student);
        return student;
    }

    public Student findById(String studentId) {
        Key key = Key.builder().partitionValue(studentId).build();
        return studentTable.getItem(key);
    }

    public Student findByUsername(String username) {
        try {
            return studentTable.index("username-index")
                    .query(QueryConditional.keyEqualTo(Key.builder().partitionValue(username).build()))
                    .stream()
                    .flatMap(page -> page.items().stream())
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    public Student findByEmail(String email) {
        try {
            return studentTable.index("email-index")
                    .query(QueryConditional.keyEqualTo(Key.builder().partitionValue(email).build()))
                    .stream()
                    .flatMap(page -> page.items().stream())
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean authenticate(String username, String password) {
        Student student = findByUsername(username);
        if (student == null) {
            return false;
        }
        return BCrypt.checkpw(password, student.getPasswordHash());
    }

    public Student updateStudent(Student student) {
        student.setUpdatedAt(Instant.now().toString());
        studentTable.putItem(student);
        return student;
    }

    public void deleteStudent(String studentId) {
        Key key = Key.builder().partitionValue(studentId).build();
        studentTable.deleteItem(key);
    }
}