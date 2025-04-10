package com.example.courseRegistration.controller;

import com.example.courseRegistration.model.Admin;
import com.example.courseRegistration.model.Course;
import com.example.courseRegistration.service.AdminService;
import com.example.courseRegistration.service.CourseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private CourseService courseService;

    @GetMapping("/login")
    public String showLoginPage() {
        return "admin-login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {
        Admin admin = adminService.authenticateAdmin(email, password);
        if (admin != null) {
            session.setAttribute("admin", admin);
            return "redirect:/admin/dashboard";
        }
        model.addAttribute("error", "Invalid credentials!");
        return "admin-login";
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) return "redirect:/admin/login";

        model.addAttribute("courses", courseService.getAllCourses());
        return "admin-dashboard";
    }

    @GetMapping("/courses/new")
    public String showAddForm(Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) return "redirect:/admin/login";
        model.addAttribute("course", new Course());
        return "addCourses";
    }

    @PostMapping("/courses")
    public String addCourse(@ModelAttribute Course course, HttpSession session) {
        if (session.getAttribute("admin") == null) return "redirect:/admin/login";
        courseService.saveCourse(course);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/courses/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) return "redirect:/admin/login";
        model.addAttribute("course", courseService.getCourseById(id));
        return "editCourses";
    }

    @PostMapping("/courses/update/{id}")
    public String updateCourse(@PathVariable Long id, @ModelAttribute Course courseDetails, HttpSession session) {
        if (session.getAttribute("admin") == null) return "redirect:/admin/login";
        courseService.updateCourse(id, courseDetails);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/courses/delete/{id}")
    public String deleteCourse(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("admin") == null) return "redirect:/admin/login";
        courseService.deleteCourse(id);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
