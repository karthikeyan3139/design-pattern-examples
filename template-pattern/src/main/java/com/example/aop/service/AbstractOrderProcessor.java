package com.example.aop.service;

import com.example.aop.model.Order;
import com.example.aop.model.OrderState;
import com.example.aop.model.RecoveryContext;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractOrderProcessor implements BaseOrderProcess {

  @Getter @Setter protected Order currentOrder;

  @Getter @Setter protected String recoveryPoint;

  @Getter @Setter protected RecoveryContext recoveryContext;

  public AbstractOrderProcessor() {
    this.recoveryPoint = getServiceRecoveryPoint();
    log.info("Initializing service with recovery point: {}", this.recoveryPoint);
  }

  // Abstract method for service-specific recovery point
  public abstract String getServiceRecoveryPoint();

  @Override
  public void beforeProcess() {
    log.info("Starting process with recovery point: {}", recoveryPoint);
    validateOrder();
  }

  @Override
  public void afterProcess() {
    log.info("Completing process: {}", recoveryPoint);
  }

  @Override
  public void onSuccess() {
    log.info("Process completed successfully: {}", recoveryPoint);
    recoveryContext = RecoveryContext.builder().recoveryPoint(recoveryPoint).build();
  }

  @Override
  public void onError(Exception e) {
    log.error("Error in process: {} - {}", recoveryPoint, e.getMessage());
    if (currentOrder != null) {
      currentOrder.setState(OrderState.ERROR);
    }
    recoveryContext =
        RecoveryContext.builder().recoveryPoint(recoveryPoint).errorMessage(e.getMessage()).build();
  }

  @Override
  public void onFinally() {
    log.info("Cleaning up resources for: {}", recoveryPoint);
  }

  protected void validateOrder() {
    if (currentOrder == null) {
      throw new IllegalStateException("Order is required for processing");
    }
  }
}
