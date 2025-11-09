package com.example.demo_2.controllers;

import com.example.demo_2.entities.Car;
import com.example.demo_2.services.RideService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/operations")
@RequiredArgsConstructor
public class CarOperationsController {
    
    @Autowired
    private RideService rideService;
    
    @GetMapping("/cars/available")
    @PreAuthorize("hasAuthority('read')")
    public List<Car> getAvailableCars() {
        return rideService.findAvailableCars();
    }
    
    @GetMapping("/calculate-cost")
    @PreAuthorize("hasAuthority('read')")
    public ResponseEntity<Double> calculateEstimatedCost(
            @RequestParam Double tarif,
            @RequestParam Integer estimatedTime,
            @RequestParam Double estimatedKilometers) {
        try {
            Double cost = rideService.calculateEstimatedCost(tarif, estimatedTime, estimatedKilometers);
            return ResponseEntity.ok(cost);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}