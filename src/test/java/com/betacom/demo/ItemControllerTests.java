package com.betacom.demo;

import com.betacom.demo.dto.item.ItemRequest;
import com.betacom.demo.model.security.User;
import com.betacom.demo.repository.ItemRepository;
import com.betacom.demo.repository.UserRepository;
import com.betacom.demo.service.security.JwtService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ItemControllerTests {
    @LocalServerPort
    private Integer port;
    private String baseUrl;
    private String jwtToken;
    private User testUser;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;
        testUser = User.builder()
                .login("testUser")
                .password("password123")
                .build();
        userRepository.save(testUser);
        jwtToken = jwtService.generateToken(testUser);
    }

    @Test
    void shouldReturnAllUserItems() {
        RestAssured.given()
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(ContentType.JSON)
                .when()
                .get(baseUrl + "/items")
                .then()
                .statusCode(200)
                .body("$", hasSize(greaterThanOrEqualTo(0)));
    }

    @Test
    void shouldReturn403WhenNoToken() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get(baseUrl + "/items")
                .then()
                .statusCode(403);
    }

    @Test
    void shouldReturn403WhenCreatingItemWithoutToken() {
        ItemRequest newItem = ItemRequest.builder()
                .name("Test Item")
                .owner(testUser.getId())
                .build();

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(newItem)
                .when()
                .post(baseUrl + "/items")
                .then()
                .statusCode(403);
    }

    @Test
    void shouldReturnEmptyListForNewUser() {
        RestAssured.given()
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(ContentType.JSON)
                .when()
                .get(baseUrl + "/items")
                .then()
                .statusCode(200)
                .body("$", hasSize(0));
    }

    @AfterEach
    void cleanup() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }
}
