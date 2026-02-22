package com.example.ecommerce.logger.AOP.services;

import com.example.ecommerce.logger.annotations.LogDeleteEntityEvent;
import com.example.ecommerce.logger.builders.audit.StructuredDeleteEntityEventLog;
import com.example.ecommerce.logger.enums.MarkerTypes;
import org.apache.logging.log4j.*;
import org.apache.logging.log4j.message.ObjectMessage;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogDeleteEntityEventAspect {

  private static Logger logger;

  @Before("@annotation(event)")
  public void initLogger(JoinPoint jp, LogDeleteEntityEvent event) {
    logger = LogManager.getLogger(event.loggerName());
  }

  @AfterReturning(pointcut = "@annotation(event)", returning = "returning")
  public void logEvent(JoinPoint jp, LogDeleteEntityEvent event, Object returning) {
    Marker marker = MarkerManager.getMarker(MarkerTypes.AUDIT.toString());
    Object[] args = jp.getArgs();
    Long entityId = null;
    for (Object arg : args) {
      if (arg instanceof Long a) {
        entityId = a;
        break;
      }
    }
    if (entityId != null) {
      StructuredDeleteEntityEventLog info =
          new StructuredDeleteEntityEventLog(entityId, event.event().toString());
      logger.info(marker, new ObjectMessage(info));
    } else {

      logger.info(marker, event.event().toString());
    }
  }
}
