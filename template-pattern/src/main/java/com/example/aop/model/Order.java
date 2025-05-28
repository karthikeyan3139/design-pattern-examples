package com.example.aop.model;

import lombok.Data;

@Data
public class Order {
    private String orderId;
    private OrderState state;
    private String recoveryPoint;
    private double price;
    // Add other necessary fields
}
