package com.example.aop.model;

public enum OrderState {
    INITIATED,
    VALIDATED,
    EMAIL_SENT,
    SIGNATURE_GENERATED,
    PRICE_CALCULATED,
    SAP_PROCESSED,
    COMPLETED,
    ERROR
}
