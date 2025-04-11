package com.example.courseRegistration.controller;

import java.util.List;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.courseRegistration.model.Course;
import com.example.courseRegistration.model.Enrollment;
import com.example.courseRegistration.model.Student;
import com.example.courseRegistration.service.CourseService;
import com.example.courseRegistration.service.StudentService;
import jakarta.servlet.http.HttpSession;

import com.example.courseRegistration.repository.CourseRepository;
import com.example.courseRegistration.repository.EnrollmentRepository;
import com.example.courseRegistration.repository.StudentRepository;

//import java.security.Principal;

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

    // Add inside StudentController
    /*
     * @GetMapping("/enroll/{courseId}")
     * public String enrollCourse(@PathVariable Long courseId, HttpSession session,
     * Model model) {
     * Student student = (Student) session.getAttribute("student");
     * if (student == null) {
     * return "redirect:/student/login";
     * }
     * 
     * boolean enrolled = studentService.enrollInCourse(student.getId(), courseId);
     * if (enrolled) {
     * model.addAttribute("message", "Successfully enrolled in course!");
     * } else {
     * model.addAttribute("error", "Enrollment failed or already enrolled.");
     * }
     * 
     * model.addAttribute("courses", studentService.getAllCourses());
     * return "listCourses";
     * }
     * 
     * @GetMapping("/courses")
     * public String browseCourses(HttpSession session, Model model) {
     * Student student = (Student) session.getAttribute("student");
     * if (student == null) {
     * return "redirect:/student/login";
     * }
     * 
     * model.addAttribute("courses", studentService.getAllCourses());
     * return "listCoursesForStudent"; // New file for student view
     * }
     * 
     * }
     */

    @GetMapping("/courses")
    public String browseCourses(HttpSession session, Model model) {
        Student student = (Student) session.getAttribute("student");
        if (student == null) {
            return "redirect:/student/login";
        }

        model.addAttribute("courses", studentService.getAllCourses());
        return "student-browse-courses"; // This should be the name of the HTML template
    }

    /*
     * @GetMapping("/enroll/{courseId}")
     * public String enrollInCourse(@PathVariable Long courseId, HttpSession
     * session, RedirectAttributes redirectAttributes) {
     * Student sessionStudent = (Student) session.getAttribute("student");
     * if (sessionStudent == null) {
     * return "redirect:/student/login";
     * }
     * 
     * // Re-fetch the student with courses loaded
     * Student student =
     * studentRepository.findByEmailWithCourses(sessionStudent.getEmail());
     * 
     * Course course = courseService.getCourseById(courseId);
     * 
     * if (!student.getCourses().contains(course)) {
     * student.getCourses().add(course);
     * studentService.saveStudent(student);
     * redirectAttributes.addFlashAttribute("message", "Successfully enrolled in " +
     * course.getName() + "!");
     * } else {
     * redirectAttributes.addFlashAttribute("message",
     * "You are already enrolled in " + course.getName() + ".");
     * }
     * 
     * // Update session with the updated student object
     * session.setAttribute("student", student);
     * 
     * return "redirect:/student/student-my-courses";
     * }
     * 
     * 
     * @GetMapping("/student-my-courses")
     * public String viewEnrolledCourses(HttpSession session, Model model) {
     * Student student = (Student) session.getAttribute("student");
     * 
     * if (student == null) {
     * return "redirect:/student/login";
     * }
     * 
     * // Re-fetch the student with enrolled courses from DB
     * Student fullStudent =
     * studentRepository.findByEmailWithCourses(student.getEmail());
     * System.out.println("Enrolled courses for student: " +
     * fullStudent.getCourses().size());
     * for (Course c : fullStudent.getCourses()) {
     * System.out.println("-> " + c.getName());
     * }
     * model.addAttribute("enrolledCourses", fullStudent.getCourses());
     * //return "student-my-courses";
     * return "student-my-courses";
     * 
     * }
     */

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CourseRepository courseRepository; // If not already available

    /*
     * WORKING ONE
     * 
     * @GetMapping("/enroll/{courseId}")
     * public String enrollInCourse(@PathVariable Long courseId, HttpSession
     * session, RedirectAttributes redirectAttributes) {
     * Student sessionStudent = (Student) session.getAttribute("student");
     * if (sessionStudent == null) {
     * return "redirect:/student/login";
     * }
     * // Re-fetch the student with courses loaded
     * Student student =
     * studentRepository.findByEmailWithCourses(sessionStudent.getEmail());
     * 
     * Course course = courseService.getCourseById(courseId);
     * 
     * if (!student.getCourses().contains(course)) {
     * student.getCourses().add(course);
     * studentService.saveStudent(student);
     * redirectAttributes.addFlashAttribute("message", "Successfully enrolled in " +
     * course.getName() + "!");
     * } else {
     * redirectAttributes.addFlashAttribute("message",
     * "You are already enrolled in " + course.getName() + ".");
     * }
     * 
     * // Update session with the updated student object
     * session.setAttribute("student", student);
     * 
     * return "student-my-courses";
     * }
     * 
     */
    @GetMapping("/enroll/{courseId}")
    public String enrollInCourse(@PathVariable Long courseId, HttpSession session,
            RedirectAttributes redirectAttributes) {
        Student sessionStudent = (Student) session.getAttribute("student");
        if (sessionStudent == null) {
            return "redirect:/student/login";
        }

        Student student = studentRepository.findByEmail(sessionStudent.getEmail());
        Course course = courseService.getCourseById(courseId);

        // Check if already enrolled
        boolean alreadyEnrolled = enrollmentRepository.findByStudentAndCourse(student, course) != null;

        if (!alreadyEnrolled) {
            Enrollment enrollment = new Enrollment();
            enrollment.setStudent(student);
            enrollment.setCourse(course);
            enrollment.setStatus("Pending"); // or "Confirmed" if you prefer
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

    /*
     * @PostMapping("/enrollments/{id}/confirm")
     * public String confirmEnrollment(@PathVariable Long id) {
     * Enrollment enrollment = enrollmentRepository.findById(id).orElse(null);
     * if (enrollment != null) {
     * enrollment.setStatus("Confirmed");
     * enrollmentRepository.save(enrollment);
     * }
     * return "redirect:/student-enrollments";
     * }
     * 
     * @PostMapping("/enrollments/{id}/remove")
     * public String removeEnrollment(@PathVariable Long id) {
     * enrollmentRepository.deleteById(id);
     * return "redirect:/student-enrollments";
     * }
     */
    @Controller
    public class EnrollmentController {

        @Autowired
        private EnrollmentRepository enrollmentRepository;

        @PostMapping("/student/enrollment/{id}/remove")
        public String removeEnrollment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
            Enrollment enrollment = enrollmentRepository.findById(id).orElse(null);
            if (enrollment != null && "Pending".equalsIgnoreCase(enrollment.getStatus())) {
                enrollmentRepository.delete(enrollment);
                redirectAttributes.addFlashAttribute("message", "Enrollment removed successfully.");
            } else {
                redirectAttributes.addFlashAttribute("error", "Only pending enrollments can be removed.");
            }
            return "redirect:/student/enrollments";
        }

    }

    @PostMapping("/student/enrollment/{id}/confirm")
    public String confirmEnrollment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Enrollment enrollment = enrollmentRepository.findById(id).orElse(null);
        if (enrollment != null) {
            if ("Confirmed".equalsIgnoreCase(enrollment.getStatus())) {
                redirectAttributes.addFlashAttribute("error", "Payment already confirmed for this course.");
            } else {
                enrollment.setStatus("Confirmed");
                enrollmentRepository.save(enrollment);
                redirectAttributes.addFlashAttribute("message", "Payment confirmed successfully.");
            }
        }
        return "redirect:/student-enrollments";
    }

}
