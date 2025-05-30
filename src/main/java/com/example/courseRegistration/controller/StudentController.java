package com.example.courseRegistration.controller;

import java.util.List;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.courseRegistration.model.Course;
import com.example.courseRegistration.model.Enrollment;
import com.example.courseRegistration.model.Student;
import com.example.courseRegistration.repository.CourseRepository;
import com.example.courseRegistration.repository.EnrollmentRepository;
import com.example.courseRegistration.repository.StudentRepository;
import com.example.courseRegistration.service.CourseService;
import com.example.courseRegistration.service.StudentService;
import com.example.courseRegistration.strategy.PasswordStrategy;
import com.example.courseRegistration.strategy.SimplePasswordStrategy;
import com.example.courseRegistration.util.UserFactory;

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

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    // Injecting the password strategy
    private final PasswordStrategy passwordStrategy = new SimplePasswordStrategy();

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

        if (!passwordStrategy.match(password, confirmPassword)) {
            model.addAttribute("error", "Passwords do not match! Please try again.");
            return "student-signup";
        }

        /*Student student = new Student(fullname, email, password);
        studentService.registerStudent(student);
        return "redirect:/student/login";
        */
        Student student = new Student();
        student.setFullname(fullname);
        student.setEmail(email);
        student.setPassword(password);


        studentService.registerStudent(student);
        return "redirect:/student/login"; 
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "student-login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password,
                        HttpSession session, Model model) {
        Student student = studentRepository.findByEmail(email);
        if (student != null && passwordStrategy.match(password, student.getPassword())) {
            session.setAttribute("student", student);
            return "redirect:/student/dashboard";
        }
        model.addAttribute("error", "Invalid credentials!");
        return "student-login";
    }

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        Student student = (Student) session.getAttribute("student");
        if (student == null) {
            return "redirect:/student/login";
        }

        model.addAttribute("courses", studentService.getAllCourses());
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
        if (!passwordStrategy.match(newPassword, confirmPassword)) {
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

    @GetMapping("/courses")
    public String browseCourses(HttpSession session, Model model) {
        Student student = (Student) session.getAttribute("student");
        if (student == null) {
            return "redirect:/student/login";
        }

        model.addAttribute("courses", studentService.getAllCourses());
        return "student-dashboard";
    }

    @GetMapping("/enroll/{courseId}")
    public String enrollInCourse(@PathVariable Long courseId, HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        Student sessionStudent = (Student) session.getAttribute("student");
        if (sessionStudent == null) {
            return "redirect:/student/login";
        }

        Student student = studentRepository.findByEmail(sessionStudent.getEmail());
        Course course = courseService.getCourseById(courseId);

        boolean alreadyEnrolled = enrollmentRepository.findByStudentAndCourse(student, course) != null;

        if (!alreadyEnrolled) {
            Enrollment enrollment = new Enrollment();
            enrollment.setStudent(student);
            enrollment.setCourse(course);
            enrollment.setStatus("Pending");
            enrollmentRepository.save(enrollment);

            redirectAttributes.addFlashAttribute("message", "Successfully enrolled in " + course.getName() + "!");
        } else {
            redirectAttributes.addFlashAttribute("message", "You are already enrolled in " + course.getName() + ".");
        }

        return "redirect:/student/enrollments";
    }

    @GetMapping("/enrollments")
    public String viewEnrollments(HttpSession session, Model model) {
        Student student = (Student) session.getAttribute("student");
        if (student == null)
            return "redirect:/student/login";

        List<Enrollment> enrollments = enrollmentRepository.findByStudent(student);
        model.addAttribute("enrollments", enrollments);

        return "student-enrollments";
    }

    @PostMapping("/student/enrollment/{id}/remove")
    public String removeEnrollment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Enrollment enrollment = enrollmentRepository.findById(id).orElse(null);

        if (enrollment != null &&
            ("Admin Disapproved".equalsIgnoreCase(enrollment.getStatus()) ||
             "Pending".equalsIgnoreCase(enrollment.getStatus()) ||
             "Admin Approved".equalsIgnoreCase(enrollment.getStatus()))) {
            enrollmentRepository.delete(enrollment);
            redirectAttributes.addFlashAttribute("message", "Enrollment removed successfully.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Only certain enrollments can be removed.");
        }

        return "redirect:/student/enrollments";
    }

    @PostMapping("/student/enrollment/{id}/confirm")
    public String confirmEnrollment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Enrollment enrollment = enrollmentRepository.findById(id).orElse(null);
        if (enrollment != null) {
            enrollment.setStatus("Pending");
            enrollmentRepository.save(enrollment);
        }
        return "redirect:/student/my-enrollments";
    }
}
