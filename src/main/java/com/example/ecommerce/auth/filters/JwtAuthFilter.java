package com.example.ecommerce.auth.filters;

import com.example.ecommerce.auth.exceptions.InvalidTokenException;
import com.example.ecommerce.auth.service.TokenValidationService;
import com.example.ecommerce.auth.utils.JwtTokenParser;
import com.example.ecommerce.auth.utils.TokenExtractor;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
  private final UserDetailsService userDetailsService;
  private final JwtTokenParser parser;
  private final TokenValidationService validationService;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    if (request.getServletPath().contains("/auth")
        || request.getServletPath().contains("/actuator")) {
      filterChain.doFilter(request, response);
      return;
    }

    String tokenValue = TokenExtractor.extract(request.getHeader(HttpHeaders.AUTHORIZATION));
    Claims payload = parser.parse(tokenValue);
    if (payload.get("token_type").equals("access_token")) {
      if (validationService.isTokenExpired(tokenValue)) {
        throw new InvalidTokenException("Token Expired");
      }
    }

    UserDetails userDetails = this.userDetailsService.loadUserByUsername(payload.getSubject());

    var authReponse =
        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    authReponse.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

    SecurityContextHolder.getContext().setAuthentication(authReponse);

    filterChain.doFilter(request, response);
  }
}
