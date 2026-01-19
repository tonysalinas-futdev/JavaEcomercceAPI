package com.example.Ecomercce.integrationTests.users;

import static org.hamcrest.Matchers.equalTo;

import com.example.Ecomercce.auth.dtos.AuthResponse;
import com.example.Ecomercce.integrationTests.globalConftest.GlobalConftest;
import com.example.Ecomercce.users.dtos.UpdateUserProfile;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class testUserControllers {
  @Autowired private GlobalConftest globalConftest;

  @Test
  @Sql("/data.sql")
  public void testGetProfile() {
    AuthResponse authResponse = globalConftest.obtainUserCredentials();

    RestAssured.given()
        .contentType("application/json")
        .header("Authorization", "Bearer " + authResponse.getAccessToken())
        .when()
        .get("http://localhost:8000/api/v1/users/profile")
        .then()
        .body("email", equalTo("user@gmail.com"))
        .body("name", equalTo("user"))
        .log()
        .all()
        .statusCode(200);
  }

  @Test
  @Sql("/data.sql")
  public void testUpdateProfile() {
    AuthResponse authResponse = globalConftest.obtainUserCredentials();

    RestAssured.given()
        .contentType("application/json")
        .header("Authorization", "Bearer " + authResponse.getAccessToken())
        .body(new UpdateUserProfile("Jose Alejandro", "newuser@gmail.com"))
        .when()
        .put("http://localhost:8000/api/v1/users/profile")
        .then()
        .body("email", equalTo("newuser@gmail.com"))
        .body("name", equalTo("Jose Alejandro"))
        .log()
        .all()
        .statusCode(200);
  }

  @Test
  @Sql("/data.sql")
  public void testUpdateProfileFail() {
    AuthResponse authResponse = globalConftest.obtainUserCredentials();
    AuthResponse adminResponse = globalConftest.obtainAdminCredentials();
    RestAssured.given()
        .contentType("application/json")
        .header("Authorization", "Bearer " + authResponse.getAccessToken())
        .body(new UpdateUserProfile("Jose Alejandro", "admin@gmail.com"))
        .when()
        .put("http://localhost:8000/api/v1/users/profile")
        .then()
        .body("message", equalTo("Ya existe un usuario con ese email"))
        .log()
        .all()
        .statusCode(409);
  }
}
