package com.example.courseRegistration.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullname;
    private String email;
    private String password;

    public Student() {}

    public Student(String fullname, String email, String password) {
        this.fullname = fullname;
        this.email = email;
        this.password = password;
    }

    public Long getId() { return id; }
    public String getFullname() { return fullname; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public void setPassword(String password) {
        this.password = password;
    }
}
