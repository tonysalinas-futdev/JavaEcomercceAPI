package com.example.Ecomercce.users.controllers;

import com.example.Ecomercce.shared.DTOs.paginatedDtos.PaginatedResponseDTO;
import com.example.Ecomercce.users.dtos.CreateUser;
import com.example.Ecomercce.users.dtos.UpdateUser;
import com.example.Ecomercce.users.dtos.UserDetails;
import com.example.Ecomercce.users.dtos.UserList;
import com.example.Ecomercce.users.models.User;
import com.example.Ecomercce.users.services.UserAdminService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.net.URI;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/api/v1/admin")
@AllArgsConstructor
public class AdminControllers {
  private final UserAdminService service;

  @PostMapping()
  @PreAuthorize("hasRole('ADMIN') and hasAuthority('CREATE_USER')")
  public ResponseEntity<?> createUser(@RequestBody @Valid CreateUser dto) {
    User user = service.createUserByAdmin(dto);
    URI location = URI.create("/api/v1/users/" + user.getId());
    ThreadContext.put("use_case", "create_user");
    ThreadContext.put("entity", "user");
    return ResponseEntity.created(location).body(user);
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<UserDetails> getUserById(@PathVariable @Positive Long id) {
    ThreadContext.putAll(Map.of("use_case", "get_user_by_id", "entity", "user"));
    return ResponseEntity.status(200).body(service.getUserDetailsById(id));
  }

  @GetMapping()
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<PaginatedResponseDTO<UserList>> getAllUsers(
      @RequestParam @Positive Integer page, @RequestParam @Positive Integer size) {
    ThreadContext.putAll(Map.of("use_case", "get_all_users", "entity", "user"));
    return ResponseEntity.status(200).body(service.getAllUsers(page, size));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN') and hasAuthority('DELETE_USER')")
  public ResponseEntity<?> deleteUser(@Positive Long id) {
    service.deleteUser(id);
    ThreadContext.putAll(Map.of("use_case", "delete_user", "entity", "user"));
    return ResponseEntity.status(200).build();
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN') and hasAuthority('UPDATE_USER')")
  public ResponseEntity<UserDetails> updateUser(@RequestBody UpdateUser dto, @Positive Long id) {
    ThreadContext.putAll(Map.of("use_case", "update_user", "entity", "user"));
    return ResponseEntity.ok(service.updateUser(id, dto));
  }
}
