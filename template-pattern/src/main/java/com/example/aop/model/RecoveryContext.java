package com.example.aop.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecoveryContext {
  private String recoveryPoint;
  private OrderState lastSuccessfulState;
  private String errorMessage;
  private String serviceName;
}
