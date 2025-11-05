package com.example.demo_2.controllers;

import com.example.demo_2.entities.Ride;
import com.example.demo_2.repositories.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rides")
public class RideController {
    
    @Autowired
    private RideRepository rideRepository;
    
    @GetMapping
    public List<Ride> getAllRides() {
        return rideRepository.findAll();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Ride> getRideById(@PathVariable Long id) {
        Optional<Ride> ride = rideRepository.findById(id);
        return ride.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }
}