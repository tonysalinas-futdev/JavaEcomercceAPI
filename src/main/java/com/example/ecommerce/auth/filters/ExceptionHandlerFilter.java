package com.example.ecommerce.auth.filters;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
public class ExceptionHandlerFilter extends  OncePerRequestFilter  {
  private final ObjectProvider<HandlerExceptionResolver> resolverProvider;

  public ExceptionHandlerFilter(
      @Qualifier("handlerExceptionResolver")
          ObjectProvider<HandlerExceptionResolver> resolverProvider) {
    this.resolverProvider = resolverProvider;
  }


  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {
    try {
      chain.doFilter(request, response);
    } catch (Exception ex) {
      resolverProvider.getObject().resolveException(request, response, null, ex);
    }
  }
}
