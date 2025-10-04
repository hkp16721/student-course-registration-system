@echo off
echo ==========================================
echo DynamoDB Connection Test
echo ==========================================
echo.

echo Building project...
call mvn clean compile -q

if %ERRORLEVEL% NEQ 0 (
    echo X Build failed!
    exit /b 1
)

echo √ Build successful
echo.

echo Testing DynamoDB connection...
echo.
call mvn exec:java -Dexec.mainClass="com.studentcourseregistration.app.util.DatabaseConnectionTest" -q

echo.
echo ==========================================
echo Test completed!
echo ==========================================
pause
