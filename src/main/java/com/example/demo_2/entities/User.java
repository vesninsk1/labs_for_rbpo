package com.example.demo_2.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class User {
    private final Long id;
    private final String name;
    private final String email;
    private final String credit_card;
}