package com.example.ecommerce.auth.controllers;

import com.example.ecommerce.auth.dtos.AuthResponseDTO;
import com.example.ecommerce.auth.dtos.LoginDTO;
import com.example.ecommerce.auth.dtos.SignUpDTO;
import com.example.ecommerce.auth.service.AuthService;
import java.util.Map;

import com.example.ecommerce.auth.utils.CookieBuilder;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthControllers {
  private final AuthService service;
  private final CookieBuilder cookieBuilder;

  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Endpoint for sign up" , description = "Endpoint for sign up")
  @PostMapping("/sign_up")
  public ResponseEntity<?> signUp(@RequestBody SignUpDTO dto) {
    AuthResponseDTO tokens = service.signUp(dto);
      ResponseCookie accessCookie=cookieBuilder.buildAccessTokenCookie(tokens);
      ResponseCookie refreshCookie=cookieBuilder.buildRefreshTokenCookie(tokens);

    ThreadContext.putAll(Map.of("use_case", "register_user", "entity", "user"));
    return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE,accessCookie.toString()).header(HttpHeaders.SET_COOKIE,refreshCookie.toString()).build();
  }

  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Endpoint for login" , description = "Endpoint for login")
  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginDTO dto) {
    AuthResponseDTO tokens = service.login(dto);
      ResponseCookie accessCookie=cookieBuilder.buildAccessTokenCookie(tokens);
      ResponseCookie refreshCookie=cookieBuilder.buildRefreshTokenCookie(tokens);
    ThreadContext.putAll(Map.of("use_case", "user_login", "entity", "user"));
    return ResponseEntity.ok()
              .header(HttpHeaders.SET_COOKIE,accessCookie.toString()).header(HttpHeaders.SET_COOKIE,refreshCookie.toString()).build();
  }
}
