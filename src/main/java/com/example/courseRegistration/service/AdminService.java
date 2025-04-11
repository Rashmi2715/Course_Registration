package com.example.courseRegistration.service;

import com.example.courseRegistration.model.Admin;
import com.example.courseRegistration.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepo;

    public void registerAdmin(Admin admin) {
        adminRepo.save(admin);
    }

    public Admin authenticateAdmin(String email, String password) {
        Admin admin = adminRepo.findByEmail(email);
        if (admin != null && admin.getPassword().equals(password)) {
            return admin;
        }
        return null;
    }
}
