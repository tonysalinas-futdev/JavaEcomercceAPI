package com.example.Ecomercce.start.events;

import com.example.Ecomercce.users.models.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class RolesCreatedEvent {
  private Role userRole;
  private Role adminRole;
  private Role managerRole;
}
