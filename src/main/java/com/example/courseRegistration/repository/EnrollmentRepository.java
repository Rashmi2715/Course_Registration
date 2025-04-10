package com.example.courseRegistration.repository;

import com.example.courseRegistration.model.Course;
import com.example.courseRegistration.model.Enrollment;
import com.example.courseRegistration.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudent(Student student);
    //ADDED BELOW
    Enrollment findByStudentAndCourse(Student student, Course course);
}
