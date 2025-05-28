package com.example.aop.model;

public enum AdobeSignRecoveryPoints {
  INIT_ADOBE_SIGN("Initialize Adobe Sign Process"),
  GENERATE_SIGNATURE("Generate Digital Signature"),
  STORE_SIGNATURE("Store Generated Signature"),
  COMPLETE_SIGNATURE("Complete Signature Process");

  private final String description;

  AdobeSignRecoveryPoints(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}
