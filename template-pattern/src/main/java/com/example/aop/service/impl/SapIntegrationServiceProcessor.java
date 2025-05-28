package com.example.aop.service.impl;

import com.example.aop.model.Order;
import com.example.aop.model.OrderState;
import com.example.aop.model.RecoveryPoint;
import com.example.aop.service.AbstractOrderProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SapIntegrationServiceProcessor extends AbstractOrderProcessor {

  @Override
  public String getServiceRecoveryPoint() {
    return RecoveryPoint.INIT_PROCESS_SAP.name();
  }

  @Override
  public void beforeProcess() {
    log.info("Validating SAP integration prerequisites");
    if (currentOrder == null) {
      throw new IllegalStateException("Order is required for SAP integration");
    }
    if (currentOrder.getState() != OrderState.PRICE_CALCULATED) {
      throw new IllegalStateException("Price must be calculated before SAP integration");
    }
  }

  @Override
  public void processOrder() {
    log.info("Sending order {} to SAP", currentOrder.getOrderId());
    // SAP integration logic here
    currentOrder.setState(OrderState.SAP_PROCESSED);
  }

  @Override
  public void afterProcess() {
    log.info("SAP integration completed, order process finished");
    currentOrder.setState(OrderState.COMPLETED);
  }

  @Override
  public void onSuccess() {
    log.info("SAP integration successful for order {}", currentOrder.getOrderId());
  }

  @Override
  public void onError(Exception e) {
    log.error("SAP integration failed for order {}: {}", currentOrder.getOrderId(), e.getMessage());
    currentOrder.setState(OrderState.ERROR);
  }

  @Override
  public void onFinally() {
    log.info("Cleaning up SAP integration resources");
    currentOrder = null;
  }

  public void setOrder(Order order) {
    this.currentOrder = order;
  }
}
