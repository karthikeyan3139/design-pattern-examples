package com.example.aop.service.impl;

import com.example.aop.model.Order;
import com.example.aop.model.OrderState;
import com.example.aop.model.RecoveryPoint;
import com.example.aop.service.AbstractOrderProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailServiceProcessor extends AbstractOrderProcessor {

  EmailServiceProcessor() {
    this.recoveryPoint = getServiceRecoveryPoint();
  }

  @Override
  public String getServiceRecoveryPoint() {
    return RecoveryPoint.INIT_SEND_EMAIL.name();
  }

  @Override
  public void beforeProcess() {
    log.info("Validating email prerequisites");
    if (currentOrder == null) {
      throw new IllegalStateException("Order is required for email sending");
    }
    if (currentOrder.getState() != OrderState.VALIDATED) {
      throw new IllegalStateException("Order must be validated before sending email");
    }
  }

  @Override
  public void processOrder() {
    log.info("Sending email for order {}", currentOrder.getOrderId());
    // Email sending logic here
    currentOrder.setState(OrderState.EMAIL_SENT);
  }

  @Override
  public void afterProcess() {
    log.info("Email sent successfully, preparing for signature generation");
  }

  @Override
  public void onSuccess() {
    log.info("Email process completed for order {}", currentOrder.getOrderId());
  }

  @Override
  public void onError(Exception e) {
    log.error("Email sending failed for order {}: {}", currentOrder.getOrderId(), e.getMessage());
    currentOrder.setState(OrderState.ERROR);
  }

  @Override
  public void onFinally() {
    log.info("Cleaning up email service resources");
    currentOrder = null;
  }

  public void setOrder(Order order) {
    this.currentOrder = order;
  }
}
