package com.example.ecommerce.users.controllers;

import com.example.ecommerce.order.models.Order;
import com.example.ecommerce.users.dtos.UpdatePassword;
import com.example.ecommerce.users.dtos.UpdateUserProfile;
import com.example.ecommerce.users.dtos.UserProfile;
import com.example.ecommerce.users.services.UserService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.ThreadContext;
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

  @GetMapping("/profile")
  public ResponseEntity<UserProfile> getProfile(Authentication authentication) {
    UserProfile user = service.getProfile(authentication.getName());

    ThreadContext.putAll(Map.of("use_case", "get_profile", "entity", "user"));
    return ResponseEntity.ok(user);
  }

  @GetMapping(value = "/orders")
  public ResponseEntity<List<Order>> getOrders(Authentication authentication) {
    List<Order> orders = service.getUserByEmailAndLoadOrders(authentication.getName());

    ThreadContext.putAll(Map.of("use_case", "get_orders", "entity", "user"));
    return ResponseEntity.ok(orders);
  }

  @PutMapping("/password")
  public ResponseEntity<?> updatePassword(
      @RequestBody @Valid UpdatePassword dto, Authentication authentication) {
    service.updatePassword(dto, authentication.getName());
    ThreadContext.putAll(Map.of("use_case", "update_password", "entity", "user"));
    return ResponseEntity.ok().build();
  }

  @PutMapping("/profile")
  public ResponseEntity<UserProfile> updateProfile(
      @RequestBody @Valid UpdateUserProfile dto, Authentication authentication) {
    UserProfile user = service.updateProfile(dto, authentication.getName());

    ThreadContext.putAll(Map.of("use_case", "update_profile", "entity", "user"));
    return ResponseEntity.ok(user);
  }
}
