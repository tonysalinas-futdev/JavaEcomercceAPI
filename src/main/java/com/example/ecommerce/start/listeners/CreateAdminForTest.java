package com.example.ecommerce.start.listeners;

import com.example.ecommerce.start.events.SettedPermissionsEvent;
import com.example.ecommerce.users.dtos.CreateUser;
import com.example.ecommerce.users.enums.RoleEnum;
import com.example.ecommerce.users.services.UserAdminService;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CreateAdminForTest {
  private final UserAdminService service;

  @EventListener
  public void createAdmin(SettedPermissionsEvent event) {
    CreateUser dto =
        CreateUser.builder()
            .email("admintest@gmail.com")
            .name("admin test")
            .password("Abcd1234#")
            .role(RoleEnum.ADMIN)
            .build();
    service.createUserByAdmin(dto);
  }
}
