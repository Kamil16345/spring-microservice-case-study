package com.betacom.demo;

import com.betacom.demo.controller.AuthenticationController;
import com.betacom.demo.dto.security.AuthenticationRequest;
import com.betacom.demo.dto.security.RegisterRequest;
import com.betacom.demo.repository.UserRepository;
import com.betacom.demo.service.security.JwtService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationControllerTests {

    @LocalServerPort
    private Integer port;

    private String baseUrl;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;
        userRepository.deleteAll();
    }

    @Test
    void shouldRegisterNewUser() {
        RegisterRequest request = RegisterRequest.builder()
                .login("testuser")
                .password("password123")
                .build();

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(baseUrl + "/register")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("User testuser created successfully! User's ID: " + userRepository.findUserByLogin("testuser").getId()));
    }


    @Test
    void shouldReturn409WhenUserAlreadyExists() {
        RegisterRequest request = RegisterRequest.builder()
                .login("testuser")
                .password("password123")
                .build();

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(baseUrl + "/register")
                .then()
                .statusCode(HttpStatus.OK.value());

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(baseUrl + "/register")
                .then()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    void shouldLoginUser() {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .login("testuser")
                .password("password123")
                .build();

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(registerRequest)
                .when()
                .post(baseUrl + "/register")
                .then()
                .statusCode(HttpStatus.OK.value());

        AuthenticationRequest loginRequest = AuthenticationRequest.builder()
                .login("testuser")
                .password("password123")
                .build();

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post(baseUrl + "/login")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("token", notNullValue());
    }

    @Test
    void shouldReturn404WhenLoginWithInvalidCredentials() {
        AuthenticationRequest loginRequest = AuthenticationRequest.builder()
                .login("nonExistingUser")
                .password("randomPassword")
                .build();

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post(baseUrl + "/login")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}