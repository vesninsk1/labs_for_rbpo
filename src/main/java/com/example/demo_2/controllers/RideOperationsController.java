package com.example.demo_2.controllers;

import com.example.demo_2.entities.Payment;
import com.example.demo_2.entities.Ride;
import com.example.demo_2.services.RideService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/operations/rides")
@RequiredArgsConstructor
public class RideOperationsController {
    
    private final RideService rideService;
    
    @PostMapping("/start")
    @PreAuthorize("hasAuthority('read')")
    public ResponseEntity<?> startRide(@RequestBody Map<String, Object> rideRequest) {
        try {
            System.out.println("=== DEBUG Starting ride ===");
            System.out.println("Request body: " + rideRequest);
            if (!rideRequest.containsKey("userId") || !rideRequest.containsKey("carId") || !rideRequest.containsKey("tarif")) {
                return ResponseEntity.badRequest().body("Ошибка: обязательные поля (userId, carId, tarif) отсутствуют");
            }
            
            Long userId = null;
            Long carId = null;
            Double tarif = null;
            
            try {
                userId = Long.valueOf(rideRequest.get("userId").toString());
                carId = Long.valueOf(rideRequest.get("carId").toString());
                tarif = Double.valueOf(rideRequest.get("tarif").toString());
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body("Ошибка: неверный формат числовых полей");
            }
            
            if (tarif <= 0) {
                return ResponseEntity.badRequest().body("Ошибка: тариф должен быть положительным числом");
            }
            
            if (userId <= 0) {
                return ResponseEntity.badRequest().body("Ошибка: userId должен быть положительным числом");
            }
            
            if (carId <= 0) {
                return ResponseEntity.badRequest().body("Ошибка: carId должен быть положительным числом");
            }
            
            Ride ride = Ride.builder()
                    .userId(userId)
                    .carId(carId)
                    .tarif(tarif)
                    .kilometrs(0.0)  
                    .totalTime(0)    
                    .status("ACTIVE")
                    .build();
            
            Ride createdRide = rideService.createRide(ride);
            return ResponseEntity.ok(createdRide);
            
        } catch (Exception e) {
            System.out.println("Error starting ride: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body("Ошибка при начале поездки: " + e.getMessage());
        }
    }

    @PostMapping("/complete")
    @PreAuthorize("hasAuthority('read')")
    public ResponseEntity<?> completeRide(@RequestBody Map<String, Object> completionData) {
        try {
            System.out.println("=== DEBUG Completing ride ===");
            System.out.println("Request body: " + completionData);
            
            if (!completionData.containsKey("rideId") || !completionData.containsKey("finalKilometers")) {
                return ResponseEntity.badRequest().body("Ошибка: обязательные поля (rideId, finalKilometers) отсутствуют");
            }
            
            Long rideId = null;
            Double finalKilometers = null;
            
            try {
                rideId = Long.valueOf(completionData.get("rideId").toString());
                finalKilometers = Double.valueOf(completionData.get("finalKilometers").toString());
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body("Ошибка: неверный формат числовых полей");
            }
            
            if (rideId <= 0) {
                return ResponseEntity.badRequest().body("Ошибка: rideId должен быть положительным числом");
            }
            
            if (finalKilometers <= 0) {
                return ResponseEntity.badRequest().body("Ошибка: финальный пробег должен быть положительным числом");
            }
            
            Payment payment = rideService.completeRide(rideId, finalKilometers);
            return ResponseEntity.ok(payment);
            
        } catch (Exception e) {
            System.out.println("Error completing ride: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body("Ошибка при завершении поездки: " + e.getMessage());
        }
    }
    
    @PostMapping("/cancel")
    @PreAuthorize("hasAuthority('read')")
    public ResponseEntity<?> cancelRide(@RequestBody Map<String, Object> cancelData) {
        try {
            System.out.println("=== DEBUG Canceling ride ===");
            System.out.println("Request body: " + cancelData);
            
            if (!cancelData.containsKey("rideId")) {
                return ResponseEntity.badRequest().body("Ошибка: обязательное поле rideId отсутствует");
            }
            
            Long rideId = null;
            
            try {
                rideId = Long.valueOf(cancelData.get("rideId").toString());
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body("Ошибка: неверный формат rideId");
            }
            
            if (rideId <= 0) {
                return ResponseEntity.badRequest().body("Ошибка: rideId должен быть положительным числом");
            }
            
            Ride ride = rideService.cancelRide(rideId);
            return ResponseEntity.ok(ride);
            
        } catch (Exception e) {
            System.out.println("Error canceling ride: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body("Ошибка при отмене поездки: " + e.getMessage());
        }
    }
    
    @GetMapping("/user/{userId}/archive")
    @PreAuthorize("hasAuthority('read')")
    public ResponseEntity<?> getUserRideArchive(@PathVariable Long userId) {
        try {
            System.out.println("=== DEBUG Getting ride archive for user: " + userId);
            
            if (userId <= 0) {
                return ResponseEntity.badRequest().body("Ошибка: userId должен быть положительным числом");
            }
            
            List<RideService.RideArchiveDTO> archive = rideService.getUserRideArchive(userId);
            return ResponseEntity.ok(archive);
            
        } catch (Exception e) {
            System.out.println("Error getting ride archive: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body("Ошибка при получении архива поездок: " + e.getMessage());
        }
    }
}