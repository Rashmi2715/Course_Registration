package com.example.courseRegistration.util;

import com.example.courseRegistration.model.Student;

public class UserFactory {
    public static Student createStudent(String name, String email, String password) {
        Student student = new Student();
        student.setFullname(name);
        student.setEmail(email);
        student.setPassword(password);
        return student;
    }
}