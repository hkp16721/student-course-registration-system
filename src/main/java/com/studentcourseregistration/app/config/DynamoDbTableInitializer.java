package com.studentcourseregistration.app.config;

import com.studentcourseregistration.app.model.Course;
import com.studentcourseregistration.app.model.Registration;
import com.studentcourseregistration.app.model.Student;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.EnhancedGlobalSecondaryIndex;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

public class DynamoDbTableInitializer {
    
    public static void initializeTables() {
        DynamoDbClient client = DynamoDbConfig.getDynamoDbClient();
        DynamoDbEnhancedClient enhancedClient = DynamoDbConfig.getEnhancedClient();
        
        createStudentsTable(client, enhancedClient);
        createCoursesTable(client, enhancedClient);
        createRegistrationsTable(client, enhancedClient);
        
        System.out.println("DynamoDB tables initialized successfully!");
    }
    
    private static void createStudentsTable(DynamoDbClient client, DynamoDbEnhancedClient enhancedClient) {
        try {
            DynamoDbTable<Student> table = enhancedClient.table("Students", TableSchema.fromBean(Student.class));
            table.createTable(builder -> builder
                .provisionedThroughput(b -> b
                    .readCapacityUnits(5L)
                    .writeCapacityUnits(5L)
                    .build())
                .globalSecondaryIndices(
                    EnhancedGlobalSecondaryIndex.builder()
                        .indexName("username-index")
                        .projection(p -> p.projectionType(ProjectionType.ALL))
                        .provisionedThroughput(b -> b
                            .readCapacityUnits(5L)
                            .writeCapacityUnits(5L)
                            .build())
                        .build(),
                    EnhancedGlobalSecondaryIndex.builder()
                        .indexName("email-index")
                        .projection(p -> p.projectionType(ProjectionType.ALL))
                        .provisionedThroughput(b -> b
                            .readCapacityUnits(5L)
                            .writeCapacityUnits(5L)
                            .build())
                        .build()
                )
            );
            System.out.println("Students table created successfully");
        } catch (ResourceInUseException e) {
            System.out.println("Students table already exists");
        } catch (Exception e) {
            System.err.println("Error creating Students table: " + e.getMessage());
        }
    }
    
    private static void createCoursesTable(DynamoDbClient client, DynamoDbEnhancedClient enhancedClient) {
        try {
            DynamoDbTable<Course> table = enhancedClient.table("Courses", TableSchema.fromBean(Course.class));
            table.createTable(builder -> builder
                .provisionedThroughput(b -> b
                    .readCapacityUnits(5L)
                    .writeCapacityUnits(5L)
                    .build())
                .globalSecondaryIndices(
                    EnhancedGlobalSecondaryIndex.builder()
                        .indexName("course-code-index")
                        .projection(p -> p.projectionType(ProjectionType.ALL))
                        .provisionedThroughput(b -> b
                            .readCapacityUnits(5L)
                            .writeCapacityUnits(5L)
                            .build())
                        .build(),
                    EnhancedGlobalSecondaryIndex.builder()
                        .indexName("department-index")
                        .projection(p -> p.projectionType(ProjectionType.ALL))
                        .provisionedThroughput(b -> b
                            .readCapacityUnits(5L)
                            .writeCapacityUnits(5L)
                            .build())
                        .build()
                )
            );
            System.out.println("Courses table created successfully");
        } catch (ResourceInUseException e) {
            System.out.println("Courses table already exists");
        } catch (Exception e) {
            System.err.println("Error creating Courses table: " + e.getMessage());
        }
    }
    
    private static void createRegistrationsTable(DynamoDbClient client, DynamoDbEnhancedClient enhancedClient) {
        try {
            DynamoDbTable<Registration> table = enhancedClient.table("Registrations", TableSchema.fromBean(Registration.class));
            table.createTable(builder -> builder
                .provisionedThroughput(b -> b
                    .readCapacityUnits(5L)
                    .writeCapacityUnits(5L)
                    .build())
                .globalSecondaryIndices(
                    EnhancedGlobalSecondaryIndex.builder()
                        .indexName("student-index")
                        .projection(p -> p.projectionType(ProjectionType.ALL))
                        .provisionedThroughput(b -> b
                            .readCapacityUnits(5L)
                            .writeCapacityUnits(5L)
                            .build())
                        .build(),
                    EnhancedGlobalSecondaryIndex.builder()
                        .indexName("course-index")
                        .projection(p -> p.projectionType(ProjectionType.ALL))
                        .provisionedThroughput(b -> b
                            .readCapacityUnits(5L)
                            .writeCapacityUnits(5L)
                            .build())
                        .build()
                )
            );
            System.out.println("Registrations table created successfully");
        } catch (ResourceInUseException e) {
            System.out.println("Registrations table already exists");
        } catch (Exception e) {
            System.err.println("Error creating Registrations table: " + e.getMessage());
        }
    }
}
