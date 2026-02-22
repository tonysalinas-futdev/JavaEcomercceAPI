package com.example.ecommerce.logger.AOP.services;

import com.example.ecommerce.auth.dtos.LoginDTO;
import com.example.ecommerce.auth.dtos.SignUpDTO;
import com.example.ecommerce.logger.annotations.LogAuthEvent;
import com.example.ecommerce.logger.builders.audit.StructuredAuthEventLog;
import com.example.ecommerce.logger.enums.MarkerTypes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.message.ObjectMessage;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAuthEventAspect {
  private static Logger logger;

  @Before("@annotation(event)")
  public void initLogger(JoinPoint jp, LogAuthEvent event) {
    logger = LogManager.getLogger(event.loggerName());
  }

  @AfterReturning(pointcut = "@annotation(event)", returning = "user")
  public void logEvent(JoinPoint jp, LogAuthEvent event, Object user) {
    Object[] args = jp.getArgs();
    Object userData = new Object();
    StructuredAuthEventLog log = new StructuredAuthEventLog();
    log.setEvent(event.event());
    userData = args[0];
    Marker marker = MarkerManager.getMarker(MarkerTypes.AUDIT.toString());
    if (userData == null) {
      logger.info("No args");
      return;
    }
    switch (userData) {
      case LoginDTO u -> {
        log.setUserEmail(u.getEmail());
      }
      case SignUpDTO u -> {
        log.setUserEmail(u.getEmail());
      }

      default -> log.setUserEmail("None");
    }

    logger.info(marker, new ObjectMessage(log));
  }
}
