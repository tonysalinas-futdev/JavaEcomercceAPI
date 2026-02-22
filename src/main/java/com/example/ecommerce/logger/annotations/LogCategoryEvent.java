package com.example.ecommerce.logger.annotations;

import com.example.ecommerce.categories.logs.events.CategoryLogsEvents;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogCategoryEvent {
  CategoryLogsEvents event();

  Class<?> loggerName();
}
