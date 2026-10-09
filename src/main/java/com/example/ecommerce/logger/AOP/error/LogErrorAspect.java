package com.example.ecommerce.logger.AOP.error;

import com.example.ecommerce.shared.exceptions.BasicException;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LogErrorAspect {

  @AfterThrowing(
      pointcut =
          "execution(* com.example.ecommerce..*(..))"
              + " && !within(*..filter..*)"
              + " && !within(*..filters..*)"
              + " && !within(*..middleware..*)",
      throwing = "ex")
  public void logError(JoinPoint jp, Exception ex) {

    String className = jp.getSignature().getDeclaringTypeName();
    String methodName = jp.getSignature().getName();

      var logger = ex instanceof BasicException ? log.atWarn() : log.atError();
      logger
              .addKeyValue("class_name", className)
              .addKeyValue("method_name", methodName)
              .addKeyValue("exception_type", ex.getClass().getName())
              .setCause(ex)
              .setMessage(ex.getMessage())
              .log();
    }
  }

