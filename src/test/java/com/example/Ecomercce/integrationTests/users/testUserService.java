package com.example.Ecomercce.integrationTests.users;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.Ecomercce.auth.dtos.SignUpDTO;
import com.example.Ecomercce.logging.service.LoggerService;
import com.example.Ecomercce.users.enums.RoleEnum;
import com.example.Ecomercce.users.models.User;
import com.example.Ecomercce.users.services.UserAdminService;
import jakarta.validation.ConstraintViolationException;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class testUserService {
  @Autowired private UserAdminService service;
  @Autowired private UserConftest conftest;
  @Autowired private LoggerService logger;

  @Test
  @Sql("/data.sql")
  @DirtiesContext
  public void testCreateUserByAdminHappyPath() {
    User user =
        service.createUserByAdmin(
            conftest.buildCreateUserDto(
                "emailexample@gmail.com", "Abc123456#", "Pedro Gonzalez", RoleEnum.USER));

    logger.logDebug(Map.of("name", user.getName(), "email", user.getEmail(), "id", user.getId()));

    assertTrue(user.getName().equals("Pedro Gonzalez"));
    assertTrue(user.getId() != null);
  }

  @ParameterizedTest
  @CsvSource({
    "Juan Carlos Salinas, usergmail.cu, Abcd123456#",
    ", user@gmail.cu, Abcd123456#",
    "Juan Antonio Salinas, user@gmail.cu, A456#",
  })
  @Sql("/data.sql")
  @DirtiesContext
  public void createUserByAdminFail(String name, String email, String password) {
    assertThrows(
        ConstraintViolationException.class,
        () -> {
          service.createUserByAdmin(
              conftest.buildCreateUserDto(email, password, name, RoleEnum.USER));
        });
  }

  @Test
  @Sql("/data.sql")
  @DirtiesContext
  public void testCreateuserBySignUp() {
    SignUpDTO data =
        conftest.buildSignUpDto(
            "Eduardo Camavinga Celmi", "camavinguismo@gmail.com", "Abcd123456#");

    User user = service.createUserBySignUp(data);

    assertTrue(user.getName().equals("Eduardo Camavinga Celmi"));
    assertTrue(user.getId() != null);
    assertTrue(user.getRole().getRoleEnum().equals(RoleEnum.USER));
  }
}
