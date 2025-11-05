package com.example.demo_2.controllers;

import com.example.demo_2.entities.Car;
import com.example.demo_2.repositories.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cars")
public class CarController {
    
    @Autowired
    private CarRepository carRepository;
    
    @PostMapping
    public ResponseEntity<Car> createCar(@RequestBody Car car) {
        try {
            Car savedCar = carRepository.save(car);
            return ResponseEntity.ok(savedCar);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        Optional<Car> car = carRepository.findById(id);
        return car.map(ResponseEntity::ok)
                 .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCar(@PathVariable Long id) {
        if (carRepository.existsById(id)) {
            carRepository.deleteById(id);
            return ResponseEntity.ok("Машина успешно удалена");
        }
        return ResponseEntity.notFound().build();
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable Long id, @RequestBody Car carDetails) {
        return carRepository.findById(id)
            .map(existingCar -> {
                existingCar.setModel(carDetails.getModel());
                existingCar.setNumber(carDetails.getNumber());
                Car updatedCar = carRepository.save(existingCar);
                return ResponseEntity.ok(updatedCar);
            })
            .orElse(ResponseEntity.notFound().build());
    }
}