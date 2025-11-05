package com.example.demo_2.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class Car {
    private final Long id;
    private final String model;
    private final String number;
}
