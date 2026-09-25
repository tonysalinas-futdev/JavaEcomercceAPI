package com.example.ecommerce.integrationTests.users;

import com.example.ecommerce.users.dtos.CreateUserDTO;
import com.example.ecommerce.users.enums.RoleEnum;
import com.example.ecommerce.users.exceptions.AdminOperationException;
import com.example.ecommerce.users.models.User;
import com.example.ecommerce.users.services.AdminService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@Transactional
@Sql(
    scripts = {"/clean.sql", "/data.sql"},
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
public class TestAdminService {
  @Autowired private AdminService adminService;

  public User getTestUser() {
    CreateUserDTO dto =
        CreateUserDTO.builder()
            .email("email@email2.com")
            .name("Carlos")
            .role(RoleEnum.USER)
            .password("1234#Abcd")
            .build();

    return adminService.createUserByAdmin(dto);
  }

  public User getAdminForTests() {
    CreateUserDTO dto =
        CreateUserDTO.builder()
            .email("email@email.com")
            .name("Tony")
            .role(RoleEnum.ADMIN)
            .password("1234#Abcd")
            .build();

    return adminService.createUserByAdmin(dto);
  }

  @Test
  void shouldAddNewRoleToUser() {
    User userToAddNewRole = getTestUser();

    adminService.addNewRoleToUser(userToAddNewRole.getId(), RoleEnum.MANAGER);

    Assertions.assertTrue(
        userToAddNewRole.getRoles().stream()
            .anyMatch(r -> r.getRoleEnum().equals(RoleEnum.MANAGER)));
  }

  @Test
  void shouldFailToAddExistingRoleToUser() {
    User userToAddNewRole = getTestUser();

    Assertions.assertThrows(
        AdminOperationException.class,
        () -> adminService.addNewRoleToUser(userToAddNewRole.getId(), RoleEnum.USER));
  }

  @Test
  void shouldRemoveExistingRoleToUser() {
    User adminUser = getAdminForTests();
    User user = getTestUser();

    adminService.addNewRoleToUser(user.getId(), RoleEnum.MANAGER);
    adminService.removeUserRole(adminUser.getId(), user.getId(), RoleEnum.USER);

    Assertions.assertEquals(1, user.getRoles().size());
    Assertions.assertEquals(RoleEnum.MANAGER, user.getRoles().getFirst().getRoleEnum());
  }

  @Test
  void shouldFailToRemoveExistingRoleToOldestAdmin() {
    User adminUser = getAdminForTests();
    User user = getTestUser();

    adminService.addNewRoleToUser(user.getId(), RoleEnum.ADMIN);

    java.lang.Exception ex =
        Assertions.assertThrows(
            AdminOperationException.class,
            () -> adminService.removeUserRole(user.getId(), adminUser.getId(), RoleEnum.ADMIN));

    Assertions.assertEquals("You cannot revoke a role from a former admin", ex.getMessage());
  }

  @Test
  void shouldFailToRemoveLastRoleFromUser() {
    User adminUser = getAdminForTests();
    User user = getTestUser();

    java.lang.Exception ex =
        Assertions.assertThrows(
            AdminOperationException.class,
            () -> adminService.removeUserRole(adminUser.getId(), user.getId(), RoleEnum.USER));

    Assertions.assertEquals(
        "The user already have the minimum number of roles , add another before", ex.getMessage());
  }
}
