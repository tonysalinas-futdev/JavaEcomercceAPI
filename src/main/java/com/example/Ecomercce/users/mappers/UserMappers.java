package com.example.Ecomercce.users.mappers;

import com.example.Ecomercce.users.dtos.CreateUser;
import com.example.Ecomercce.users.dtos.UpdateUser;
import com.example.Ecomercce.users.dtos.UpdateUserProfile;
import com.example.Ecomercce.users.dtos.UserDetails;
import com.example.Ecomercce.users.dtos.UserList;
import com.example.Ecomercce.users.dtos.UserProfile;
import com.example.Ecomercce.users.models.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UserMappers {
  User createUserDTOToEntity(CreateUser dto);

  UserDetails entityToUserDetailsDto(User entity);

  UserList entityToUserListDTO(User entity);

  UserProfile entityToUserProfileDTO(User entity);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  User updateUserDTOToEntity(UpdateUser dto, @MappingTarget User entity);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  User updateUserProfileWithDTO(UpdateUserProfile dto, @MappingTarget User entity);
}
