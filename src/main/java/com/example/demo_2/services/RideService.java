package com.example.demo_2.services;

import com.example.demo_2.entities.Car;
import com.example.demo_2.entities.Payment;
import com.example.demo_2.entities.Ride;
import com.example.demo_2.repositories.CarRepository;
import com.example.demo_2.repositories.PaymentRepository;
import com.example.demo_2.repositories.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RideService {
    
    @Autowired
    private RideRepository rideRepository;
    
    @Autowired
    private CarRepository carRepository;
    
    @Autowired
    private PaymentRepository paymentRepository;
    public List<Car> findAvailableCars() {
        return carRepository.findByAvailableTrue();
    }
    
    @Transactional
    public Ride createRide(Ride rideRequest) {
        Optional<Car> carOpt = carRepository.findById(rideRequest.getCarId());
        if (carOpt.isEmpty()) {
            throw new RuntimeException("Машина не доступна с таким id: " + rideRequest.getCarId());
        }
        
        Car car = carOpt.get();
        if (!car.getAvailable()) {
            throw new RuntimeException("Машина не доступна");
        }
        
        Ride newRide = Ride.builder()
            .userId(rideRequest.getUserId())
            .carId(rideRequest.getCarId())
            .totalTime(0)
            .tarif(rideRequest.getTarif())
            .kilometrs(0.0)
            .status("Активна")
            .startTime(LocalDateTime.now())
            .build();
        
        Ride savedRide = rideRepository.save(newRide);
        

        car.setAvailable(false);
        carRepository.save(car);
        
        return savedRide;
    }
    
    @Transactional
    public Payment completeRide(Long rideId, Double finalKilometers) {
        
        Optional<Ride> rideOpt = rideRepository.findById(rideId);
        if (rideOpt.isEmpty()) {
            throw new RuntimeException("Поездка не найдена");
        }
        
        Ride ride = rideOpt.get();
        if (!"Активна".equals(ride.getStatus())) {
            throw new RuntimeException("Поездка не активна");
        }

        ride.setEndTime(LocalDateTime.now());
        ride.setTotalTime(ride.calculateTotalTime());
        ride.setKilometrs(finalKilometers);
        ride.setStatus("Завершена");
        rideRepository.save(ride);

        Optional<Car> carOpt = carRepository.findById(ride.getCarId());
        if (carOpt.isPresent()) {
            Car car = carOpt.get();
            car.setAvailable(true);
            carRepository.save(car);
        }

        Double amount = ride.calculateAmount();
        Payment payment = Payment.builder()
            .rideId(rideId)
            .userId(ride.getUserId())
            .amount(amount)
            .status("Оплачено")
            .createdAt(LocalDateTime.now())
            .build();
        
        return paymentRepository.save(payment);
    }
    
    @Transactional
    public Ride cancelRide(Long rideId) {
        Optional<Ride> rideOpt = rideRepository.findById(rideId);
        if (rideOpt.isEmpty()) {
            throw new RuntimeException("Поездка не найдена");
        }
        
        Ride ride = rideOpt.get();
        if (!"Активна".equals(ride.getStatus())) {
            throw new RuntimeException("Только активная поездка может быть отменена");
        }
        ride.setStatus("Отменена");
        ride.setEndTime(LocalDateTime.now());
        ride.setTotalTime(ride.calculateTotalTime());
        Ride updatedRide = rideRepository.save(ride);
        Optional<Car> carOpt = carRepository.findById(ride.getCarId());
        if (carOpt.isPresent()) {
            Car car = carOpt.get();
            car.setAvailable(true);
            carRepository.save(car);
        }
        
        return updatedRide;
    }
    public Double calculateEstimatedCost(Double tarif, Integer estimatedTime, Double estimatedKilometers) {
        return (estimatedTime * tarif) + (estimatedKilometers * 10.0);
    }
    
    public List<RideArchiveDTO> getUserRideArchive(Long userId) {
        List<Ride> userRides = rideRepository.findByUserId(userId);
        
        return userRides.stream()
            .map(ride -> {
                Double amount = paymentRepository.findByRideId(ride.getId())
                    .map(Payment::getAmount)
                    .orElse(0.0); 
                return new RideArchiveDTO(
                    ride.getId(),
                    ride.getCarId(),
                    ride.getStartTime(),
                    ride.getEndTime(),
                    ride.getTotalTime(),
                    ride.getKilometrs(),
                    ride.getStatus(),
                    amount 
                );
            })
            .collect(Collectors.toList());
    }
    

    public static class RideArchiveDTO {
        private Long rideId;
        private Long carId;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private Integer totalTime;
        private Double kilometers;
        private String status;
        private Double amount;
        
        public RideArchiveDTO(Long rideId, Long carId, LocalDateTime startTime, 
                             LocalDateTime endTime, Integer totalTime, Double kilometers,
                             String status, Double amount) {
            this.rideId = rideId;
            this.carId = carId;
            this.startTime = startTime;
            this.endTime = endTime;
            this.totalTime = totalTime;
            this.kilometers = kilometers;
            this.status = status;
            this.amount = amount;
        }
        

        public Long getRideId() { return rideId; }
        public Long getCarId() { return carId; }
        public LocalDateTime getStartTime() { return startTime; }
        public LocalDateTime getEndTime() { return endTime; }
        public Integer getTotalTime() { return totalTime; }
        public Double getKilometers() { return kilometers; }
        public String getStatus() { return status; }
        public Double getAmount() { return amount; }

        public void setRideId(Long rideId) { this.rideId = rideId; }
        public void setCarId(Long carId) { this.carId = carId; }
        public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
        public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
        public void setTotalTime(Integer totalTime) { this.totalTime = totalTime; }
        public void setKilometers(Double kilometers) { this.kilometers = kilometers; }
        public void setStatus(String status) { this.status = status; }
        public void setAmount(Double amount) { this.amount = amount; }
    }
}