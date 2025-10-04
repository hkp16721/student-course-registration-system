#!/bin/bash

# Student Course Registration System Runner
echo "Starting Student Course Registration System..."
echo "=========================================="

# Check if JAR exists
if [ ! -f "target/student-course-registration-app-1.0-SNAPSHOT.jar" ]; then
    echo "JAR file not found. Building project..."
    mvn clean package
fi

# Run the application
java -jar target/student-course-registration-app-1.0-SNAPSHOT.jar