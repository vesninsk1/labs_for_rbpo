package com.example.demo_2.controllers;
import com.example.demo_2.entities.Car;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/cars")
public class CarController {
    
    private final List<Car> cars = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);
    
    @PostMapping
    public Car createCar(@RequestBody Car car) {
        Car newCar = Car.builder()
            .id(idCounter.getAndIncrement())
            .model(car.getModel())
            .number(car.getNumber())
            .build();
        cars.add(newCar);
        return newCar;
    }
    
    @GetMapping
    public List<Car> getAllCars() {
        return cars;
    }
    
    @GetMapping("/{id}")
    public Car getCarById(@PathVariable Long id) {
        return cars.stream()
            .filter(car -> car.getId().equals(id))
            .findFirst()
            .orElse(null);
    }
    
    @DeleteMapping("/{id}")
    public String deleteCar(@PathVariable Long id) {
        cars.removeIf(car -> car.getId().equals(id));
        return "Car deleted";
    }
    
    @PutMapping("/{id}")
    public Car updateCar(@PathVariable Long id, @RequestBody Car car) {
        deleteCar(id);
        Car updatedCar = Car.builder()
            .id(id)
            .model(car.getModel())
            .number(car.getNumber())
            .build();
        cars.add(updatedCar);
        return updatedCar;
    }
}