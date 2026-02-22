package com.example.ecommerce.users.services;

import com.example.ecommerce.logger.annotations.LogDeleteEntityEvent;
import com.example.ecommerce.logger.annotations.LogUserEvent;
import com.example.ecommerce.shared.dtos.paginatedresponse.PaginatedResponseDTO;
import com.example.ecommerce.shared.exceptions.NotFoundException;
import com.example.ecommerce.shared.exceptions.PersistenceErrorException;
import com.example.ecommerce.shared.utils.PageableUtils;
import com.example.ecommerce.users.dtos.CreateUser;
import com.example.ecommerce.users.dtos.UpdateUser;
import com.example.ecommerce.users.dtos.UserDetails;
import com.example.ecommerce.users.dtos.UserList;
import com.example.ecommerce.users.helpers.UserServicesHelper;
import com.example.ecommerce.users.logs.events.UserEvents;
import com.example.ecommerce.users.mappers.UserMappers;
import com.example.ecommerce.users.models.Role;
import com.example.ecommerce.users.models.User;
import com.example.ecommerce.users.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@RequiredArgsConstructor
public class UserAdminService {
  private final UserRepository repo;
  private final UserMappers mapper;
  private final UserServicesHelper helper;

  public User getUserEntityById(Long userId) {
    User user = repo.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
    return user;
  }

  public User getUserByEmail(String email) {
    User user =
        repo.findUserByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
    return user;
  }

  public UserDetails getUserDetailsById(Long id) {
    User user = getUserEntityById(id);
    return mapper.entityToUserDetailsDto(user);
  }

  @Transactional
  @LogUserEvent(value = UserEvents.USER_CREATED, type = UserAdminService.class)
  public User createUserByAdmin(@Valid CreateUser dto) {
    helper.verifyExistingName(dto.getName());
    helper.verifyExistingEmail(dto.getEmail());

    User newUser = mapper.createUserDTOToEntity(dto);
    Role role = helper.getRoleByEnum(dto.getRole());
    newUser.setPassword(helper.encodePassword(newUser.getPassword()));
    newUser.setRole(role);

    try {
      repo.saveAndFlush(newUser);
    } catch (DataAccessException ex) {
      throw new PersistenceErrorException("Database Error", ex);
    }
    return newUser;
  }

  public PaginatedResponseDTO<UserList> getAllUsers(Integer page, Integer size) {
    var verifyPage = PageableUtils.verifyPage(page);
    var verifySize = PageableUtils.verifySize(size);
    Pageable pageable = PageRequest.of(verifyPage, verifySize);
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
  @LogUserEvent(value = UserEvents.USER_UPDATE, type = UserAdminService.class)
  public UserDetails updateUser(Long id, UpdateUser dto) {
    helper.verifyExistingEmail(dto.getEmail());
    helper.verifyExistingName(dto.getName());

    User user = mapper.updateUserDTOToEntity(dto, getUserEntityById(id));
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
    User user = getUserEntityById(id);
    try {
      repo.delete(user);
    } catch (DataAccessException ex) {
      throw new PersistenceErrorException("Error en la base de datos", ex);
    }
  }

  public User refreshUser(User user) {
    repo.saveAndFlush(user);
    return user;
  }
}
