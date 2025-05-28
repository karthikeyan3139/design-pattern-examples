package com.example.aop.service.impl;

import com.example.aop.model.AdobeSignRecoveryPoints;
import com.example.aop.model.Order;
import com.example.aop.model.OrderState;
import com.example.aop.model.RecoveryContext;
import com.example.aop.model.RecoveryPoint;
import com.example.aop.service.AbstractOrderProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AdobeSignServiceProcessor extends AbstractOrderProcessor {

  public AdobeSignServiceProcessor() {
    this.recoveryPoint = getServiceRecoveryPoint();
  }

  @Override
  public String getServiceRecoveryPoint() {
    return RecoveryPoint.INIT_GENERATE_SIGNATURE.name();
  }

  @Override
  public void beforeProcess() {
    setRecoveryPoint(AdobeSignRecoveryPoints.INIT_ADOBE_SIGN.name());
    log.info("Initializing Adobe Sign process");
    if (currentOrder == null) {
      throw new IllegalStateException("Order is required for signature generation");
    }
  }

  @Override
  public void processOrder() {
    setRecoveryPoint(AdobeSignRecoveryPoints.GENERATE_SIGNATURE.name());
    log.info("Generating signature with recovery point: {}", getRecoveryPoint());
    currentOrder.setState(OrderState.SIGNATURE_GENERATED);
  }

  @Override
  public void afterProcess() {
    setRecoveryPoint(AdobeSignRecoveryPoints.COMPLETE_SIGNATURE.name());
    log.info("Completing signature process");
  }

  @Override
  public void onSuccess() {
    log.info("Signature generation completed successfully");
  }

  @Override
  public void onError(Exception e) {
    log.error("Error in signature generation: {}", e.getMessage());
    currentOrder.setState(OrderState.ERROR);
  }

  @Override
  public void onFinally() {
    log.info("Cleaning up Adobe Sign resources");
  }

  @Override
  public void setOrder(Order order) {
    this.currentOrder = order;
  }

  @Override
  public RecoveryContext getRecoveryContext() {
    return this.recoveryContext;
  }

  @Override
  public void setRecoveryContext(RecoveryContext context) {
    this.recoveryContext = context;
  }

  @Override
  public String getRecoveryPoint() {
    return this.recoveryPoint;
  }

  @Override
  public void setRecoveryPoint(String recoveryPoint) {
    this.recoveryPoint = recoveryPoint;
  }
}
