package com.example.aop.model;

public enum RecoveryPoint {
  INIT_VALIDATE_ORDER("INIT_VALIDATE_ORDER"),
  INIT_SEND_EMAIL("INIT_SEND_EMAIL"),
  INIT_GENERATE_SIGNATURE("INIT_GENERATE_SIGNATURE"),
  INIT_CALCULATE_PRICE("INIT_CALCULATE_PRICE"),
  INIT_PROCESS_SAP("INIT_PROCESS_SAP"),
  INIT_COMPLETE_ORDER("INIT_COMPLETE_ORDER"),
  INIT_ERROR_HANDLING("INIT_ERROR_HANDLING");

  private final String point;

  public String getPoint() {
    return point;
  }

  RecoveryPoint(String point) {
    this.point = point;
  }
}
