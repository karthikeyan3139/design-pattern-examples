package com.example.aop.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServiceResponse {
    private boolean success;
    private String message;
    private OrderState state;
    private String recoveryPoint;
}
