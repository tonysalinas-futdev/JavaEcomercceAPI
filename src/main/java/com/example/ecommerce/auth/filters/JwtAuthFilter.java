package com.example.ecommerce.auth.filters;

import com.example.ecommerce.auth.exceptions.InvalidTokenException;
import com.example.ecommerce.auth.service.TokenValidationService;
import com.example.ecommerce.auth.utils.JwtTokenParser;
import com.example.ecommerce.auth.utils.TokenExtractor;
import com.example.ecommerce.users.models.User;
import com.example.ecommerce.users.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {
  private final UserDetailsService userDetailsService;
  private final JwtTokenParser parser;
  private final UserRepository userRepository;

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
      Claims payload;
      try {
          payload = parser.parse(tokenValue);
      } catch (ExpiredJwtException ex) {
          payload = ex.getClaims();

          if ("access_token".equals(payload.get("token_type"))) {
              userRepository.findUserByEmail(payload.getSubject()).ifPresent(user -> {
                  user.setCredentialsExpired(true);
                  userRepository.save(user);
                  log.info("Assigned credentials expired to user with id:{}", user.getId());
              });
          }
          throw new InvalidTokenException("Token Expired");
      }

    UserDetails userDetails = this.userDetailsService.loadUserByUsername(payload.getSubject());

    UsernamePasswordAuthenticationToken authReponse =
        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    authReponse.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

    SecurityContextHolder.getContext().setAuthentication(authReponse);

    filterChain.doFilter(request, response);
  }
}
