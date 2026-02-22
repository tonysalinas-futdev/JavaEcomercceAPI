package com.example.ecommerce.unit_tests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.ecommerce.shared.utils.Prueba;
import com.example.ecommerce.users.dtos.CreateUser;
import com.example.ecommerce.users.enums.RoleEnum;
import com.example.ecommerce.users.models.User;
import com.example.ecommerce.users.services.UserAdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Sql(
    scripts = {"/clean.sql", "/data.sql"},
    executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
public class AOP {
  @Autowired private Prueba prueba;
  @Autowired private UserAdminService service;

  @Test
  public void shouldLogParams() {
    Integer suma = prueba.sum(4, 5);

    assertTrue(suma.equals(9));
  }

  @Test
  public void testCreateUser() {
    CreateUser dto =
        CreateUser.builder()
            .email("email@gmail.com")
            .name("Tonitin")
            .password("Abcd1234#")
            .role(RoleEnum.USER)
            .build();

    User user = service.createUserByAdmin(dto);

    assertTrue(user != null);
  }
}
