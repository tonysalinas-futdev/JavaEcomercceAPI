package com.example.ecommerce.users.services;

import com.example.ecommerce.auth.dtos.SignUpDTO;
import com.example.ecommerce.order.models.Order;
import com.example.ecommerce.shared.exceptions.InvalidRequestException;
import com.example.ecommerce.shared.exceptions.NotFoundException;
import com.example.ecommerce.shared.exceptions.PersistenceErrorException;
import com.example.ecommerce.users.dtos.UpdatePassword;
import com.example.ecommerce.users.dtos.UpdateUserProfile;
import com.example.ecommerce.users.dtos.UserProfile;
import com.example.ecommerce.users.enums.RoleEnum;
import com.example.ecommerce.users.mappers.UserMappers;
import com.example.ecommerce.users.models.Role;
import com.example.ecommerce.users.models.User;
import com.example.ecommerce.users.repository.RoleRepository;
import com.example.ecommerce.users.repository.UserRepository;
import com.example.ecommerce.users.utils.BuildUserUtil;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
  private final UserRepository repo;
  private final PasswordEncoder encoder;
  private final UserMappers mapper;
  private final UserQueryService queryService;
  private final RoleRepository roleRepo;

  @Transactional
  public UserProfile updateProfile(UpdateUserProfile dto, String userEmail) {
    User user = queryService.findByEmailOrThrow(userEmail);
    if (dto.getEmail() != null) {
      queryService.findByEmailAndThrowIfExists(dto.getEmail());
    }

    try {
      mapper.updateUserProfileWithDTO(dto, user);
      repo.saveAndFlush(user);
      return mapper.entityToUserProfileDTO(user);
    } catch (DataAccessException ex) {
      throw new PersistenceErrorException("Database Error", ex);
    }
  }

  @Transactional
  public void updatePassword(UpdatePassword data, String userEmail) {
    User user = queryService.findByEmailOrThrow(userEmail);
    if (encoder.matches(data.getOldPassword(), user.getPassword())) {
      user.setPassword(encoder.encode(data.getNewPassword()));
      repo.save(user);
      return;
    }

    throw new InvalidRequestException("Invalid password");
  }

  @Transactional
  public User registerValidUser(@Valid SignUpDTO dto) {
    queryService.findByEmailAndThrowIfExists(dto.getEmail());
    User user =BuildUserUtil.buildUser(dto);
    Role role = roleRepo.findByRoleEnum(RoleEnum.USER).orElseThrow(()-> new NotFoundException("Role USER not found"));
    user.setPassword(encoder.encode(dto.getPassword()));
    user.setRole(role);

    try {
      repo.saveAndFlush(user);

    } catch (DataAccessException ex) {
      throw new PersistenceErrorException("Database Error", ex);
    }
    return user;
  }
}
