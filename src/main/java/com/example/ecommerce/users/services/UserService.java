package com.example.ecommerce.users.services;

import com.example.ecommerce.auth.dtos.SignUpDTO;
import com.example.ecommerce.shared.exceptions.InvalidRequestException;
import com.example.ecommerce.shared.exceptions.NotFoundException;
import com.example.ecommerce.users.dtos.UpdatePasswordDTO;
import com.example.ecommerce.users.dtos.UpdateUserProfileDTO;
import com.example.ecommerce.users.dtos.UserProfileDTO;
import com.example.ecommerce.users.enums.RoleEnum;
import com.example.ecommerce.users.mappers.UserMappers;
import com.example.ecommerce.users.models.Role;
import com.example.ecommerce.users.models.User;
import com.example.ecommerce.users.repository.RoleRepository;
import com.example.ecommerce.users.repository.UserRepository;
import com.example.ecommerce.users.utils.BuilderUserUtil;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository repo;
  private final PasswordEncoder encoder;
  private final UserMappers mapper;
  private final UserQueryService queryService;
  private final RoleRepository roleRepo;

  @Transactional
  public UserProfileDTO updateProfile(UpdateUserProfileDTO dto, String userEmail) {
    User user = queryService.findByEmailOrThrow(userEmail);
    if (dto.email() != null) {
      queryService.findByEmailAndThrowIfExists(dto.email());
    }

      mapper.updateUserProfileWithDTO(dto, user);
      repo.saveAndFlush(user);
      log.info("Succesfully updated the profile of the user with email = {}", userEmail);
      return mapper.entityToUserProfileDTO(user);

  }

  @Transactional
  public void updatePassword(UpdatePasswordDTO data, String userEmail) {
    User user = queryService.findByEmailOrThrow(userEmail);
    if (encoder.matches(data.oldPassword(), user.getPassword())) {
      user.setPassword(encoder.encode(data.newPassword()));
      repo.save(user);
      log.info("Successfully updated password of user with email = {}", userEmail);
      return;
    }

    throw new InvalidRequestException("Invalid password");
  }

  @Transactional
  public User registerValidUser(@Valid SignUpDTO dto) {
    queryService.findByEmailAndThrowIfExists(dto.email());
    User user = BuilderUserUtil.build(dto);

    Role userRole =
        roleRepo
            .findByRoleEnum(RoleEnum.USER)
            .orElseThrow(() -> new NotFoundException("Role not found"));

    user.setPassword(encoder.encode(dto.password()));
    List<Role> updatedRoleList= user.getRoles();
    updatedRoleList.add(userRole);
    user.setRoles(updatedRoleList);

    User savedUser = repo.saveAndFlush(user);
    log.info("Register user with email = {}, id = {} , name = {}", dto.email(), savedUser.getId(), dto.name());

    return user;
  }
}
