package com.example.ecommerce.logger.mappers;

import com.example.ecommerce.logger.builders.audit.StructuredUserEventLogWriter;
import com.example.ecommerce.users.dtos.UserDetails;
import com.example.ecommerce.users.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserLogMapper {
  @Mapping(source = "role.roleEnum", target = "role")
  StructuredUserEventLogWriter entityToStructuredLog(User user);

  StructuredUserEventLogWriter detailsDtoToStructuredLog(UserDetails user);
}
