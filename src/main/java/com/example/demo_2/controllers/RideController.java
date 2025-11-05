package com.example.demo_2.controllers;

import com.example.demo_2.entities.Ride;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/rides")
public class RideController {
    
    private final List<Ride> rides = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    @PostMapping
    public Ride createRide(@RequestBody Ride ride) {
        Ride newRide = Ride.builder()
            .id(idCounter.getAndIncrement())
            .userId(ride.getUserId())
            .carId(ride.getCarId())
            .time(ride.getTime())
            .tarif(ride.getTarif())
            .kilometrs(ride.getKilometrs())
            .build();
        rides.add(newRide);
        return newRide;
    }

    @GetMapping
    public List<Ride> getAllRides() {
        return rides;
    }

    
    @GetMapping("/{id}")
    public Ride getRideById(@PathVariable Long id) {
        return rides.stream()
            .filter(ride -> ride.getId().equals(id))
            .findFirst()
            .orElse(null);
    }

    
    @DeleteMapping("/{id}")
    public String deleteRide(@PathVariable Long id) {
        rides.removeIf(ride -> ride.getId().equals(id));
        return "Ride deleted";
    }

    
    @PutMapping("/{id}")
    public Ride updateRide(@PathVariable Long id, @RequestBody Ride ride) {
        deleteRide(id);
        Ride updatedRide = Ride.builder()
            .id(id)
            .userId(ride.getUserId())
            .carId(ride.getCarId())
            .time(ride.getTime())
            .tarif(ride.getTarif())
            .kilometrs(ride.getKilometrs())
            .build();
        rides.add(updatedRide);
        return updatedRide;
    }
}
