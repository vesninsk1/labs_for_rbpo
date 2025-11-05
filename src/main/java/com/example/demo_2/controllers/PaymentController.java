package com.example.demo_2.controllers;
import com.example.demo_2.entities.Payment;
import com.example.demo_2.entities.Ride;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    
    private final List<Payment> payments = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);
    private final RideController rideController;
    
    public PaymentController(RideController rideController) {
        this.rideController = rideController;
    }
    @PostMapping
    public Payment createPayment(@RequestBody Payment paymentRequest) {
        Ride ride = rideController.getRideById(paymentRequest.getRideId());
        if (ride == null) {
            throw new RuntimeException("Ride not found with id: " + paymentRequest.getRideId());
        }
        
        Double amount = (ride.getTime() * ride.getTarif()) + (ride.getKilometrs() * 10);
        
        Payment newPayment = Payment.builder()
            .id(idCounter.getAndIncrement())
            .rideId(paymentRequest.getRideId())
            .userId(paymentRequest.getUserId())
            .amount(amount) 
            .status(paymentRequest.getStatus())
            .build();
        payments.add(newPayment);
        return newPayment;
    }
    
    @GetMapping
    public List<Payment> getAllPayments() {
        return payments;
    }
    
    @GetMapping("/{id}")
    public Payment getPaymentById(@PathVariable Long id) {
        return payments.stream()
            .filter(payment -> payment.getId().equals(id))
            .findFirst()
            .orElse(null);
    }

    @DeleteMapping("/{id}")
    public String deletePayment(@PathVariable Long id) {
        payments.removeIf(payment -> payment.getId().equals(id));
        return "Payment deleted";
    }

    @PutMapping("/{id}")
    public Payment updatePayment(@PathVariable Long id, @RequestBody Payment payment) {
        deletePayment(id);
        Payment updatedPayment = Payment.builder()
            .id(id)
            .rideId(payment.getRideId())
            .userId(payment.getUserId())
            .amount(payment.getAmount())
            .status(payment.getStatus())
            .build();
        payments.add(updatedPayment);
        return updatedPayment;
    }
}