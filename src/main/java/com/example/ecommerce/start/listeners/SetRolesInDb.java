package com.example.ecommerce.start.listeners;

import com.example.ecommerce.start.events.RolesCreatedEvent;
import com.example.ecommerce.users.enums.RoleEnum;
import com.example.ecommerce.users.models.Role;
import com.example.ecommerce.users.repository.RoleRepository;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class SetRolesInDb {
  private RoleRepository repo;
  private ApplicationEventPublisher publisher;

  @EventListener
  public void saveRoles(ApplicationReadyEvent event) {
    Optional<Role> adminRole = repo.findByRoleEnum(RoleEnum.ADMIN);
    Optional<Role> userRole = repo.findByRoleEnum(RoleEnum.USER);
    Optional<Role> managerRole = repo.findByRoleEnum(RoleEnum.MANAGER);

    RolesCreatedEvent createdRolesEvent = new RolesCreatedEvent();
    if (adminRole.isEmpty()) {
      Role createdAdminRole = Role.builder().roleEnum(RoleEnum.ADMIN).build();
      repo.saveAndFlush(createdAdminRole);
      createdRolesEvent.setAdminRole(createdAdminRole);
    } else if (adminRole.isPresent()) {
      createdRolesEvent.setAdminRole(adminRole.get());
    }

    if (userRole.isEmpty()) {
      Role createdUserRole = Role.builder().roleEnum(RoleEnum.USER).build();
      repo.saveAndFlush(createdUserRole);
      createdRolesEvent.setUserRole(createdUserRole);
      ;
    } else if (userRole.isPresent()) {
      createdRolesEvent.setUserRole(userRole.get());
      ;
    }

    if (managerRole.isEmpty()) {
      Role createdManagerRole = Role.builder().roleEnum(RoleEnum.MANAGER).build();
      repo.saveAndFlush(createdManagerRole);
      createdRolesEvent.setManagerRole(createdManagerRole);
      ;
    } else if (managerRole.isPresent()) {
      createdRolesEvent.setManagerRole(managerRole.get());
    }

    publisher.publishEvent(createdRolesEvent);
  }
}
