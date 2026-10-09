package com.example.ecommerce.logger.filters;

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

import lombok.extern.slf4j.Slf4j;

import org.apache.logging.log4j.ThreadContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LoggingMiddleware implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    Instant start = Instant.now();

    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    String user = "anonymous";
    if (SecurityContextHolder.getContext().getAuthentication()!= null){
        user = SecurityContextHolder.getContext().getAuthentication().getName();}

    String method = httpRequest.getMethod();
    String uri = httpRequest.getRequestURI();
      ThreadContext.put("request_user_email",user);
    try {
      chain.doFilter(request, response);

    } finally {

      Integer status = httpResponse.getStatus();

      Instant end = Instant.now();

      Duration totalTime = Duration.between(start, end);

        log.atInfo()
                .addKeyValue("method", method)
                .addKeyValue("uri", uri)
                .addKeyValue("ip", request.getRemoteAddr())
                .addKeyValue("duration", totalTime.toMillis())
                .addKeyValue("status", status)
                .log( "HTTP request successfully completed");

    }
  }
}
