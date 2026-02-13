package com.example.Ecomercce.auth.service;

import com.example.Ecomercce.auth.dtos.AuthResponse;
import com.example.Ecomercce.auth.dtos.LoginDTO;
import com.example.Ecomercce.auth.dtos.SignUpDTO;
import com.example.Ecomercce.auth.facade.JwtFacade;
import com.example.Ecomercce.logging.service.LoggerService;
import com.example.Ecomercce.users.models.User;
import com.example.Ecomercce.users.services.UserService;
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
  private final LoggerService logger;
  public final JwtFacade facade;

  public AuthResponse login(LoginDTO dto) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
    User user = userService.getUserByEmail(dto.getEmail());

    logger.addTypeOfLog("bussiness");
    logger.createBusinnessEventLog("authenticated_user", "login", "user_id", user.getId());
    return facade.saveTokenAndBuildResponse(user);
  }

  public AuthResponse signUp(SignUpDTO dto) {
    User user = userService.registerValidUser(dto);
    logger.addTypeOfLog("bussiness");
    logger.createBusinnessEventLog("registered_user", "signUp", "user_id", user.getId());
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
