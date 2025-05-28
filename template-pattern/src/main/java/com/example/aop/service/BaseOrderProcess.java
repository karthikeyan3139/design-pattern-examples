package com.example.aop.service;

import com.example.aop.annotation.ProcessTracker;
import com.example.aop.model.Order;
import com.example.aop.model.RecoveryContext;

public interface BaseOrderProcess {

  @ProcessTracker(processName = "execute",useServiceRecoveryPoint = true)
  default void execute() {
    try {
      beforeProcess();
      processOrder();
      afterProcess();
      onSuccess();
    } catch (Exception e) {
      onError(e);
    } finally {
      onFinally();
    }
  }


  void beforeProcess();


  void processOrder();


  void afterProcess();


  void onSuccess();

  @ProcessTracker(processName = "onError")
  void onError(Exception e);


  void onFinally();

  void setOrder(Order order);

  RecoveryContext getRecoveryContext();

  void setRecoveryContext(RecoveryContext context);

  void setRecoveryPoint(String recoveryPoint);

  String getRecoveryPoint(); // Each service will define its own recovery points
}
