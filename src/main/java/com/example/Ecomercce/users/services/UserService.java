package com.example.Ecomercce.users.services;

import com.example.Ecomercce.logging.service.LoggerService;
import com.example.Ecomercce.shared.exceptions.AlreadyExistsException;
import com.example.Ecomercce.shared.exceptions.InvalidRequestException;
import com.example.Ecomercce.shared.exceptions.NotFoundException;
import com.example.Ecomercce.shared.exceptions.PersistenceErrorException;
import com.example.Ecomercce.users.dtos.UpdatePassword;
import com.example.Ecomercce.users.dtos.UpdateUserProfile;
import com.example.Ecomercce.users.dtos.UserProfile;
import com.example.Ecomercce.users.mappers.UserMappers;
import com.example.Ecomercce.users.models.User;
import com.example.Ecomercce.users.repository.UserRepository;
import jakarta.transaction.Transactional;
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
    if (repo.findUserByEmail(dto.getEmail()).isPresent()) {
      throw new AlreadyExistsException("Ya existe un usuario con ese email");
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
}
