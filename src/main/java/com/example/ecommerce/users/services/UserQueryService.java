package com.example.ecommerce.users.services;

import com.example.ecommerce.shared.dtos.paginatedresponse.PaginatedResponseDTO;
import com.example.ecommerce.shared.exceptions.AlreadyExistsException;
import com.example.ecommerce.shared.exceptions.NotFoundException;
import com.example.ecommerce.shared.utils.PageableUtils;
import com.example.ecommerce.users.dtos.UserDetailsDTO;
import com.example.ecommerce.users.dtos.UserListDTO;
import com.example.ecommerce.users.dtos.UserProfileDTO;
import com.example.ecommerce.users.mappers.UserMappers;
import com.example.ecommerce.users.models.User;
import com.example.ecommerce.users.repository.UserRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserQueryService {
  private final UserRepository repo;
  private final UserMappers mapper;

  public User findEntityByIdOrThrow(Long userId) {
    return repo.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
  }

  public User findByEmailOrThrow(String email) {
    return repo.findUserByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
  }

  public UserDetailsDTO findByIdAndReturnDetailsDto(Long id) {
    User user = findEntityByIdOrThrow(id);
    return mapper.entityToUserDetailsDto(user);
  }

  @Cacheable(value = "users", key = "#email")
  public UserProfileDTO findByEmailAndReturnProfileDto(String email) {
    User user = findByEmailOrThrow(email);
    return mapper.entityToUserProfileDTO(user);
  }

  public void findByEmailAndThrowIfExists(String email) {
    if (repo.findUserByEmail(email).isPresent()) {
      throw new AlreadyExistsException("Email already exists");
    }
  }

  public void findByNameAndThrowIfExists(String name) {
    if (repo.findUserByName(name).isPresent()) {
      throw new AlreadyExistsException("Name already exists");
    }
  }

  public PaginatedResponseDTO<UserListDTO> getAllUsers(Integer page, Integer size) {
    var verifyPage = PageableUtils.verifyPage(page);
    var verifySize = PageableUtils.verifySize(size);
    Pageable pageable = PageRequest.of(verifyPage, verifySize);
    Page<User> users = repo.findAll(pageable);
    List<UserListDTO> usersList = users.stream().map(mapper::entityToUserListDTO).toList();

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
}
