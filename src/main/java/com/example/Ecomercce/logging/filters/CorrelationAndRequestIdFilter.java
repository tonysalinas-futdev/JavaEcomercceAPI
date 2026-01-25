package com.example.Ecomercce.logging.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class CorrelationAndRequestIdFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws IOException, ServletException {
    String correlationId = request.getHeader("X-Correlation-ID");
    String requestId = request.getHeader("X-Request-ID");

    if (correlationId == null) {
      correlationId = UUID.randomUUID().toString();
    }
    ThreadContext.put("X-Correlation-ID", correlationId);

    ThreadContext.put("X-Request-ID", requestId);

    try {
      filterChain.doFilter(request, response);
    } finally {
      ThreadContext.clearMap();
    }
  }
}
