package com.studentcourseregistration.app.config;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import java.net.URI;

public class DynamoDbConfig {
    private static DynamoDbEnhancedClient enhancedClient;
    private static DynamoDbClient dynamoDbClient;
    
    public static DynamoDbEnhancedClient getEnhancedClient() {
        if (enhancedClient == null) {
            dynamoDbClient = createLocalDynamoDbClient();
            enhancedClient = DynamoDbEnhancedClient.builder()
                    .dynamoDbClient(dynamoDbClient)
                    .build();
        }
        return enhancedClient;
    }
    
    public static DynamoDbClient getDynamoDbClient() {
        if (dynamoDbClient == null) {
            dynamoDbClient = createLocalDynamoDbClient();
        }
        return dynamoDbClient;
    }
    
    private static DynamoDbClient createLocalDynamoDbClient() {
        return DynamoDbClient.builder()
                .endpointOverride(URI.create("http://localhost:8000"))
                .region(Region.US_EAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(
                    AwsBasicCredentials.create("dummy", "dummy")))
                .build();
    }
}