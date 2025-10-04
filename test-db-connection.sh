#!/bin/bash

echo "=========================================="
echo "DynamoDB Connection Test"
echo "=========================================="
echo ""

# Build the project
echo "Building project..."
mvn clean compile -q

if [ $? -ne 0 ]; then
    echo "✗ Build failed!"
    exit 1
fi

echo "✓ Build successful"
echo ""

# Run the connection test
echo "Testing DynamoDB connection..."
echo ""
mvn exec:java -Dexec.mainClass="com.studentcourseregistration.app.util.DatabaseConnectionTest" -q

echo ""
echo "=========================================="
echo "Test completed!"
echo "=========================================="
