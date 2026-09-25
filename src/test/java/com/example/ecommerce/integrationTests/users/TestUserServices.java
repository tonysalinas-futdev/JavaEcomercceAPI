package com.example.ecommerce.integrationTests.users;

import static org.junit.jupiter.api.Assertions.*;

import com.example.ecommerce.auth.dtos.SignUpDTO;
import com.example.ecommerce.users.dtos.UpdateUserDTO;
import com.example.ecommerce.users.dtos.UserDetailsDTO;
import com.example.ecommerce.users.enums.RoleEnum;
import com.example.ecommerce.users.models.User;
import com.example.ecommerce.users.services.AdminService;
import com.example.ecommerce.users.services.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ActiveProfiles("test")
@Transactional
@Sql(
    scripts = {"/clean.sql", "/data.sql"},
    executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
public class TestUserServices {
  @Autowired private AdminService service;
  @Autowired private UserService userService;
  @Autowired private UserConftest conftest;

  @Test
  public void shouldCreateUserByAdminSuccessfully() {
    User user =
        service.createUserByAdmin(
            conftest.buildCreateUserDto(
                "emailexample@gmail.com", "Abc123456#", "Pedro Gonzalez", RoleEnum.USER));

    assertEquals("Pedro Gonzalez", user.getName());
    assertEquals(RoleEnum.USER, user.getRoles().getFirst().getRoleEnum());
    assertNotNull(user.getId());
  }

  @ParameterizedTest
  @CsvSource({
    "Juan Carlos Salinas, invalidemail.cu, Abcd123456#",
    ", user@gmail.cu, Abcd123456#",
    "Juan Antonio Salinas, user@gmail.cu, invalidpassword",
  })
  public void shouldFailToCreateUserGivenInvalidEmailNullNameAndInvalidPassword(
      String name, String email, String password) {
    assertThrows(
        ConstraintViolationException.class,
        () -> {
          service.createUserByAdmin(
              conftest.buildCreateUserDto(email, password, name, RoleEnum.USER));
        });
  }

  @Test
  public void shouldCreateUserBySignUpSuccessfully() {
    SignUpDTO data =
        conftest.buildSignUpDto(
            "Eduardo Camavinga Celmi", "camavinguismo@gmail.com", "Abcd123456#");

    User user = userService.registerUser(data);

    assertEquals("Eduardo Camavinga Celmi", user.getName());
    assertNotNull(user.getId());
    assertEquals(RoleEnum.USER, user.getRoles().getFirst().getRoleEnum());
  }

  @Test
  public void shouldUpdateUserSuccessfully() {
    User user =
        service.createUserByAdmin(
            conftest.buildCreateUserDto(
                "vini@gmail.com", "Abcd1234#", "Vini Junior", RoleEnum.USER));
    UpdateUserDTO data =
        UpdateUserDTO.builder().email("updated_email").name("updated_name").build();

    UserDetailsDTO updateUser = service.updateUser(user.getId(), data);

    assertEquals("updated_name", updateUser.name());
    assertEquals("updated_email", updateUser.email());
  }
}
