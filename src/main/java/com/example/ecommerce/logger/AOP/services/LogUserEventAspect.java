package com.example.ecommerce.logger.AOP.services;

import com.example.ecommerce.logger.annotations.LogUserEvent;
import com.example.ecommerce.logger.builders.audit.StructuredUserEventLogWriter;
import com.example.ecommerce.logger.enums.MarkerTypes;
import com.example.ecommerce.logger.mappers.UserLogMapper;
import com.example.ecommerce.users.dtos.UserDetails;
import com.example.ecommerce.users.logs.events.UserEvents;
import com.example.ecommerce.users.models.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.message.ObjectMessage;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@AllArgsConstructor
public class LogUserEventAspect {
  private static Logger log;
  private final UserLogMapper mapper;

  @Before("@annotation(event)")
  public void initLogger(JoinPoint jp, LogUserEvent event) {
    log = LogManager.getLogger(event.type());
  }

  @AfterReturning(pointcut = "@annotation(event)", returning = "user")
  public void logEvent(JoinPoint jp, LogUserEvent event, Object user)
      throws JsonProcessingException {
    UserEvents userEvent = event.value();
    StructuredUserEventLogWriter structuredData = null;

    switch (user) {
      case User u -> {
        structuredData = mapper.entityToStructuredLog(u);
        structuredData.setEvent(userEvent);
      }
      case UserDetails u -> {
        structuredData = mapper.detailsDtoToStructuredLog(u);
        structuredData.setEvent(userEvent);
      }
      default -> structuredData = null;
    }

    if (structuredData != null) {
      log.info(
          MarkerManager.getMarker(MarkerTypes.AUDIT.toString()), new ObjectMessage(structuredData));
    }

    if (structuredData == null) {
      log.info("The user is null");
    }

    System.out.println("Aspect ejecutado");
  }
}
