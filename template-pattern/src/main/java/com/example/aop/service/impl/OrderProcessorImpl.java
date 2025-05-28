package com.example.aop.service.impl;

import com.example.aop.model.Order;
import com.example.aop.service.OrderProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderProcessorImpl implements OrderProcessor {

  private final PriceCalculationService priceCalculationService;
  private final ExternalValidationServiceProcessor validationService;
  private final EmailServiceProcessor emailService;
  private final AdobeSignServiceProcessor signatureService;
  private final SapIntegrationServiceProcessor sapService;

  @Override
  public void validateOrder(Order order) {
    validationService.setOrder(order);
    validationService.execute();
  }

  @Override
  public void sendEmail(Order order) {
    emailService.setOrder(order);
    emailService.execute();
  }

  @Override
  public void generateSignature(Order order) {
    signatureService.setOrder(order);
    signatureService.execute();
  }

  @Override
  public void calculatePrice(Order order) {
    priceCalculationService.setOrder(order);
    priceCalculationService.execute();
  }

  @Override
  public void sendToSAP(Order order) {
    sapService.setOrder(order);
    sapService.execute();
  }

  // Optional: Add a method to process entire order flow
  public void processOrder(Order order) {
    validateOrder(order);
    sendEmail(order);
    generateSignature(order);
    calculatePrice(order);
    sendToSAP(order);
  }
}
