package com.example.courseRegistration.service;

import com.example.courseRegistration.model.Student;
import com.example.courseRegistration.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepo;

    public void registerStudent(Student student) {
        if (student.getEmail() == null || student.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        studentRepo.save(student);
    }

    public Student authenticateStudent(String email, String password) {
        Student student = studentRepo.findByEmail(email);
        if (student != null && student.getPassword().equals(password)) {
            return student;
        }
        return null;
    }
    
    public boolean updatePassword(String email, String newPassword) {
        Student student = studentRepo.findByEmail(email);
        if (student != null) {
            student.setPassword(newPassword);  // Ideally, hash the password before storing
            studentRepo.save(student);
            return true;
        }
        return false;
    }

}
