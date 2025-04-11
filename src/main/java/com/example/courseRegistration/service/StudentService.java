package com.example.courseRegistration.service;

import com.example.courseRegistration.model.Course;
import com.example.courseRegistration.model.Student;
import com.example.courseRegistration.repository.CourseRepository;
import com.example.courseRegistration.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepo;
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

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
            student.setPassword(newPassword);
            studentRepo.save(student);
            return true;
        }
        return false;
    }

    public Student findByEmail(String email) {
        return studentRepository.findByEmail(email);
    }

    public boolean enrollInCourse(Long studentId, Long courseId) {
        Optional<Student> studentOpt = studentRepo.findById(studentId);
        Optional<Course> courseOpt = courseRepository.findById(courseId);

        if (studentOpt.isPresent() && courseOpt.isPresent()) {
            Student student = studentOpt.get();
            Course course = courseOpt.get();

            if (!student.getCourses().contains(course)) {
                student.enrollCourse(course);
                studentRepo.save(student);
                return true;
            }
        }
        return false;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public void saveStudent(Student student) {
        studentRepository.saveAndFlush(student); // makes DB commit immediately
    }

}
