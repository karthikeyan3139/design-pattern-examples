package com.example.aop.controller;

import com.example.aop.model.Order;
import com.example.aop.service.OrderProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

  @Autowired private OrderProcessor orderProcessor;

  @GetMapping("/create")
  public String createOrder() {
    Order order = new Order();
    order.setOrderId("12345");

    orderProcessor.process(order);
    return "Order created successfully";
  }
}
