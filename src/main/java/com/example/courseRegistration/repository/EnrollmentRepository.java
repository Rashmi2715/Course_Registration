package com.example.courseRegistration.repository;

import com.example.courseRegistration.model.Course;
import com.example.courseRegistration.model.Enrollment;
import com.example.courseRegistration.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudent(Student student);
    //ADDED BELOW
    Enrollment findByStudentAndCourse(Student student, Course course);
    List<Enrollment> findByStatus(String status);
    long countByCourseAndStatus(Course course, String status);
    @Query("SELECT e FROM Enrollment e JOIN FETCH e.student WHERE e.status = 'Pending'")
    List<Enrollment> findPendingWithStudents();
    List<Enrollment> findByCourseIdAndStatus(Long courseId, String status);
}