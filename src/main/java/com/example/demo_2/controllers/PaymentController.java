package com.example.demo_2.controllers;

import com.example.demo_2.entities.Payment;
import com.example.demo_2.repositories.PaymentRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    @GetMapping
    @PreAuthorize("hasAuthority('modify')")
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('read')")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        Optional<Payment> payment = paymentRepository.findById(id);
        return payment.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
}