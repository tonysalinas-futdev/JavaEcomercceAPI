package com.example.ecommerce.logger.annotations;

import com.example.ecommerce.order.logs.events.OrderDetailsLogEvents;
import com.example.ecommerce.order.logs.events.OrderLogsEvents;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LogOrderEvent {
  OrderLogsEvents orderEvent();

  OrderDetailsLogEvents detailsEvent() default OrderDetailsLogEvents.WITHOUT_EVENT;

  Class<?> class_();
}
