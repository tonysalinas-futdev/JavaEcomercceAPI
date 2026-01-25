package com.example.Ecomercce.users.services;

import com.example.Ecomercce.auth.dtos.SignUpDTO;
import com.example.Ecomercce.logging.service.LoggerService;
import com.example.Ecomercce.shared.DTOs.paginatedDtos.PaginatedResponseDTO;
import com.example.Ecomercce.shared.exceptions.AlreadyExistsException;
import com.example.Ecomercce.shared.exceptions.NotFoundException;
import com.example.Ecomercce.shared.exceptions.PersistenceErrorException;
import com.example.Ecomercce.users.dtos.CreateUser;
import com.example.Ecomercce.users.dtos.UpdateUser;
import com.example.Ecomercce.users.dtos.UserDetails;
import com.example.Ecomercce.users.dtos.UserList;
import com.example.Ecomercce.users.enums.RoleEnum;
import com.example.Ecomercce.users.mappers.UserMappers;
import com.example.Ecomercce.users.models.Role;
import com.example.Ecomercce.users.models.User;
import com.example.Ecomercce.users.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@AllArgsConstructor
public class UserAdminService {
  private final UserRepository repo;
  private final UserMappers mapper;
  private final LoggerService logger;
  private final PasswordEncoder encoder;
  private final RoleService roleService;

  public User getUserEntityById(Long userId) {
    User user =
        repo.findById(userId)
            .orElseThrow(() -> new NotFoundException("No se ha encontrado al usuario"));

    return user;
  }

  public User getUserByName(String name) {
    User user =
        repo.findUserByName(name)
            .orElseThrow(() -> new NotFoundException("No se ha encontrado al usuario"));

    return user;
  }

  public User getUserByEmail(String email) {
    User user =
        repo.findUserByEmail(email)
            .orElseThrow(() -> new NotFoundException("No se ha encontrado al usuario"));

    return user;
  }

  public UserDetails getUserDetailsById(Long id) {
    User user = getUserEntityById(id);
    return mapper.entityToUserDetailsDto(user);
  }

  @Transactional
  public User createUserBySignUp(@Valid SignUpDTO dto) {
    if (repo.findUserByEmail(dto.getEmail()).isPresent()) {
      throw new AlreadyExistsException("Ya existe ese email");
    }
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

    try {
      repo.saveAndFlush(user);

      logger.addTypeOfLog("bussiness");
      logger.createBusinnessEventLog("user_created", "createUser", "user_id", user.getId());

    } catch (DataAccessException ex) {
      throw new PersistenceErrorException("Error en la base de datos", ex);
    }
    return user;
  }

  @Transactional
  public User createUserByAdmin(@Valid CreateUser dto) {
    if (repo.findUserByName(dto.getName()).isPresent()) {
      throw new AlreadyExistsException("Ya existe un usuario con ese nombre");
    }

    if (repo.findUserByEmail(dto.getEmail()).isPresent()) {
      throw new AlreadyExistsException("Ya existe un usuario con ese email");
    }
    User newUser = mapper.createUserDTOToEntity(dto);
    Role role = roleService.getRoleByEnum(dto.getRole());
    newUser.setPassword(encoder.encode(newUser.getPassword()));
    newUser.setRole(role);

    try {
      repo.saveAndFlush(newUser);
      logger.addTypeOfLog("bussiness");
      logger.createBusinnessEventLog("user_created", "createUser", "user_id", newUser.getId());
    } catch (DataAccessException ex) {
      throw new PersistenceErrorException("Error en la base de datos", ex);
    }
    return newUser;
  }

  public PaginatedResponseDTO<UserList> getAllUsers(Integer page, Integer size) {
    if (page == null) {
      page = 0;
    }
    if (size == null) {
      size = 10;
    }

    Pageable pageable = PageRequest.of(page, size);
    Page<User> users = repo.findAll(pageable);
    List<UserList> usersList = users.stream().map(u -> mapper.entityToUserListDTO(u)).toList();

    return new PaginatedResponseDTO<>(
        usersList,
        users.hasNext(),
        users.hasPrevious(),
        users.getNumber(),
        users.getTotalPages(),
        users.getTotalElements(),
        users.getNumberOfElements(),
        users.getSize());
  }

  @Transactional
  public UserDetails updateUser(Long id, UpdateUser dto) {
    if (repo.findUserByName(dto.getName()).isPresent()) {
      throw new AlreadyExistsException("Ya existe un usuario con ese nombre");
    }

    if (repo.findUserByEmail(dto.getEmail()).isPresent()) {
      throw new AlreadyExistsException("Ya existe un usuario con ese email");
    }
    User user = mapper.updateUserDTOToEntity(dto, getUserEntityById(id));
    try {
      repo.save(user);
      logger.addTypeOfLog("bussiness");
      logger.createBusinnessEventLog("updated_user", "updateUser", "user_id", user.getId());
    } catch (DataAccessException ex) {
      throw new PersistenceErrorException("Error en la base de datos", ex);
    }

    return mapper.entityToUserDetailsDto(user);
  }

  @Transactional
  public void deleteUser(Long id) {
    User user = getUserEntityById(id);
    try {
      repo.delete(user);
      logger.addTypeOfLog("bussiness");
      logger.createBusinnessEventLog("deleted_user", "deleteUser", "user_id", id);
    } catch (DataAccessException ex) {
      throw new PersistenceErrorException("Error en la base de datos", ex);
    }
  }

  public User refreshUser(User user) {
    repo.saveAndFlush(user);
    return user;
  }
}
