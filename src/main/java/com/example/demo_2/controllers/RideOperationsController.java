package com.example.demo_2.controllers;

import com.example.demo_2.entities.Payment;
import com.example.demo_2.entities.Ride;
import com.example.demo_2.services.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/operations/rides")
public class RideOperationsController {
    
    @Autowired
    private RideService rideService;
    
    @PostMapping("/start")
    public ResponseEntity<Ride> startRide(@RequestBody Ride rideRequest) {
        try {
            Ride ride = rideService.createRide(rideRequest);
            return ResponseEntity.ok(ride);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/complete")
    public ResponseEntity<Payment> completeRide(@RequestBody Map<String, Object> completionData) {
        try {
            Long rideId = Long.valueOf(completionData.get("rideId").toString());
            Double finalKilometers = Double.valueOf(completionData.get("finalKilometers").toString());
            
            Payment payment = rideService.completeRide(rideId, finalKilometers);
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @PostMapping("/cancel")
    public ResponseEntity<Ride> cancelRide(@RequestBody Map<String, Object> cancelData) {
        try {
            Long rideId = Long.valueOf(cancelData.get("rideId").toString());
            
            Ride ride = rideService.cancelRide(rideId);
            return ResponseEntity.ok(ride);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/user/{userId}/archive")
    public ResponseEntity<?> getUserRideArchive(@PathVariable Long userId) {
        try {
            List<RideService.RideArchiveDTO> archive = rideService.getUserRideArchive(userId);
            return ResponseEntity.ok(archive);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}