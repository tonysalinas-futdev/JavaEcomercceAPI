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
import com.example.ecommerce.users.helpers.UserServicesHelper;
import com.example.ecommerce.users.mappers.UserMappers;
import com.example.ecommerce.users.models.Role;
import com.example.ecommerce.users.models.User;
import com.example.ecommerce.users.repository.UserRepository;
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
  private final UserServicesHelper helper;

  private User buildUser(@Valid SignUpDTO dto) {
    User user =
        User.builder()
            .name(dto.getName())
            .email(dto.getEmail())
            .password(encoder.encode(dto.getPassword()))
            .isEnabled(true)
            .accountNoLocked(true)
            .build();
    Role role = helper.getRoleByEnum(RoleEnum.USER);
    user.setRole(role);
    return user;
  }

  public User getUserByEmail(String email) {
    return repo.findUserByEmail(email).orElseThrow(() -> new NotFoundException("Email not found"));
  }

  public List<Order> getUserByEmailAndLoadOrders(String email) {
    var user =
        repo.findByEmailAndLoadOrders(email)
            .orElseThrow(() -> new NotFoundException("Email not found"));
    return user.getOrders();
  }

  public UserProfile getProfile(String email) {
    User user = getUserByEmail(email);
    return mapper.entityToUserProfileDTO(user);
  }

  @Transactional
  public UserProfile updateProfile(UpdateUserProfile dto, String userEmail) {
    User user = getUserByEmail(userEmail);
    if (dto.getEmail() != null) {
      helper.verifyExistingEmail(dto.getEmail());
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
    User user = getUserByEmail(userEmail);
    if (encoder.matches(data.getOldPassword(), user.getPassword())) {
      user.setPassword(encoder.encode(data.getNewPassword()));
      repo.save(user);
      return;
    }

    throw new InvalidRequestException("Invalid password");
  }

  @Transactional
  public User registerValidUser(@Valid SignUpDTO dto) {
    helper.verifyExistingEmail(dto.getEmail());
    User user = buildUser(dto);

    try {
      repo.saveAndFlush(user);

    } catch (DataAccessException ex) {
      throw new PersistenceErrorException("Database Error", ex);
    }
    return user;
  }
}
