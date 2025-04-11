package com.example.courseRegistration.controller;

import com.example.courseRegistration.model.Enrollment;
import com.example.courseRegistration.model.Payment;
import com.example.courseRegistration.repository.EnrollmentRepository;
import com.example.courseRegistration.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.text.SimpleDateFormat;

import java.util.UUID;

import java.math.BigDecimal;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/form/{enrollmentId}")
    public String showPaymentForm(@PathVariable Long enrollmentId, Model model) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId).orElse(null);
        if (enrollment == null) {
            return "error";
        }

        BigDecimal amount = enrollment.getCourse().getPrice();

        model.addAttribute("enrollmentId", enrollmentId);
        model.addAttribute("amount", amount);
        return "payment_form";

    }

    @RequestMapping(value = "/processing", method = { RequestMethod.GET, RequestMethod.POST })

    public String processPayment(@RequestParam("enrollmentId") int enrollmentId,
            @RequestParam("upiId") String upiId,
            RedirectAttributes redirectAttributes) {
        // Do your payment processing logic here...

        // Add parameters to redirect
        redirectAttributes.addAttribute("enrollmentId", enrollmentId);
        redirectAttributes.addAttribute("upiId", upiId);
        redirectAttributes.addAttribute("enrollmentId", enrollmentId);
        redirectAttributes.addAttribute("upiId", upiId);

        return "redirect:/payment/success";
    }

    @PostMapping("/process/submit")
    public String submitPaymentProcessing(@RequestParam Long enrollmentId,
            @RequestParam String upiId,
            Model model) {
        model.addAttribute("enrollmentId", enrollmentId);
        model.addAttribute("upiId", upiId);
        return "payment-processing";
    }

    @GetMapping("/success")
    public String showPaymentSuccess(@RequestParam Long enrollmentId,
            @RequestParam String upiId,
            Model model) {
        try {
            Payment payment = paymentService.makePayment(enrollmentId, upiId);

            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String randomPart = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
            String transactionId = "TXN" + timeStamp + randomPart;

            model.addAttribute("payment", payment);
            model.addAttribute("transactionId", transactionId);
            model.addAttribute("timestamp", new Date());

            return "payment_success";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "payment_form";
        }
    }

}
