package com.example.demo_2.entities;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.Duration;

@Entity
@Table(name = "rides")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "car_id", nullable = false)
    private Long carId;
    
    @Column(name = "total_time") 
    @Builder.Default
    private Integer totalTime = 0;
    
    @Column(nullable = false)
    private Double tarif;
    
    @Column(nullable = false)
    @Builder.Default
    private Double kilometrs = 0.0;
    
    @Column(nullable = false)
    @Builder.Default
    private String status = "ACTIVE";
    
    @Column(name = "start_time")
    @Builder.Default
    private LocalDateTime startTime = LocalDateTime.now();
    
    @Column(name = "end_time")
    private LocalDateTime endTime;
    
    public Integer calculateTotalTime() {
        if (startTime != null && endTime != null) {
            Duration duration = Duration.between(startTime, endTime);
            return (int) duration.toMinutes();
        }
        return 0;
    }
    
    public Double calculateAmount() {
        Integer time = (totalTime != null) ? totalTime : calculateTotalTime();
        return (time * tarif) + (kilometrs * 10.0);
    }
}