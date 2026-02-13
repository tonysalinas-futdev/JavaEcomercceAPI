package com.example.Ecomercce.logging.service;

import com.example.Ecomercce.logging.interface_.LoggerInterface;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoggerService {
  private final LoggerInterface logger;

  public void createBusinnessEventLog(String event, String method, String object_type, Long id) {
    ThreadContext.put("type", "business");
    logger.info(Map.of("event", event, "method", method, object_type, id));
  }

  public void createStartRequestLog(String uri, String method) {
    logger.info(Map.of("url", uri, "method", method));
  }

  public void addTypeOfLog(String type) {
    ThreadContext.put("type", type);
  }

  public void createEndRequestLog(Integer status, String method, Long duration, String uri) {
    logger.info(Map.of("duration", duration, "status", status, "method", method, "uri", uri));
  }

  public void createErrorLog(
      String message, String exceptionType, Throwable stackTrace, String statusCode) {
    logger.error(
        Map.of(
            "status_code",
            statusCode,
            "message",
            message,
            "type_exception",
            exceptionType,
            "stack_trace",
            stackTrace));
  }

  public void createWarnLog(String message, String exceptionType, String statusCode) {
    logger.warn(
        Map.of("status_code", statusCode, "message", message, "type_exception", exceptionType));
  }

  public void addContextInControllers(String useCase, String entity) {
    ThreadContext.put("use_case", useCase);
    ThreadContext.put("entity", entity);
  }

  public void logInfo(String message) {
    logger.info(message);
  }

  public void logError(String message) {
    logger.error(message);
  }

  public void logDebug(Object message) {
    logger.debug(message);
  }
}
