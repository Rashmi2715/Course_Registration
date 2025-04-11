/*package com.example.courseRegistration.repository;

import com.example.courseRegistration.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    Student findByEmail(String email);
} */

package com.example.courseRegistration.repository;

import com.example.courseRegistration.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByEmail(String email);
    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.courses WHERE s.email = :email")
    Student findByEmailWithCourses(@Param("email") String email);
}

