package com.example.ecommerce.auth.service;

import com.example.ecommerce.auth.dtos.AuthResponse;
import com.example.ecommerce.auth.dtos.LoginDTO;
import com.example.ecommerce.auth.dtos.SignUpDTO;
import com.example.ecommerce.auth.facade.JwtFacade;
import com.example.ecommerce.auth.log.events.AuthLogEvents;
import com.example.ecommerce.logger.annotations.LogAuthEvent;
import com.example.ecommerce.users.models.User;
import com.example.ecommerce.users.services.UserService;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
  private final UserService userService;
  private final AuthenticationManager authenticationManager;
  public final JwtFacade facade;

  @LogAuthEvent(event = AuthLogEvents.USER_LOGIN, loggerName = AuthService.class)
  public AuthResponse login(LoginDTO dto) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
    User user = userService.getUserByEmail(dto.getEmail());
    return facade.saveTokenAndBuildResponse(user);
  }

  @LogAuthEvent(event = AuthLogEvents.USER_REGISTER, loggerName = AuthService.class)
  public AuthResponse signUp(SignUpDTO dto) {
    User user = userService.registerValidUser(dto);
    return facade.saveTokenAndBuildResponse(user);
  }

  public AuthResponse refreshToken(String authHeader) {
    Map<String, String> data = facade.extractBearerTokenAndPayload(authHeader);

    String userEmail = data.get("userEmail");

    String refreshToken = data.get("refreshToken");

    User user = userService.getUserByEmail(userEmail);
    facade.validateToken(refreshToken, user);
    return facade.saveTokenAndBuildResponse(user);
  }
}
