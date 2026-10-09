package com.example.ecommerce.auth.service;

import com.example.ecommerce.auth.dtos.AuthResponseDTO;
import com.example.ecommerce.auth.dtos.LoginDTO;
import com.example.ecommerce.auth.dtos.SignUpDTO;
import com.example.ecommerce.auth.model.Token;
import com.example.ecommerce.auth.utils.JwtTokenParser;
import com.example.ecommerce.auth.utils.JwtTokenProvider;
import com.example.ecommerce.users.models.User;
import com.example.ecommerce.users.services.UserQueryService;
import com.example.ecommerce.users.services.UserService;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Transactional
@Service
@Validated
@RequiredArgsConstructor
@Slf4j
public class AuthService {
  private final UserQueryService userQueryService;
  private final UserService userService;
  private final AuthenticationManager authenticationManager;
  private final TokenService tokenService;
  private final JwtTokenProvider jwtTokenProvider;
  private final JwtTokenParser parser;
  public final TokenValidationService validationService;

  @Transactional
  public AuthResponseDTO login(@Valid LoginDTO dto) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(dto.email(), dto.password()));
    User user = userQueryService.findByEmailOrThrow(dto.email());
    String accessToken = jwtTokenProvider.createAccessToken(user);
    String refreshToken = jwtTokenProvider.createRefreshToken(user);
    tokenService.saveUserTokenAndDeletePrevious(user, refreshToken);

    log.atInfo()
              .addKeyValue("event_type","user_authenticated")
              .addKeyValue("user_id",user.getId())
              .addKeyValue("user_email", user.getEmail())
              .log("User authenticated successfully");

    return new AuthResponseDTO(accessToken, refreshToken);
  }

  @Transactional
  public AuthResponseDTO signUp(@Valid SignUpDTO dto) {
    User user = userService.registerUser(dto);
    String accessToken = jwtTokenProvider.createAccessToken(user);
    String refreshToken = jwtTokenProvider.createRefreshToken(user);
    tokenService.saveUserTokenAndDeletePrevious(user, refreshToken);
      log.atInfo()
              .addKeyValue("event_type","user_signed_up")
              .addKeyValue("user_id",user.getId())
              .addKeyValue("user_email", user.getEmail())
              .log("User registered and authenticated successfully");

    return new AuthResponseDTO(accessToken, refreshToken);
  }

  public AuthResponseDTO refreshToken(String tokenValue) {
    Token token = tokenService.getByValue(tokenValue);

    validationService.validateRefreshToken(token);
    Claims payload = parser.parse(token.getValue());
    User user = userQueryService.findByEmailOrThrow(payload.getSubject());

    String accessToken = jwtTokenProvider.createAccessToken(user);
    String refreshToken = jwtTokenProvider.createRefreshToken(user);
    tokenService.saveUserTokenAndDeletePrevious(user, refreshToken);

    log.atInfo()
            .addKeyValue("event_type","token_refreshed")
            .addKeyValue("user_id",user.getId())
            .addKeyValue("user_email", user.getEmail())
            .log("Authentication tokens refreshed successfully");


    return new AuthResponseDTO(accessToken, refreshToken);
  }
}
