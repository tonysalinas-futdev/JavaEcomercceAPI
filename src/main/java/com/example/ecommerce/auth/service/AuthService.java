package com.example.ecommerce.auth.service;

import com.example.ecommerce.auth.dtos.AuthResponseDTO;
import com.example.ecommerce.auth.dtos.LoginDTO;
import com.example.ecommerce.auth.dtos.SignUpDTO;
import com.example.ecommerce.auth.log.events.AuthLogEvents;
import com.example.ecommerce.auth.model.Token;
import com.example.ecommerce.auth.utils.JwtTokenParser;
import com.example.ecommerce.auth.utils.JwtTokenProvider;
import com.example.ecommerce.logger.annotations.LogAuthEvent;
import com.example.ecommerce.users.models.User;
import com.example.ecommerce.users.services.UserQueryService;
import com.example.ecommerce.users.services.UserService;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@AllArgsConstructor
public class AuthService {
  private final UserQueryService userQueryService;
  private final UserService userService;
  private final AuthenticationManager authenticationManager;
  private final TokenService tokenService;
  private final JwtTokenProvider jwtTokenProvider;
  private final JwtTokenParser parser;
  public final TokenValidationService validationService;

  @LogAuthEvent(event = AuthLogEvents.USER_LOGIN, loggerName = AuthService.class)
  public AuthResponseDTO login(@Valid LoginDTO dto) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(dto.email(), dto.password()));
    User user = userQueryService.findByEmailOrThrow(dto.email());
    String accessToken = jwtTokenProvider.createAccessToken(user);
    String refreshToken = jwtTokenProvider.createRefreshToken(user);
    tokenService.saveUserTokenAndDeletePrevious(user, refreshToken);
    return new AuthResponseDTO(accessToken, refreshToken);
  }

  @LogAuthEvent(event = AuthLogEvents.USER_REGISTER, loggerName = AuthService.class)
  public AuthResponseDTO signUp(@Valid SignUpDTO dto) {
    User user = userService.registerUser(dto);
    String accessToken = jwtTokenProvider.createAccessToken(user);
    String refreshToken = jwtTokenProvider.createRefreshToken(user);
    tokenService.saveUserTokenAndDeletePrevious(user, refreshToken);
    return new AuthResponseDTO(accessToken, refreshToken);
  }

  public AuthResponseDTO refreshToken(String tokenValue) {
    Token token = tokenService.getByValue(tokenValue);

    validationService.validateRefreshToken(token);
    Claims payload = parser.parse(token.getValue());
    User user = userQueryService.findByEmailOrThrow(payload.getSubject());
    tokenService.revokeUserToken(user);

    String accessToken = jwtTokenProvider.createAccessToken(user);
    String refreshToken = jwtTokenProvider.createRefreshToken(user);
    tokenService.saveUserTokenAndDeletePrevious(user, refreshToken);

    return new AuthResponseDTO(accessToken, refreshToken);
  }
}
