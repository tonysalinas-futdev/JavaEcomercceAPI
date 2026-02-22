package com.example.ecommerce.logger.annotations;

public @interface LogDeleteEntityEvent {
  String event();

  Class<?> loggerName();
}
