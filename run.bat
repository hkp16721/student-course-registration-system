@echo off
echo Starting Student Course Registration System...
echo ==========================================

REM Check if JAR exists
if not exist "target\student-course-registration-app-1.0-SNAPSHOT.jar" (
    echo JAR file not found. Building project...
    mvn clean package
)

REM Run the application
java -jar target\student-course-registration-app-1.0-SNAPSHOT.jar
pause