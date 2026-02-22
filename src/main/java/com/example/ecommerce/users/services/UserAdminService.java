package com.example.ecommerce.users.services;

import com.example.ecommerce.logger.annotations.LogDeleteEntityEvent;
import com.example.ecommerce.logger.annotations.LogUserEvent;
import com.example.ecommerce.shared.exceptions.NotFoundException;
import com.example.ecommerce.shared.exceptions.PersistenceErrorException;
import com.example.ecommerce.users.dtos.CreateUser;
import com.example.ecommerce.users.dtos.UpdateUser;
import com.example.ecommerce.users.dtos.UserDetails;
import com.example.ecommerce.users.enums.RoleEnum;
import com.example.ecommerce.users.logs.events.UserEvents;
import com.example.ecommerce.users.mappers.UserMappers;
import com.example.ecommerce.users.models.Role;
import com.example.ecommerce.users.models.User;
import com.example.ecommerce.users.repository.RoleRepository;
import com.example.ecommerce.users.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@RequiredArgsConstructor
public class UserAdminService {
  private final UserRepository repo;
  private final UserMappers mapper;
  private final UserQueryService queryService;
  private final RoleRepository roleRepo;
  private final PasswordEncoder encoder;

  @Transactional
  @LogUserEvent(value = UserEvents.USER_CREATED, type = UserAdminService.class)
  public User createUserByAdmin(@Valid CreateUser dto) {
    queryService.findByNameAndThrowIfExists(dto.getName());
    queryService.findByEmailAndThrowIfExists(dto.getEmail());

    User newUser = mapper.createUserDTOToEntity(dto);
    Role role =
        roleRepo
            .findByRoleEnum(RoleEnum.ADMIN)
            .orElseThrow(() -> new NotFoundException("Role USER not found"));
    newUser.setPassword(encoder.encode(newUser.getPassword()));
    newUser.setRole(role);

    try {
      repo.saveAndFlush(newUser);
      return newUser;
    } catch (DataAccessException ex) {
      throw new PersistenceErrorException("Database Error", ex);
    }
  }

  @Transactional
  @LogUserEvent(value = UserEvents.USER_UPDATE, type = UserAdminService.class)
  public UserDetails updateUser(Long id, UpdateUser dto) {
    queryService.findByEmailAndThrowIfExists(dto.getEmail());
    queryService.findByNameAndThrowIfExists(dto.getName());

    User user = mapper.updateUserDTOToEntity(dto, queryService.findEntityByIdOrThrow(id));
    try {
      repo.save(user);
    } catch (DataAccessException ex) {
      throw new PersistenceErrorException("Database Error", ex);
    }

    return mapper.entityToUserDetailsDto(user);
  }

  @Transactional
  @LogDeleteEntityEvent(event = "USER_DELETED", loggerName = UserAdminService.class)
  public void deleteUser(Long id) {
    User user = queryService.findEntityByIdOrThrow(id);
    try {
      repo.delete(user);
    } catch (DataAccessException ex) {
      throw new PersistenceErrorException("Error en la base de datos", ex);
    }
  }
}
