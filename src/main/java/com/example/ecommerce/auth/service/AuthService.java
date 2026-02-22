package com.example.ecommerce.auth.service;

import com.example.ecommerce.auth.dtos.AuthResponse;
import com.example.ecommerce.auth.dtos.LoginDTO;
import com.example.ecommerce.auth.dtos.SignUpDTO;
import com.example.ecommerce.auth.facade.JwtFacade;
import com.example.ecommerce.auth.log.events.AuthLogEvents;
import com.example.ecommerce.logger.annotations.LogAuthEvent;
import com.example.ecommerce.users.models.User;
import com.example.ecommerce.users.services.UserQueryService;
import com.example.ecommerce.users.services.UserService;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@AllArgsConstructor
public class AuthService {
  private final UserQueryService userQUserService;
  private final UserService userService;
  private final AuthenticationManager authenticationManager;
  public final JwtFacade facade;

  @LogAuthEvent(event = AuthLogEvents.USER_LOGIN, loggerName = AuthService.class)
  public AuthResponse login(@Valid LoginDTO dto) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
    User user = userQUserService.findByEmailOrThrow(dto.getEmail());
    return facade.saveTokenAndBuildResponse(user);
  }

  @LogAuthEvent(event = AuthLogEvents.USER_REGISTER, loggerName = AuthService.class)
  public AuthResponse signUp(@Valid SignUpDTO dto) {
    User user = userService.registerValidUser(dto);
    return facade.saveTokenAndBuildResponse(user);
  }

  public AuthResponse refreshToken(String authHeader) {
    Map<String, String> data = facade.extractBearerTokenAndPayload(authHeader);

    String userEmail = data.get("userEmail");

    String refreshToken = data.get("refreshToken");

    User user = userQUserService.findByEmailOrThrow(userEmail);
    facade.validateToken(refreshToken, user);
    return facade.saveTokenAndBuildResponse(user);
  }
}
