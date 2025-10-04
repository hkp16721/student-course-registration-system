#!/bin/bash

echo "=========================================="
echo "Loading Sample Data into DynamoDB"
echo "=========================================="
echo ""

# Check if tables exist first
echo "Checking database connection..."
mvn exec:java -Dexec.mainClass="com.studentcourseregistration.app.util.DatabaseConnectionTest" -q

if [ $? -ne 0 ]; then
    echo ""
    echo "✗ Database connection failed!"
    echo "Please run ./test-db-connection.sh first to initialize tables."
    exit 1
fi

echo ""
echo "Loading sample data..."
echo ""

# Load sample data
mvn exec:java -Dexec.mainClass="com.studentcourseregistration.app.util.SampleDataLoader"

echo ""
echo "=========================================="
echo "Done!"
echo "=========================================="
