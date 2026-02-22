package com.example.ecommerce.logger.filters;

import com.example.ecommerce.logger.builders.technical.StructuredTechnicalMiddlewareLog;
import com.example.ecommerce.logger.enums.MarkerTypes;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.message.ObjectMessage;
import org.springframework.stereotype.Component;

@Component
public class LoggingMiddleware implements Filter {
  private static final Logger logger = LogManager.getLogger(LoggingMiddleware.class);

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    Instant start = Instant.now();
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    String method = httpRequest.getMethod();
    String uri = httpRequest.getRequestURI();

    chain.doFilter(request, response);

    Integer status = httpResponse.getStatus();

    var logInfo =
        StructuredTechnicalMiddlewareLog.builder()
            .method(method)
            .path(uri)
            .statusCode(status.toString())
            .build();
    Marker marker = MarkerManager.getMarker(MarkerTypes.TECHNICAL.toString());

    Instant end = Instant.now();

    Duration totalTime = Duration.between(start, end);
    logInfo.setDuration(totalTime.toString());

    logger.info(marker, new ObjectMessage(logInfo));
  }
}
