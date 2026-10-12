package com.example.ecommerce.users.services;

import com.example.ecommerce.shared.exceptions.NotFoundException;
import com.example.ecommerce.users.dtos.CreateUserDTO;
import com.example.ecommerce.users.dtos.UpdateUserDTO;
import com.example.ecommerce.users.dtos.UserDetailsDTO;
import com.example.ecommerce.users.enums.RoleEnum;
import com.example.ecommerce.users.exceptions.AdminOperationException;
import com.example.ecommerce.users.mappers.UserMappers;
import com.example.ecommerce.users.models.Role;
import com.example.ecommerce.users.models.User;
import com.example.ecommerce.users.repository.RoleRepository;
import com.example.ecommerce.users.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class AdminService {
  private final UserRepository repo;
  private final UserMappers mapper;
  private final UserQueryService queryService;
  private final RoleRepository roleRepo;
  private final PasswordEncoder encoder;

  @Transactional
  public User createUserByAdmin(@Valid CreateUserDTO dto) {
    queryService.findByNameAndThrowIfExists(dto.name());
    queryService.findByEmailAndThrowIfExists(dto.email());

    User newUser = mapper.createUserDTOToEntity(dto);
    newUser.setAccountLocked(false);
    newUser.setCredentialsExpired(false);
    newUser.setIsEnabled(true);

    Role role =
        roleRepo
            .findByRoleEnum(dto.role())
            .orElseThrow(() -> new NotFoundException("Role" + dto.role() + "not found"));

    newUser.setPassword(encoder.encode(newUser.getPassword()));
    newUser.getRoles().add(role);
    repo.saveAndFlush(newUser);
    log.atInfo()
        .addKeyValue("event_type", "user_created_by_admin")
        .addKeyValue("user_id", newUser.getId())
        .addKeyValue("email", newUser.getEmail())
        .addKeyValue("roles", newUser.getRoles())
        .log("Successfully created user");

    return newUser;
  }

  @Transactional
  public void addNewRoleToUser(Long userId, RoleEnum role) {

    User user = queryService.findEntityByIdOrThrow(userId);
    Role roleToAdd =
        roleRepo.findByRoleEnum(role).orElseThrow(() -> new NotFoundException("Role not found"));
    if (user.getRoles().contains(roleToAdd)) {
      throw new AdminOperationException("Cannot grant a role to a user who already holds it");
    }

    user.getRoles().add(roleToAdd);
    repo.saveAndFlush(user);

    log.atInfo()
        .addKeyValue("event_type", "added_role_to_user")
        .addKeyValue("user_id", user.getId())
        .addKeyValue("added_role", role.toString())
        .log("Successfully added new role to user");
  }

  @Transactional
  public void removeUserRole(Long adminId, Long userId, RoleEnum role) {
    User user = queryService.findEntityByIdOrThrow(userId);
    User adminUser = queryService.findEntityByIdOrThrow(adminId);
    Role roleToAdd =
        roleRepo.findByRoleEnum(role).orElseThrow(() -> new NotFoundException("Role not found"));
    removeUserRoleConditionals(adminUser, user, roleToAdd);
    user.getRoles().removeIf(r -> r.getRoleEnum().equals(role));
    repo.saveAndFlush(user);
    log.atInfo()
        .addKeyValue("event_type", "user_role_removed")
        .addKeyValue("user_id", user.getId())
        .addKeyValue("removed_role", role.toString())
        .log("Successfully removed role from user");
  }

  public void removeUserRoleConditionals(User adminUser, User user, Role role) {
    if (!user.getRoles().contains(role)) {
      throw new AdminOperationException("Cannot remove a role that user does not possess ");
    }

    if (adminUser.getCreatedAt().isAfter(user.getCreatedAt())
        && user.getRoles().stream().anyMatch(r -> r.getRoleEnum().equals(RoleEnum.ADMIN))) {
      throw new AdminOperationException("You cannot revoke a role from a former admin");
    }

    if (user.getRoles().size() == 1) {
      throw new AdminOperationException(
          "The user already have the minimum number of roles , add another before");
    }
  }

  @Transactional
  public UserDetailsDTO updateUser(Long id, UpdateUserDTO dto) {
    queryService.findByEmailAndThrowIfExists(dto.email());
    queryService.findByNameAndThrowIfExists(dto.name());

    User user = mapper.updateUserDTOToEntity(dto, queryService.findEntityByIdOrThrow(id));
    repo.save(user);
    log.atInfo()
        .addKeyValue("event_type", "updated_user")
        .addKeyValue("user_id", user.getId())
        .log("Successfully updated user");

    return mapper.entityToUserDetailsDto(user);
  }

  public void blockAccount(Long adminId, Long userToBlockId) {
    User user = queryService.findEntityByIdOrThrow(userToBlockId);
    User adminUser = queryService.findEntityByIdOrThrow(adminId);

    if (adminUser.getCreatedAt().isAfter(user.getCreatedAt())
        && user.getRoles().stream().anyMatch(r -> r.getRoleEnum().equals(RoleEnum.ADMIN))
        && user.getRoles().stream().anyMatch(r -> r.getRoleEnum().equals(RoleEnum.ADMIN))) {
      throw new AdminOperationException("You cannot block the account of a former admin");
    }
    user.setAccountLocked(true);
    repo.saveAndFlush(user);
    log.atInfo()
        .addKeyValue("event_type", "account_blocked")
        .addKeyValue("user_id", user.getId())
        .log("Successfully blocked user account");
  }

  public void unlockAccount(Long userToUnlockId) {
    User user = queryService.findEntityByIdOrThrow(userToUnlockId);
    user.setAccountLocked(false);
    repo.saveAndFlush(user);

    log.atInfo()
        .addKeyValue("event_type", "account_unlocked")
        .addKeyValue("user_id", user.getId())
        .log("Successfully unlocked user account");
  }

  @Transactional
  public void deleteUser(Long id) {
    repo.delete(queryService.findEntityByIdOrThrow(id));
    log.atInfo()
        .addKeyValue("event_type", "delete_user")
        .addKeyValue("user_id", id)
        .log("Successfully deleted user account");
  }
}
