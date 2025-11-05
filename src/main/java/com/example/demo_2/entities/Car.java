package com.example.demo_2.entities;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "cars")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String model;
    
    @Column(nullable = false, unique = true)
    private String number;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean available = true;
}