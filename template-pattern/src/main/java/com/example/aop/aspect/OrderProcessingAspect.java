package com.example.aop.aspect;

import com.example.aop.model.ServiceResponse;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class OrderProcessingAspect {

  // Store recovery points for failed operations
  private final ConcurrentHashMap<String, ServiceResponse> recoveryPoints =
      new ConcurrentHashMap<>();

  @Around("execution(* com.example.aop.service.*Service.*(..))")
  public Object monitorService(ProceedingJoinPoint joinPoint) throws Throwable {
    String methodName = joinPoint.getSignature().getName();
    String serviceName = joinPoint.getTarget().getClass().getSimpleName();
    Instant startTime = Instant.now();

    try {
      log.info("Starting {} in service {} at {}", methodName, serviceName, startTime);
      Object result = joinPoint.proceed();

      // If result is ServiceResponse, log success/failure
      if (result instanceof ServiceResponse response) {
        if (response.isSuccess()) {
          log.info(
              "Successfully completed {} in service {}. New state: {}",
              methodName,
              serviceName,
              response.getState());
        } else {
          // Store recovery point for failed operations
          String recoveryKey = generateRecoveryKey(joinPoint);
          recoveryPoints.put(recoveryKey, response);
          log.error(
              "Failed {} in service {}. Recovery point: {}", methodName, serviceName, recoveryKey);
        }
      }

      return result;
    } catch (Exception e) {
      String recoveryKey = generateRecoveryKey(joinPoint);
      ServiceResponse errorResponse =
          ServiceResponse.builder()
              .success(false)
              .message("Exception in " + serviceName + ": " + e.getMessage())
              .recoveryPoint(recoveryKey)
              .build();

      recoveryPoints.put(recoveryKey, errorResponse);
      log.error(
          "Exception in {} - {}: {}. Recovery point: {}",
          serviceName,
          methodName,
          e.getMessage(),
          recoveryKey);
      throw e;
    } finally {
      long duration = Instant.now().toEpochMilli() - startTime.toEpochMilli();
      log.info("Service {} method {} took {}ms", serviceName, methodName, duration);
    }
  }

  private String generateRecoveryKey(ProceedingJoinPoint joinPoint) {
    return String.format(
        "%s-%s-%d",
        joinPoint.getTarget().getClass().getSimpleName(),
        joinPoint.getSignature().getName(),
        System.currentTimeMillis());
  }

  // Method to retrieve recovery point
  public ServiceResponse getRecoveryPoint(String key) {
    return recoveryPoints.get(key);
  }

  // Method to resume from recovery point
  public void clearRecoveryPoint(String key) {
    recoveryPoints.remove(key);
  }

  // @Around("@annotation(processTracker)")
  // public Object trackProcess(ProceedingJoinPoint joinPoint, ProcessTracker processTracker)
  //     throws Throwable {
  //   String recoveryPoint = processTracker.recoveryPoint();

  //   try {
  //     log.info(
  //         "Starting process: {} at recovery point: {}",
  //         processTracker.processName(),
  //         recoveryPoint);
  //     Object result = joinPoint.proceed();

  //     if (processTracker.isRecoveryPoint()) {
  //       RecoveryContext context =
  //           RecoveryContext.builder()
  //               .recoveryPoint(recoveryPoint)
  //               .serviceName(joinPoint.getTarget().getClass().getSimpleName())
  //               .build();
  //       ((BaseOrderProcess) joinPoint.getTarget()).setRecoveryContext(context);
  //     }

  //     return result;
  //   } catch (Exception e) {
  //     RecoveryContext context =
  //         RecoveryContext.builder()
  //             .recoveryPoint(recoveryPoint)
  //             .errorMessage(e.getMessage())
  //             .serviceName(joinPoint.getTarget().getClass().getSimpleName())
  //             .build();
  //     ((BaseOrderProcess) joinPoint.getTarget()).setRecoveryContext(context);
  //     throw e;
  //   }
  // }
}
