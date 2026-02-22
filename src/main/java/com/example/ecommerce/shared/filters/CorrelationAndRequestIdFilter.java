package com.example.ecommerce.shared.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
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
      response.addHeader("X-Correlation-ID", correlationId);
      ThreadContext.put("X-Correlation-ID", correlationId);
      MDC.put("X-Correlation-ID", correlationId);
    }

    if (requestId == null) {
      requestId = UUID.randomUUID().toString();
      response.addHeader("X-Request-ID", requestId);
    }
    try {
      filterChain.doFilter(request, response);
    } finally {
      ThreadContext.clearAll();
      MDC.clear();
    }
  }
}
