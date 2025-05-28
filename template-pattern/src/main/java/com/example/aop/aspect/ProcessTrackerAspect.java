package com.example.aop.aspect;

import com.example.aop.annotation.ProcessTracker;
import com.example.aop.service.AbstractOrderProcessor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ProcessTrackerAspect {

  @Around("@annotation(processTracker)")
  public Object trackProcess(ProceedingJoinPoint joinPoint, ProcessTracker processTracker)
      throws Throwable {
    AbstractOrderProcessor process = (AbstractOrderProcessor) joinPoint.getTarget();
    String recoveryPoint =
        processTracker.useServiceRecoveryPoint()
            ? process.getServiceRecoveryPoint() // Use service-specific recovery point
            : processTracker.processName();

    log.info("Executing {} with recovery point: {}", processTracker.processName(), recoveryPoint);
    process.setRecoveryPoint(recoveryPoint); // Update current recovery point
    return joinPoint.proceed();
  }
}
