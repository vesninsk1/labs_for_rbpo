package com.example.demo_2.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class Ride {
    private final Long id;
    private final Long userId;
    private final Long carId;
    private final Integer time;
    private final Double  tarif;
    private final Double kilometrs;
}
