package com.example.ecommerce.integrationTests.users;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.ecommerce.auth.dtos.SignUpDTO;
import com.example.ecommerce.users.dtos.UpdateUser;
import com.example.ecommerce.users.dtos.UserDetails;
import com.example.ecommerce.users.enums.RoleEnum;
import com.example.ecommerce.users.models.User;
import com.example.ecommerce.users.services.UserAdminService;
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
public class testUserService {
  @Autowired private UserAdminService service;
  @Autowired private UserService userService;
  @Autowired private UserConftest conftest;

  @Test
  public void mustCreateAndReturnUser() {
    User user =
        service.createUserByAdmin(
            conftest.buildCreateUserDto(
                "emailexample@gmail.com", "Abc123456#", "Pedro Gonzalez", RoleEnum.USER));

    assertTrue(user.getName().equals("Pedro Gonzalez"));
    assertTrue(user.getId() != null);
  }

  @ParameterizedTest
  @CsvSource({
    "Juan Carlos Salinas, invalidemail.cu, Abcd123456#",
    ", user@gmail.cu, Abcd123456#",
    "Juan Antonio Salinas, user@gmail.cu, invalidpassword",
  })
  public void shouldFailToCreateUserGivenInvalidInput(String name, String email, String password) {
    assertThrows(
        ConstraintViolationException.class,
        () -> {
          service.createUserByAdmin(
              conftest.buildCreateUserDto(email, password, name, RoleEnum.USER));
        });
  }

  @Test
  public void shouldCreateUserBySignUp() {
    SignUpDTO data =
        conftest.buildSignUpDto(
            "Eduardo Camavinga Celmi", "camavinguismo@gmail.com", "Abcd123456#");

    User user = userService.registerValidUser(data);

    assertTrue(user.getName().equals("Eduardo Camavinga Celmi"));
    assertTrue(user.getId() != null);
    assertTrue(user.getRole().getRoleEnum().equals(RoleEnum.USER));
  }

  @Test
  public void shouldUpdateUser() {
    User user =
        service.createUserByAdmin(
            conftest.buildCreateUserDto(
                "vini@gmail.com", "Abcd1234#", "Vini Junior", RoleEnum.USER));
    UpdateUser data = UpdateUser.builder().email("updated_email").name("updated_name").build();

    UserDetails updateUser = service.updateUser(user.getId(), data);

    assertTrue(updateUser.getName().equals("updated_name"));
  }
}
