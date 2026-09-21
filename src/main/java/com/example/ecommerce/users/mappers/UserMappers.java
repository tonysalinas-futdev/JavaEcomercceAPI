package com.example.ecommerce.users.mappers;

import com.example.ecommerce.users.dtos.*;
import com.example.ecommerce.users.dtos.UserDetailsDTO;
import com.example.ecommerce.users.models.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UserMappers {
  User createUserDTOToEntity(CreateUserDTO dto);

  UserDetailsDTO entityToUserDetailsDto(User entity);

  UserListDTO entityToUserListDTO(User entity);

  UserProfileDTO entityToUserProfileDTO(User entity);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  User updateUserDTOToEntity(UpdateUserDTO dto, @MappingTarget User entity);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  User updateUserProfileWithDTO(UpdateUserProfileDTO dto, @MappingTarget User entity);
}
