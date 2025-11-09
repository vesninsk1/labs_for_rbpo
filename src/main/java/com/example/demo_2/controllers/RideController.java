package com.example.demo_2.controllers;

import com.example.demo_2.entities.Ride;
import com.example.demo_2.repositories.RideRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rides")
@RequiredArgsConstructor
public class RideController {
    
    @Autowired
    private RideRepository rideRepository;
    
    @GetMapping
    @PreAuthorize("hasAuthority('modify')")
    public List<Ride> getAllRides() {
        return rideRepository.findAll();
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('modify')")
    public ResponseEntity<Ride> getRideById(@PathVariable Long id) {
        Optional<Ride> ride = rideRepository.findById(id);
        return ride.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }
}