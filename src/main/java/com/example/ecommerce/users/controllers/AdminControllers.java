package com.example.ecommerce.users.controllers;

import com.example.ecommerce.shared.utils.PaginationCriteria;
import com.example.ecommerce.users.dtos.CreateUserDTO;
import com.example.ecommerce.users.dtos.UpdateUserDTO;
import com.example.ecommerce.users.dtos.UserDetailsDTO;
import com.example.ecommerce.users.dtos.UserListDTO;
import com.example.ecommerce.users.models.User;
import com.example.ecommerce.users.services.AdminService;
import com.example.ecommerce.users.services.UserQueryService;
import com.example.ecommerce.users.utils.UserSortCriteria;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.net.URI;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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
  private final AdminService service;
  private final UserQueryService queryService;

  @PostMapping()
  @PreAuthorize("hasRole('ADMIN') and hasAuthority('CREATE_USER')")
  public ResponseEntity<?> createUser(@RequestBody @Valid CreateUserDTO dto) {
    User user = service.createUserByAdmin(dto);
    URI location = URI.create("/api/v1/users/" + user.getId());
    return ResponseEntity.created(location).body(user);
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<UserDetailsDTO> getUserById(@PathVariable @Positive Long id) {
    return ResponseEntity.status(200).body(queryService.findByIdAndReturnDetailsDto(id));
  }

  @GetMapping()
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Page<UserListDTO>> getAllUsers(
      @RequestParam(required = false, defaultValue = "0") Integer page,
      @RequestParam(required = false, defaultValue = "10") Integer items,
      @RequestParam(required = false, defaultValue = "true") Boolean ascending,
      @RequestParam(required = false, defaultValue = "id") UserSortCriteria userSortCriteria) {
    PaginationCriteria<UserSortCriteria> paginationCriteria =
        new PaginationCriteria<>(page, items, userSortCriteria, ascending);
    return ResponseEntity.status(200).body(queryService.getAllUsers(paginationCriteria));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN') and hasAuthority('DELETE_USER')")
  public ResponseEntity<?> deleteUser(@Positive Long id) {
    service.deleteUser(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN') and hasAuthority('UPDATE_USER')")
  public ResponseEntity<UserDetailsDTO> updateUser(
      @RequestBody UpdateUserDTO dto, @Positive Long id) {

    return ResponseEntity.ok(service.updateUser(id, dto));
  }

  @PutMapping("/{id}/lock")
  @PreAuthorize("hasRole('ADMIN') and hasAuthority('UPDATE_USER')")
  public ResponseEntity<Void> lockAccount(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
    service.blockAccount(jwt.getClaim("id"), id);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/{id}/unlock")
  @PreAuthorize("hasRole('ADMIN') and hasAuthority('UPDATE_USER')")
  public ResponseEntity<Void> unlockAccount(@PathVariable Long id) {
    service.unlockAccount(id);
    return ResponseEntity.ok().build();
  }
}
