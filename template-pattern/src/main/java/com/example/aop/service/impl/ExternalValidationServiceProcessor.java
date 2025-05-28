package com.example.aop.service.impl;

import com.example.aop.model.Order;
import com.example.aop.model.RecoveryPoint;
import com.example.aop.service.AbstractOrderProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ExternalValidationServiceProcessor extends AbstractOrderProcessor {

  @Override
  public String getServiceRecoveryPoint() {
    return RecoveryPoint.INIT_VALIDATE_ORDER.getPoint();
  }

  @Override
  public void beforeProcess() {
    // Logic to execute before processing the order
    System.out.println("Starting external validation process...");
  }

  @Override
  public void processOrder() {
    // Logic to validate the order externally
    System.out.println("Validating order with external service...");
    // Simulate external validation logic here
  }

  @Override
  public void afterProcess() {
    // Logic to execute after processing the order
    System.out.println("External validation completed.");
  }

  @Override
  public void onSuccess() {
    // Logic to execute on successful validation
    System.out.println("Order validated successfully.");
  }

  @Override
  public void onError(Exception e) {
    // Logic to handle errors during validation
    System.err.println("Error during external validation: " + e.getMessage());
  }

  @Override
  public void onFinally() {
    // Cleanup or final actions after processing
    System.out.println("Finalizing external validation process.");
  }

  public void setOrder(Order order) {
    this.currentOrder = order;
  }
}
