package com.example.ecommerce.start.events;

import com.example.ecommerce.users.models.Role;
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
