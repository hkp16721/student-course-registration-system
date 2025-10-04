package com.studentcourseregistration.app.service;

import com.studentcourseregistration.app.model.Student;
import com.studentcourseregistration.app.repository.InMemoryStudentRepository;
import org.mindrot.jbcrypt.BCrypt;

import java.time.Instant;
import java.util.UUID;

public class SimpleStudentService {
    private final InMemoryStudentRepository repository;

    public SimpleStudentService() {
        this.repository = new InMemoryStudentRepository();
    }

    public Student createStudent(String firstName, String lastName, String email, 
                               String username, String password, String major, Integer year) {
        // Check if username or email already exists
        if (repository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (repository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        Student student = new Student();
        student.setStudentId(UUID.randomUUID().toString());
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setEmail(email);
        student.setUsername(username);
        student.setPasswordHash(BCrypt.hashpw(password, BCrypt.gensalt()));
        student.setMajor(major);
        student.setYear(year);
        student.setGpa(0.0);

        repository.save(student);
        return student;
    }

    public Student findById(String studentId) {
        return repository.findById(studentId).orElse(null);
    }

    public Student findByUsername(String username) {
        return repository.findByUsername(username).orElse(null);
    }

    public Student findByEmail(String email) {
        return repository.findByEmail(email).orElse(null);
    }

    public boolean authenticate(String username, String password) {
        Student student = findByUsername(username);
        if (student == null) {
            return false;
        }
        return BCrypt.checkpw(password, student.getPasswordHash());
    }

    public Student updateStudent(Student student) {
        student.setUpdatedAt(Instant.now().toString());
        repository.save(student);
        return student;
    }

    public void deleteStudent(String studentId) {
        repository.delete(studentId);
    }

    public void clearAll() {
        repository.clear();
    }
}