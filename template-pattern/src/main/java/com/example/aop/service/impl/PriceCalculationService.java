package com.example.aop.service.impl;

import com.example.aop.model.Order;
import com.example.aop.model.OrderState;
import com.example.aop.model.RecoveryPoint;
import com.example.aop.service.AbstractOrderProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PriceCalculationService extends AbstractOrderProcessor {

  @Override
  public String getServiceRecoveryPoint() {
    return RecoveryPoint.INIT_CALCULATE_PRICE.name();
  }

  @Override
  public void processOrder() {
    log.info("Processing price calculation at recovery point: {}", getRecoveryPoint());
    // Process logic
    // Simulate price calculation
    double calculatedPrice = 100.00; // Example price
    currentOrder.setPrice(calculatedPrice);
    currentOrder.setState(OrderState.PRICE_CALCULATED);
  }

  @Override
  public void beforeProcess() {
    super.beforeProcess();
    // Validate prerequisites before processing
    log.info("Validating price calculation prerequisites");
    if (currentOrder.getState() != OrderState.SIGNATURE_GENERATED) {
      throw new IllegalStateException("Order must be signed before price calculation");
    }
  }

  @Override
  public void afterProcess() {
    // Post-processing steps
    log.info("Price calculation completed. Preparing for SAP integration");
    // Could trigger next step in workflow
    // Example: eventPublisher.publish(new PriceCalculatedEvent(currentOrder));
  }

  @Override
  public void onSuccess() {
    // Handle successful completion
    log.info("Price calculation successful for order {}", currentOrder.getOrderId());
    // Could update metrics or send notifications
    // metricsService.incrementSuccessfulPriceCalculations();
  }

  @Override
  public void onError(Exception e) {
    // Handle errors during processing
    log.error(
        "Price calculation failed for order {}: {}", currentOrder.getOrderId(), e.getMessage());
    currentOrder.setState(OrderState.ERROR);
    // Could implement retry logic or compensation
    // retryService.scheduleRetry(currentOrder);
  }

  @Override
  public void onFinally() {
    // Cleanup resources, always executed
    log.info("Cleaning up price calculation resources");
    // Example: close connections, clear caches
    currentOrder = null;
  }

  // Method to set the order before processing
  public void setOrder(Order order) {
    this.currentOrder = order;
  }
}
