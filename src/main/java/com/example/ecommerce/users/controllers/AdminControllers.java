package com.example.ecommerce.users.controllers;

import com.example.ecommerce.shared.dtos.paginatedresponse.PaginatedResponseDTO;
import com.example.ecommerce.users.dtos.CreateUser;
import com.example.ecommerce.users.dtos.UpdateUser;
import com.example.ecommerce.users.dtos.UserDetails;
import com.example.ecommerce.users.dtos.UserList;
import com.example.ecommerce.users.models.User;
import com.example.ecommerce.users.services.UserAdminService;
import com.example.ecommerce.users.services.UserQueryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.net.URI;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/api/v1/admin")
@AllArgsConstructor
public class AdminControllers {
  private final UserAdminService service;
  private final UserQueryService queryService;

  @PostMapping()
  @PreAuthorize("hasRole('ADMIN') and hasAuthority('CREATE_USER')")
  public ResponseEntity<?> createUser(@RequestBody @Valid CreateUser dto) {
    User user = service.createUserByAdmin(dto);
    URI location = URI.create("/api/v1/users/" + user.getId());
    return ResponseEntity.created(location).body(user);
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<UserDetails> getUserById(@PathVariable @Positive Long id) {
    return ResponseEntity.status(200).body(queryService.findByIdAndReturnDetailsDto(id));
  }

  @GetMapping()
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<PaginatedResponseDTO<UserList>> getAllUsers(
      @RequestParam @Positive Integer page, @RequestParam @Positive Integer size) {
    return ResponseEntity.status(200).body(queryService.getAllUsers(page, size));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN') and hasAuthority('DELETE_USER')")
  public ResponseEntity<?> deleteUser(@Positive Long id) {
    service.deleteUser(id);
    return ResponseEntity.status(200).build();
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN') and hasAuthority('UPDATE_USER')")
  public ResponseEntity<UserDetails> updateUser(@RequestBody UpdateUser dto, @Positive Long id) {

    return ResponseEntity.ok(service.updateUser(id, dto));
  }
}
