package com.example.courseRegistration.model;

import jakarta.persistence.*;

@Entity
@Table(name = "admins")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullname;
    private String email;
    private String password;

    // Newly added fields for payment
    private String upiId;
    private String accountNumber;
    private String ifscCode;

    public Admin() {
    }

    public Admin(String fullname, String email, String password, String upiId, String accountNumber, String ifscCode) {
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.upiId = upiId;
        this.accountNumber = accountNumber;
        this.ifscCode = ifscCode;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getFullname() {
        return fullname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUpiId() {
        return upiId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    // Setters (optional, only if needed)
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }
}
