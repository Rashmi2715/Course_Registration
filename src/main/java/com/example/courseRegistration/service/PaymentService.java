package com.example.courseRegistration.service;

import com.example.courseRegistration.model.Enrollment;
import com.example.courseRegistration.model.Payment;
import com.example.courseRegistration.repository.EnrollmentRepository;
import com.example.courseRegistration.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Transactional
    public Payment makePayment(Long enrollmentId, String upiId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId).orElse(null);
        if (enrollment == null) {
            throw new IllegalArgumentException("Enrollment not found for ID: " + enrollmentId);
        }

        Payment payment = new Payment();
        payment.setEnrollment(enrollment);
        payment.setAmount(enrollment.getCourse().getPrice());
        payment.setUpiId(upiId);
        payment.setStatus("Paid");

        Payment savedPayment = paymentRepository.save(payment);

        enrollment.setStatus("Confirmed");
        enrollmentRepository.save(enrollment);

        return savedPayment;
    }
}
