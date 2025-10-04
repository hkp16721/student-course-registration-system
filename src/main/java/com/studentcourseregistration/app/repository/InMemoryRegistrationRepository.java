package com.studentcourseregistration.app.repository;

import com.studentcourseregistration.app.model.Registration;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryRegistrationRepository {
    private final Map<String, Registration> registrationsById = new HashMap<>();

    public void save(Registration registration) {
        registrationsById.put(registration.getRegistrationId(), registration);
    }

    public Optional<Registration> findById(String registrationId) {
        return Optional.ofNullable(registrationsById.get(registrationId));
    }

    public List<Registration> findByStudentId(String studentId) {
        return registrationsById.values().stream()
                .filter(reg -> studentId.equals(reg.getStudentId()))
                .collect(Collectors.toList());
    }

    public List<Registration> findByCourseId(String courseId) {
        return registrationsById.values().stream()
                .filter(reg -> courseId.equals(reg.getCourseId()))
                .collect(Collectors.toList());
    }

    public List<Registration> findActiveByStudentId(String studentId) {
        return findByStudentId(studentId).stream()
                .filter(reg -> "ENROLLED".equals(reg.getStatus()))
                .collect(Collectors.toList());
    }

    public List<Registration> findActiveByCourseId(String courseId) {
        return findByCourseId(courseId).stream()
                .filter(reg -> "ENROLLED".equals(reg.getStatus()))
                .collect(Collectors.toList());
    }

    public Optional<Registration> findActiveRegistration(String studentId, String courseId) {
        return registrationsById.values().stream()
                .filter(reg -> studentId.equals(reg.getStudentId()) && 
                              courseId.equals(reg.getCourseId()) && 
                              "ENROLLED".equals(reg.getStatus()))
                .findFirst();
    }

    public void delete(String registrationId) {
        registrationsById.remove(registrationId);
    }

    public void clear() {
        registrationsById.clear();
    }
}