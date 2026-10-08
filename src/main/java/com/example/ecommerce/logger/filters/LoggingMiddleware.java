package com.example.ecommerce.logger.filters;

import com.example.ecommerce.logger.builders.technical.StructuredTechnicalMiddlewareLog;
import com.example.ecommerce.logger.enums.MarkerTypes;
import com.example.ecommerce.users.models.User;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.message.ObjectMessage;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LoggingMiddleware implements Filter {

  private static final Logger logger = LogManager.getLogger(LoggingMiddleware.class);

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

    try {
      chain.doFilter(request, response);

    } finally {

      Integer status = httpResponse.getStatus();

      Instant end = Instant.now();

      Duration totalTime = Duration.between(start, end);

        log.info(
                "HTTP request successfully completed: method={}, path={}, user_email={} , ip={}, duration={}, status={}",
                method,
                uri,
                user,
                request.getRemoteAddr(),
                totalTime.toMillis(),
                status

        );
    }
  }
}
