package com.example.Ecomercce.logging.interface_;

public interface LoggerInterface {
  void info(Object data);

  void error(Object payload);

  void warn(Object payload);

  void debug(Object payload);
}
