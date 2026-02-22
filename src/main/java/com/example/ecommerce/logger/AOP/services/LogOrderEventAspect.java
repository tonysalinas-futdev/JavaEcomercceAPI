package com.example.ecommerce.logger.AOP.services;

import com.example.ecommerce.logger.annotations.LogOrderEvent;
import com.example.ecommerce.logger.builders.audit.StructuredOrderDetailsEventLogWriter;
import com.example.ecommerce.logger.builders.audit.StructuredOrderEventLogWriter;
import com.example.ecommerce.logger.enums.MarkerTypes;
import com.example.ecommerce.logger.mappers.OrderDetailssLogMapper;
import com.example.ecommerce.logger.mappers.OrderLogMapper;
import com.example.ecommerce.order.dtos.order.OrderDTO;
import com.example.ecommerce.order.logs.events.OrderDetailsLogEvents;
import com.example.ecommerce.order.logs.events.OrderLogsEvents;
import com.example.ecommerce.order.models.Order;
import java.util.List;
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
public class LogOrderEventAspect {
  private static Logger logger;
  private final OrderLogMapper mapper;
  private final OrderDetailssLogMapper orderDetailsMapper;

  @Before("@annotation(event)")
  public void initLogger(JoinPoint jp, LogOrderEvent event) {
    logger = LogManager.getLogger(event.class_());
  }

  @AfterReturning(pointcut = "@annotation(event)", returning = "order")
  public void logEvent(JoinPoint jp, LogOrderEvent event, Object order) {
    OrderLogsEvents orderEvent = event.orderEvent();
    OrderDetailsLogEvents orderDetailsEvent = event.detailsEvent();
    StructuredOrderEventLogWriter structuredOrderLog = null;
    List<StructuredOrderDetailsEventLogWriter> structuredOrderDetailsList = null;

    switch (order) {
      case Order o -> {
        structuredOrderLog = mapper.fromEntityToStructuredLog(o);
        structuredOrderDetailsList =
            o.getOrderDetails().stream()
                .map(od -> orderDetailsMapper.fromEntityToStructuredLog(od))
                .toList();
        structuredOrderLog.setEvent(orderEvent);
      }
      case OrderDTO dto -> {
        structuredOrderLog = mapper.fromDtoToStructuredLog(dto);
        structuredOrderDetailsList =
            dto.getOrderDetails().stream()
                .map(od -> orderDetailsMapper.fromDtoToStructuredLog(od))
                .toList();
        structuredOrderLog.setEvent(orderEvent);
      }

      default -> structuredOrderLog = null;
    }
    Marker marker = MarkerManager.getMarker(MarkerTypes.AUDIT.toString());

    if (structuredOrderLog != null) {
      logger.info(marker, new ObjectMessage(structuredOrderLog));

    } else {
      logger.info(marker, "Order null");
    }

    if (structuredOrderDetailsList != null
        && !orderDetailsEvent.equals(OrderDetailsLogEvents.WITHOUT_EVENT)) {
      for (StructuredOrderDetailsEventLogWriter od : structuredOrderDetailsList) {
        od.setEvent(orderDetailsEvent);
        logger.info(marker, new ObjectMessage(od));
      }
    }

    System.out.println("Order aspect ejecuted");
  }
}
