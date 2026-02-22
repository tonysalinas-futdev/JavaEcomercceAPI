package com.example.ecommerce.auth.controllers;

import com.example.ecommerce.auth.dtos.AuthResponse;
import com.example.ecommerce.auth.dtos.LoginDTO;
import com.example.ecommerce.auth.dtos.SignUpDTO;
import com.example.ecommerce.auth.service.AuthService;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthControllers {
  private final AuthService service;

  @PostMapping("/sign_up")
  public ResponseEntity<?> signUp(@RequestBody SignUpDTO dto) {
    AuthResponse tokens = service.signUp(dto);
    ThreadContext.putAll(Map.of("use_case", "register_user", "entity", "user"));
    return ResponseEntity.ok(tokens);
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginDTO dto) {
    AuthResponse tokens = service.login(dto);
    ThreadContext.putAll(Map.of("use_case", "user_login", "entity", "user"));
    return ResponseEntity.ok(tokens);
  }
}
