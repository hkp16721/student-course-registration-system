package com.studentcourseregistration.app.util;

import com.studentcourseregistration.app.config.DynamoDbConfig;
import com.studentcourseregistration.app.config.DynamoDbTableInitializer;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.ListTablesResponse;

public class DatabaseConnectionTest {
    
    public static void main(String[] args) {
        System.out.println("Testing DynamoDB connection to localhost:8000...\n");
        
        try {
            DynamoDbClient client = DynamoDbConfig.getDynamoDbClient();
            
            // Test connection by listing tables
            System.out.println("Attempting to connect to DynamoDB...");
            ListTablesResponse response = client.listTables();
            
            System.out.println("✓ Successfully connected to DynamoDB!");
            System.out.println("\nExisting tables:");
            if (response.tableNames().isEmpty()) {
                System.out.println("  (No tables found)");
            } else {
                response.tableNames().forEach(table -> System.out.println("  - " + table));
            }
            
            // Initialize tables
            System.out.println("\n" + "=".repeat(50));
            System.out.println("Initializing application tables...");
            System.out.println("=".repeat(50) + "\n");
            
            DynamoDbTableInitializer.initializeTables();
            
            // List tables again
            System.out.println("\n" + "=".repeat(50));
            System.out.println("Current tables in database:");
            System.out.println("=".repeat(50));
            response = client.listTables();
            response.tableNames().forEach(table -> System.out.println("  ✓ " + table));
            
            System.out.println("\n✓ Database setup completed successfully!");
            System.out.println("You can now run the application with DynamoDB support.");
            
        } catch (Exception e) {
            System.err.println("✗ Failed to connect to DynamoDB!");
            System.err.println("Error: " + e.getMessage());
            System.err.println("\nPlease ensure:");
            System.err.println("  1. NoSQL Workbench is running");
            System.err.println("  2. DynamoDB Local is started on port 8000");
            System.err.println("  3. No firewall is blocking the connection");
            e.printStackTrace();
        }
    }
}
