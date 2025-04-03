package com.example.courseRegistration.repository;

import com.example.courseRegistration.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    // No additional methods required – basic CRUD is provided by JpaRepository
}
