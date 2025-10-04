@echo off
echo ==========================================
echo Verifying Data in DynamoDB
echo ==========================================
echo.

call mvn exec:java -Dexec.mainClass="com.studentcourseregistration.app.util.VerifyData" -q

echo.
echo ==========================================
echo Verification Complete!
echo ==========================================
pause
