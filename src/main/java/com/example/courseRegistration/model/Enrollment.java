package com.example.courseRegistration.model;

import jakarta.persistence.*;

@Entity
@Table(name = "enrollments")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Student student;

    @ManyToOne
    private Course course;

    @Column(nullable = false)
    private String status = "Pending"; // Default status on enrollment

    public Enrollment() {
    }

    public Enrollment(Student student, Course course, String status) {
        this.student = student;
        this.course = course;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }   

    @Transient
     public String getAction() {
    if ("Admin Approved".equalsIgnoreCase(this.status)) {
        return "Make Payment";
    } else if ("Admin Disapproved".equalsIgnoreCase(this.status)) {
        return "Apply for Other Courses";
    } else if ("Pending".equalsIgnoreCase(this.status)) {
        return "Under Review";
    }
    return "Confirmed";
}

    
}
