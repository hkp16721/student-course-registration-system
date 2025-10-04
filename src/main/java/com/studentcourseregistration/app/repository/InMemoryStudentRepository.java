package com.studentcourseregistration.app.repository;

import com.studentcourseregistration.app.model.Student;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryStudentRepository {
    private final Map<String, Student> studentsById = new HashMap<>();
    private final Map<String, Student> studentsByUsername = new HashMap<>();
    private final Map<String, Student> studentsByEmail = new HashMap<>();

    public void save(Student student) {
        studentsById.put(student.getStudentId(), student);
        studentsByUsername.put(student.getUsername(), student);
        studentsByEmail.put(student.getEmail(), student);
    }

    public Optional<Student> findById(String studentId) {
        return Optional.ofNullable(studentsById.get(studentId));
    }

    public Optional<Student> findByUsername(String username) {
        return Optional.ofNullable(studentsByUsername.get(username));
    }

    public Optional<Student> findByEmail(String email) {
        return Optional.ofNullable(studentsByEmail.get(email));
    }

    public void delete(String studentId) {
        Student student = studentsById.remove(studentId);
        if (student != null) {
            studentsByUsername.remove(student.getUsername());
            studentsByEmail.remove(student.getEmail());
        }
    }

    public void clear() {
        studentsById.clear();
        studentsByUsername.clear();
        studentsByEmail.clear();
    }
}