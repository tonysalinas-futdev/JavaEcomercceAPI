package com.example.ecommerce.start.listeners;

import com.example.ecommerce.start.events.RolesCreatedEvent;
import com.example.ecommerce.start.events.SettedPermissionsEvent;
import com.example.ecommerce.users.models.Permission;
import com.example.ecommerce.users.models.Role;
import com.example.ecommerce.users.repository.PermissionRepository;
import com.example.ecommerce.users.repository.RoleRepository;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class SetPermissionsInDb {
  private final RoleRepository roleRepo;
  private final PermissionRepository repo;
  private final ApplicationEventPublisher publisher;

  private Permission buildPermission(String permissionName, Set<Role> roles) {
    return Permission.builder().permissionName(permissionName).roles(roles).build();
  }

  @EventListener
  public void setPermissions(RolesCreatedEvent event) {
    Role adminRole = event.getAdminRole();
    Role managerRole = event.getManagerRole();

    Permission editCataloguePermission =
        buildPermission("EDIT_CATALOGUE", Set.of(adminRole, managerRole));

    Permission createUser = buildPermission("CREATE_USER", Set.of(adminRole));

    Permission deleteUser = buildPermission("DELETE_USER", Set.of(adminRole));

    Permission updateUser = buildPermission("UPDATE_USER", Set.of(adminRole));

    adminRole.setPermissions(Set.of(editCataloguePermission, createUser, deleteUser, updateUser));
    managerRole.setPermissions(Set.of(editCataloguePermission));

    repo.saveAll(List.of(editCataloguePermission, createUser, deleteUser, updateUser));
    publisher.publishEvent(new SettedPermissionsEvent());
  }
}
