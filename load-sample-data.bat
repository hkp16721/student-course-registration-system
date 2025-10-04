@echo off
echo ==========================================
echo Loading Sample Data into DynamoDB
echo ==========================================
echo.

echo Checking database connection...
call mvn exec:java -Dexec.mainClass="com.studentcourseregistration.app.util.DatabaseConnectionTest" -q

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo X Database connection failed!
    echo Please run test-db-connection.bat first to initialize tables.
    pause
    exit /b 1
)

echo.
echo Loading sample data...
echo.

call mvn exec:java -Dexec.mainClass="com.studentcourseregistration.app.util.SampleDataLoader"

echo.
echo ==========================================
echo Done!
echo ==========================================
pause
