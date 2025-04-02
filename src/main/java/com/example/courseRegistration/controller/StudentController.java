package com.example.courseRegistration.controller;

import com.example.courseRegistration.model.Student;
import com.example.courseRegistration.service.StudentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @GetMapping("/signup")
    public String showSignupPage() {
        return "student-signup";
    }

    @PostMapping("/signup")
    public String registerStudent(@RequestParam String fullname, 
        @RequestParam String email,
        @RequestParam String password, 
        @RequestParam String confirmPassword,
        Model model) {
    if (fullname.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
        model.addAttribute("error", "All fields are required!");
        return "student-signup";
    }

    if (!password.equals(confirmPassword)) {
        model.addAttribute("error", "Passwords do not match! Please try again.");
        return "student-signup";
    }

        Student student = new Student(fullname, email, password);
        studentService.registerStudent(student);
        return "redirect:/student/login";
    }


    @GetMapping("/login")
    public String showLoginPage() {
        return "student-login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {
        Student student = studentService.authenticateStudent(email, password);
        if (student != null) {
            session.setAttribute("student", student);
            return "redirect:/student/dashboard";
        }
        model.addAttribute("error", "Invalid credentials!");
        return "student-login";
    }

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session) {
        if (session.getAttribute("student") == null) {
            return "redirect:/student/login";
        }
        return "student-dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
    @GetMapping("/forgot-password")
    public String showForgotPasswordPage() {
        return "student-forgot-password";
    }

    @PostMapping("/forgot-password")
    public String resetPassword(@RequestParam String email, 
                                @RequestParam String newPassword, 
                                @RequestParam String confirmPassword, 
                                Model model) {
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match!");
            return "student-forgot-password";
        }

        boolean isUpdated = studentService.updatePassword(email, newPassword);
        if (isUpdated) {
            model.addAttribute("message", "Password updated successfully! Please login.");
            return "redirect:/student/login";
        } else {
            model.addAttribute("error", "Email not found! Please register.");
            return "redirect:/student/signup";
        }
        
    }

}
