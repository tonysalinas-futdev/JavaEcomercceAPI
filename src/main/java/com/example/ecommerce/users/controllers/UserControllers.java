package com.example.ecommerce.users.controllers;

import com.example.ecommerce.users.dtos.UpdatePasswordDTO;
import com.example.ecommerce.users.dtos.UpdateUserProfileDTO;
import com.example.ecommerce.users.dtos.UserProfileDTO;
import com.example.ecommerce.users.services.UserQueryService;
import com.example.ecommerce.users.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@Validated
@RequestMapping("/api/v1/me")
public class UserControllers {
  private final UserService service;
  private final UserQueryService queryService;

  @GetMapping("/profile")
  public ResponseEntity<UserProfileDTO> getProfile(Authentication authentication) {
    UserProfileDTO user = queryService.findByEmailAndReturnProfileDto(authentication.getName());
    return ResponseEntity.ok(user);
  }

  @PutMapping("/password")
  public ResponseEntity<?> updatePassword(
          @RequestBody @Valid UpdatePasswordDTO dto, Authentication authentication) {
    service.updatePassword(dto, authentication.getName());
    return ResponseEntity.ok().build();
  }

  @PutMapping("/profile")
  public ResponseEntity<UserProfileDTO> updateProfile(
          @RequestBody @Valid UpdateUserProfileDTO dto, Authentication authentication) {
    UserProfileDTO user = service.updateProfile(dto, authentication.getName());
    return ResponseEntity.ok(user);
  }
}
