package com.example.ecommerce.logger.builders.audit;

import com.example.ecommerce.users.enums.RoleEnum;
import com.example.ecommerce.users.logs.events.UserEvents;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StructuredUserEventLogWriter {
  Long id;
  UserEvents event;
  String name;
  String email;
  RoleEnum role;

  public StructuredUserEventLogWriter(
      UserEvents event, Long id, String userName, String email, RoleEnum role) {
    this.event = event;
    this.id = id;
    this.name = userName;
    this.email = email;
    this.role = role;
  }
}
