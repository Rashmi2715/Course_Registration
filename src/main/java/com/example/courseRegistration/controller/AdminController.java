package com.example.courseRegistration.controller;

import com.example.courseRegistration.model.Admin;
import com.example.courseRegistration.model.Course;
import com.example.courseRegistration.model.Enrollment;
import com.example.courseRegistration.repository.CourseRepository;
import com.example.courseRegistration.repository.EnrollmentRepository;
import com.example.courseRegistration.service.AdminService;
import com.example.courseRegistration.service.CourseService;
import com.example.courseRegistration.strategy.PasswordStrategy;
import com.example.courseRegistration.strategy.SimplePasswordStrategy;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepository courseRepository; // 🔧 FIXED: Added missing injection

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    private final PasswordStrategy passwordStrategy = new SimplePasswordStrategy();

    @GetMapping("/login")
    public String showLoginPage() {
        return "admin-login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password,
                        HttpSession session, Model model) {
        Admin admin = adminService.authenticateAdmin(email, password);
        if (admin != null && passwordStrategy.match(password, admin.getPassword())) {
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

    @GetMapping("/requests")
    public String viewRequests(Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) return "redirect:/admin/login";

        List<Enrollment> pendingEnrollments = enrollmentRepository.findPendingWithStudents();
        model.addAttribute("requests", pendingEnrollments);
        return "admin-requests";
    }

    @PostMapping("/request/accept")
    public String acceptRequest(@RequestParam Long enrollmentId, RedirectAttributes redirectAttributes) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId).orElse(null);
        if (enrollment != null) {
            Course course = enrollment.getCourse();
            long currentEnrollments = enrollmentRepository.countByCourseAndStatus(course, "Admin Approved");
            if (currentEnrollments < course.getMaxStudents()) {
                enrollment.setStatus("Admin Approved");
                enrollmentRepository.save(enrollment);
                redirectAttributes.addFlashAttribute("message", "Enrollment approved successfully.");
            } else {
                redirectAttributes.addFlashAttribute("error", "Course has reached max capacity.");
                redirectAttributes.addFlashAttribute("courseId", course.getId());
            }
        }
        return "redirect:/admin/requests";
    }

    @PostMapping("/course/increase")
    public String increaseCourseCapacity(@RequestParam Long courseId, @RequestParam int newLimit,
                                         RedirectAttributes redirectAttributes) {
        Course course = courseService.getCourseById(courseId);
        if (course != null && newLimit > course.getMaxStudents()) {
            course.setMaxStudents(newLimit);
            courseService.saveCourse(course);
            redirectAttributes.addFlashAttribute("message", "Course capacity updated successfully.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid new limit. It must be greater than current max.");
        }
        return "redirect:/admin/requests";
    }

    @PostMapping("/request/reject")
    public String rejectRequest(@RequestParam Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId).orElse(null);
        if (enrollment != null) {
            enrollment.setStatus("Admin Disapproved");
            enrollmentRepository.save(enrollment);
        }
        return "redirect:/admin/requests";
    }

    // 🔍 View confirmed enrollments by course
    @GetMapping("/enrollments")
    public String showEnrollmentPage(Model model) {
        List<Course> courses = courseRepository.findAll();
        model.addAttribute("courses", courses);
        return "admin-enrollments";
    }

    @PostMapping("/enrollments")
    public String filterEnrollments(@RequestParam("courseId") Long courseId, Model model) {
    List<Course> courses = courseRepository.findAll();
    model.addAttribute("courses", courses);
    model.addAttribute("selectedCourseId", courseId);

    List<Enrollment> enrollments = enrollmentRepository.findByCourseIdAndStatus(courseId, "Confirmed");

    if (enrollments.isEmpty()) {
        model.addAttribute("noEnrollmentsMessage", "No one is enrolled in this course.");
    } else {
        model.addAttribute("enrollments", enrollments);
    }

    return "admin-enrollments";
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
