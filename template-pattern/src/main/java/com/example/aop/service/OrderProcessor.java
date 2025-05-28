package com.example.aop.service;

import com.example.aop.model.Order;

public interface OrderProcessor {
    void validateOrder(Order order);
    void sendEmail(Order order);
    void generateSignature(Order order);
    void calculatePrice(Order order);
    void sendToSAP(Order order);
    
    default void process(Order order) {
        validateOrder(order);
        sendEmail(order);
        generateSignature(order);
        calculatePrice(order);
        sendToSAP(order);
    }
}
