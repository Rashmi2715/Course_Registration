package com.example.courseRegistration.repository;

import com.example.courseRegistration.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
