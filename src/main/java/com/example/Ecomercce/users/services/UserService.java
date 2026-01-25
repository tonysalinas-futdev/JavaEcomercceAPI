package com.example.Ecomercce.users.services;

import com.example.Ecomercce.auth.dtos.SignUpDTO;
import com.example.Ecomercce.logging.service.LoggerService;
import com.example.Ecomercce.shared.exceptions.AlreadyExistsException;
import com.example.Ecomercce.shared.exceptions.InvalidRequestException;
import com.example.Ecomercce.shared.exceptions.NotFoundException;
import com.example.Ecomercce.shared.exceptions.PersistenceErrorException;
import com.example.Ecomercce.users.dtos.UpdatePassword;
import com.example.Ecomercce.users.dtos.UpdateUserProfile;
import com.example.Ecomercce.users.dtos.UserProfile;
import com.example.Ecomercce.users.enums.RoleEnum;
import com.example.Ecomercce.users.mappers.UserMappers;
import com.example.Ecomercce.users.models.Role;
import com.example.Ecomercce.users.models.User;
import com.example.Ecomercce.users.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
  private final UserRepository repo;
  private final LoggerService logger;
  private final PasswordEncoder encoder;
  private final UserMappers mapper;
  private final RoleService roleService;

  private User buildUser(@Valid SignUpDTO dto) {
    User user =
        User.builder()
            .name(dto.getName())
            .email(dto.getEmail())
            .password(encoder.encode(dto.getPassword()))
            .isEnabled(true)
            .accountNoLocked(true)
            .build();
    Role role = roleService.getRoleByEnum(RoleEnum.USER);
    user.setRole(role);
    return user;
  }

  private void verifyExistingEmail(String email) {
    if (getUserByEmail(email) != null) {
      throw new AlreadyExistsException("Ya existe ese email");
    }
  }

  public User getUserByEmail(String email) {
    return repo.findUserByEmail(email).orElseThrow(() -> new NotFoundException("Email not found"));
  }

  public UserProfile getProfile(String email) {
    User user = getUserByEmail(email);
    logger.addTypeOfLog("business");

    return mapper.entityToUserProfileDTO(user);
  }

  @Transactional
  public UserProfile updateProfile(UpdateUserProfile dto, String userEmail) {
    User user = getUserByEmail(userEmail);
    if (dto.getEmail() != null) {
      verifyExistingEmail(dto.getEmail());
    }

    try {
      mapper.updateUserProfileWithDTO(dto, user);
      repo.saveAndFlush(user);
      logger.addTypeOfLog("business");
      logger.createBusinnessEventLog("update_profile", "updateProfile", "user_id", user.getId());
      return mapper.entityToUserProfileDTO(user);
    } catch (DataAccessException ex) {
      throw new PersistenceErrorException("Error en la base de datos", ex);
    }
  }

  @Transactional
  public void updatePassword(UpdatePassword data, String userEmail) {
    User user = getUserByEmail(userEmail);
    if (encoder.matches(data.getOldPassword(), user.getPassword())) {
      user.setPassword(encoder.encode(data.getNewPassword()));
      repo.save(user);
      logger.addTypeOfLog("business");
      logger.createBusinnessEventLog("updated_password", "updatePassword", "user_id", user.getId());
      return;
    }

    throw new InvalidRequestException("Error en la contraseña enviada");
  }

  @Transactional
  public User saveRegisterUser(@Valid SignUpDTO dto) {
    verifyExistingEmail(dto.getEmail());
    User user = buildUser(dto);

    try {
      repo.saveAndFlush(user);
      logger.addTypeOfLog("bussiness");
      logger.createBusinnessEventLog("user_created", "createUser", "user_id", user.getId());

    } catch (DataAccessException ex) {
      throw new PersistenceErrorException("Error en la base de datos", ex);
    }
    return user;
  }
}
