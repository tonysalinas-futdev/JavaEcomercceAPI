package com.example.ecommerce.logger.annotations;

import com.example.ecommerce.auth.log.events.AuthLogEvents;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogAuthEvent {
  AuthLogEvents event();

  Class<?> loggerName();
}
