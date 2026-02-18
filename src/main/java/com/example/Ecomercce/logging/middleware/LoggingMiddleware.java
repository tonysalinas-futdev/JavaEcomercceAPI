package com.example.Ecomercce.logging.middleware;

import com.example.Ecomercce.logging.service.LoggerService;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class LoggingMiddleware implements Filter {
  private final LoggerService logger;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    String method = httpRequest.getMethod();
    String uri = httpRequest.getRequestURI();

    long initTime = System.currentTimeMillis();

    logger.addTypeOfLog("technical");
    logger.createStartRequestLog(uri, method);

    chain.doFilter(request, response);

    long totalTime = System.currentTimeMillis() - initTime;
    int status = httpResponse.getStatus();

    logger.createEndRequestLog(status, method, totalTime, uri);

    ThreadContext.clearMap();
  }
}
