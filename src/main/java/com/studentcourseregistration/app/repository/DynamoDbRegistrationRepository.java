package com.studentcourseregistration.app.repository;

import com.studentcourseregistration.app.config.DynamoDbConfig;
import com.studentcourseregistration.app.model.Registration;
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

public class DynamoDbRegistrationRepository {
    private final DynamoDbTable<Registration> registrationTable;
    
    public DynamoDbRegistrationRepository() {
        DynamoDbEnhancedClient enhancedClient = DynamoDbConfig.getEnhancedClient();
        this.registrationTable = enhancedClient.table("Registrations", TableSchema.fromBean(Registration.class));
    }
    
    public Registration save(Registration registration) {
        registrationTable.putItem(registration);
        return registration;
    }
    
    public Optional<Registration> findById(String registrationId) {
        try {
            Registration registration = registrationTable.getItem(Key.builder().partitionValue(registrationId).build());
            return Optional.ofNullable(registration);
        } catch (ResourceNotFoundException e) {
            return Optional.empty();
        }
    }
    
    public List<Registration> findByStudentId(String studentId) {
        try {
            return registrationTable.index("student-index")
                    .query(QueryConditional.keyEqualTo(Key.builder().partitionValue(studentId).build()))
                    .stream()
                    .flatMap(page -> page.items().stream())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    
    public List<Registration> findByCourseId(String courseId) {
        try {
            return registrationTable.index("course-index")
                    .query(QueryConditional.keyEqualTo(Key.builder().partitionValue(courseId).build()))
                    .stream()
                    .flatMap(page -> page.items().stream())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    
    public List<Registration> findAll() {
        List<Registration> registrations = new ArrayList<>();
        registrationTable.scan().items().forEach(registrations::add);
        return registrations;
    }
    
    public void delete(String registrationId) {
        registrationTable.deleteItem(Key.builder().partitionValue(registrationId).build());
    }
}
