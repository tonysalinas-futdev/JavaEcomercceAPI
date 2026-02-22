package com.example.ecommerce.auth.filters;

import com.example.ecommerce.auth.exceptions.InvalidTokenException;
import com.example.ecommerce.auth.facade.JwtFacade;
import com.example.ecommerce.shared.exceptions.NotFoundException;
import com.example.ecommerce.users.models.User;
import com.example.ecommerce.users.services.UserQueryService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
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
  private final UserQueryService userQueryService;
  private final JwtFacade facade;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    if (request.getServletPath().contains("/auth")) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      facade.validateAccessToken(request.getHeader(HttpHeaders.AUTHORIZATION));

      Map<String, String> data =
          facade.extractBearerTokenAndPayload(request.getHeader(HttpHeaders.AUTHORIZATION));
      User user = userQueryService.findEntityByIdOrThrow(Long.valueOf(data.get("userId")));
      UserDetails userDetails = this.userDetailsService.loadUserByUsername(user.getEmail());

      var authReponse =
          new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
      authReponse.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

      SecurityContextHolder.getContext().setAuthentication(authReponse);

    } catch (NotFoundException | InvalidTokenException ex) {
    }

    filterChain.doFilter(request, response);
  }
}
