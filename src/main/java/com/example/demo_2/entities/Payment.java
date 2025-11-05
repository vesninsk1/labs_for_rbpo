package com.example.demo_2.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class Payment {
    private final Long id;
    private final Long rideId;
    private final Long userId;
    private final Double amount;
    private final String status;
}