package com.example.ecommerce.logger.AOP.services;

import com.example.ecommerce.categories.categoryDTOs.CategoryDetailsDTO;
import com.example.ecommerce.logger.annotations.LogCategoryEvent;
import com.example.ecommerce.logger.builders.audit.StructuredCategoryEventLog;
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
public class LogCategoryEventsAspect {

  private static Logger logger;

  @Before("@annotation(event)")
  public void initLogger(JoinPoint jp, LogCategoryEvent event) {
    logger = LogManager.getLogger(event.loggerName());
  }

  @AfterReturning(pointcut = "@annotation(event)", returning = "categoryDetails")
  public void logEvent(JoinPoint jp, LogCategoryEvent event, Object categoryDetails) {
    Marker marker = MarkerManager.getMarker(MarkerTypes.AUDIT.toString());
    StructuredCategoryEventLog info = new StructuredCategoryEventLog();

    info.setEvent(event);
    if (categoryDetails instanceof CategoryDetailsDTO c) {
      info.setCategoryId(c.getId());
      info.setName(c.getName());
      logger.info(marker, new ObjectMessage(info));
    }
    System.out.println("Category Aspect ejecuted");
  }
}
