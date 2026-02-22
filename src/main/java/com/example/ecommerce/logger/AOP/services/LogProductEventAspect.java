package com.example.ecommerce.logger.AOP.services;

import com.example.ecommerce.logger.annotations.LogProductEvent;
import com.example.ecommerce.logger.enums.MarkerTypes;
import com.example.ecommerce.logger.mappers.ProductLogMapper;
import com.example.ecommerce.products.DTOs.ProductDetailsDTO;
import java.util.Map;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class LogProductEventAspect {
  private static Logger logger;
  private final ProductLogMapper mapper;

  @Before("@annotation(event)")
  public void initLogger(JoinPoint jp, LogProductEvent event) {
    logger = LogManager.getLogger(event.loggerName());
  }

  @AfterReturning(pointcut = "@annotation(event)", returning = "productDetails")
  public void logEvent(JoinPoint jp, LogProductEvent event, Object productDetails) {
    Marker marker = MarkerManager.getMarker(MarkerTypes.AUDIT.toString());
    if (productDetails instanceof ProductDetailsDTO pd) {
      var logInfo = mapper.productDetailsDtoToStructuredLog(pd);
      logInfo.setEvent(event.event());
      logger.info(marker, new ObjectMessage(logInfo));
    } else {
      var info =
          Map.of("event", event.event(), "product_class", productDetails.getClass().getName());
      logger.info(marker, new ObjectMessage(info));
    }

    System.out.println("Product aspect ejecuted");
  }
}
