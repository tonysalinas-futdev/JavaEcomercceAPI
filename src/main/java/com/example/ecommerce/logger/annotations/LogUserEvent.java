package com.example.ecommerce.logger.annotations;

import com.example.ecommerce.users.logs.events.UserEvents;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LogUserEvent {

  UserEvents value();

  Class<?> type();
}
