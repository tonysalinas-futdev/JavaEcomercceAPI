package com.example.Ecomercce.integrationTests.users;

import static org.hamcrest.Matchers.equalTo;

import com.example.Ecomercce.integrationTests.globalconftest.GlobalConftest;
import com.example.Ecomercce.users.dtos.CreateUser;
import com.example.Ecomercce.users.dtos.UpdateUserProfile;
import com.example.Ecomercce.users.enums.RoleEnum;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@Sql(
    scripts = {"/clean.sql", "/data.sql"},
    executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class testUserControllers {
  @Autowired private GlobalConftest globalConftest;

  @Test
  public void mustReturnUserProfileAndStatus200() {
    globalConftest.createUser();
    var userCredentials = globalConftest.obtainUserCredentials();
    RestAssured.given()
        .contentType("application/json")
        .header("Authorization", "Bearer " + userCredentials.getAccessToken())
        .when()
        .get("http://localhost:8000/api/v1/me/profile")
        .then()
        .body("email", equalTo("user@gmail.com"))
        .body("name", equalTo("user"))
        .log()
        .all()
        .statusCode(200);
  }

  @Test
  public void mustUpdateUserProfileAndReturn200() {
    globalConftest.createUser();
    var userCredentials = globalConftest.obtainUserCredentials();

    RestAssured.given()
        .contentType("application/json")
        .header("Authorization", "Bearer " + userCredentials.getAccessToken())
        .body(new UpdateUserProfile("Jose Alejandro", "newuser@gmail.com"))
        .when()
        .put("http://localhost:8000/api/v1/me/profile")
        .then()
        .body("email", equalTo("newuser@gmail.com"))
        .body("name", equalTo("Jose Alejandro"))
        .log()
        .all()
        .statusCode(200);
  }

  @Test
  public void shouldFailToUpdateProfileWithExistingEmailAndReturn409() {
    globalConftest.createUser();
    var userCredentials = globalConftest.obtainUserCredentials();
    globalConftest.createAdmin();
    RestAssured.defaultParser = Parser.JSON;

    RestAssured.given()
        .contentType("application/json")
        .header("Authorization", "Bearer " + userCredentials.getAccessToken())
        .body(new UpdateUserProfile("Jose Alejandro", "admin@gmail.com"))
        .when()
        .put("http://localhost:8000/api/v1/me/profile")
        .then()
        .body("message", equalTo("Email already exists"))
        .log()
        .all()
        .statusCode(409);
  }

  @Test
  public void shouldCreateUserByAdmin() {
    globalConftest.createAdmin();
    var adminCredentials = globalConftest.obtainAdminCredentials();
    CreateUser userData =
        CreateUser.builder()
            .name("User Created")
            .email("example@gmail.com")
            .role(RoleEnum.ADMIN)
            .password("Abcd1234#")
            .build();

    RestAssured.given()
        .contentType("application/json")
        .header("Authorization", "Bearer " + adminCredentials.getAccessToken())
        .body(userData)
        .when()
        .post("http://localhost:8000/api/v1/admin")
        .then()
        .body("name", equalTo("User Created"))
        .log()
        .all()
        .statusCode(201);
  }
}
