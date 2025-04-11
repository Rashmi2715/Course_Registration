/*package com.example.courseRegistration.controller;

import com.example.courseRegistration.model.Course;
import com.example.courseRegistration.repository.CourseRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    // Display list of courses at URL: /courses
    @GetMapping("")
    public String listCourses(Model model) {
        model.addAttribute("courses", courseRepository.findAll());
        return "listCourses";
    }

    // Show form to add a new course at URL: /courses/new
    @GetMapping("/new")
    public String showAddForm(Course course) {
        return "addCourses";
    }

    // Save a new course at URL: /courses (POST)
    @PostMapping("")
    public String addCourse(@Valid Course course, BindingResult result) {
        if (result.hasErrors()) {
            return "addCourses";
        }
        courseRepository.save(course);
        return "redirect:/courses";
    }

    // Show form to update an existing course at URL: /courses/edit/{id}
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));
        model.addAttribute("course", course);
        return "editCourses";
    }

    // Update the course at URL: /courses/update/{id}
    @PostMapping("/update/{id}")
    public String updateCourse(@PathVariable("id") long id, @Valid Course course,
                               BindingResult result) {
        if (result.hasErrors()) {
            course.setId(id);
            return "editCourses";
        }
        courseRepository.save(course);
        return "redirect:/courses";
    }

    // Delete a course at URL: /courses/delete/{id}
    @GetMapping("/delete/{id}")
    public String deleteCourse(@PathVariable("id") long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));
        courseRepository.delete(course);
        return "redirect:/courses";
    }
}
*/

package com.example.courseRegistration.controller;

import com.example.courseRegistration.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("")
    public String listCourses(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());
        return "listCourses";
    }
}
