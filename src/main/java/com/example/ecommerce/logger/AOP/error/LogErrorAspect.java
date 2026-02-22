package com.example.ecommerce.logger.AOP.error;

import com.example.ecommerce.logger.builders.error.StructuredErrorLog;
import com.example.ecommerce.logger.enums.MarkerTypes;
import com.example.ecommerce.shared.exceptions.BasicException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.message.ObjectMessage;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogErrorAspect {
  private static org.apache.logging.log4j.Logger log;

  @Before(
      "execution(* com.example.ecommerce..*(..))"
          + " && !within(*..filter..*)"
          + " && !within(*..filters..*)"
          + " && !within(*..middleware..*)")
  public void initLogger(JoinPoint jp) {
    log = LogManager.getLogger(jp.getTarget().getClass());
  }

  @AfterThrowing(
      pointcut =
          "execution(* com.example.ecommerce..*(..))"
              + " && !within(*..filter..*)"
              + " && !within(*..filters..*)"
              + " && !within(*..middleware..*)",
      throwing = "ex")
  public void logError(JoinPoint jp, Exception ex) {

    String stackTrace =
        Arrays.stream(ex.getStackTrace())
            .limit(5)
            .map(StackTraceElement::toString)
            .collect(Collectors.joining("\n"));
    Marker marker = MarkerManager.getMarker(MarkerTypes.ERROR.toString());
    StructuredErrorLog structuredInfo =
        StructuredErrorLog.builder()
            .dateTime(LocalDateTime.now().toString())
            .message(ex.getMessage())
            .stackTrace(stackTrace)
            .exceptionType(ex.getClass().getName())
            .build();

    if (ex instanceof BasicException) {
      log.warn(marker, structuredInfo);

    } else {
      log.error(marker, new ObjectMessage(structuredInfo));
    }

    System.out.println("Aspect ejecutado correctamente");
  }
}
