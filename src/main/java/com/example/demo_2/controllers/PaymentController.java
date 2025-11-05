package com.example.demo_2.controllers;

import com.example.demo_2.entities.Payment;
import com.example.demo_2.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    
    @Autowired
    private PaymentRepository paymentRepository;
    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        Optional<Payment> payment = paymentRepository.findById(id);
        return payment.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

}