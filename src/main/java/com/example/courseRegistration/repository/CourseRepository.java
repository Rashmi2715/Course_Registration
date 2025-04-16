
package com.example.courseRegistration.repository;

import com.example.courseRegistration.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {}
