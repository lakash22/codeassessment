package com.example.codeassessment.integration.tests;

import com.example.codeassessment.api.entity.User;
import com.example.codeassessment.api.model.UserLoginRequest;
import com.example.codeassessment.api.model.UserResponse;
import com.example.codeassessment.api.repository.UserRepository;
import com.example.codeassessment.integration.utils.UserLoginHelper;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TestGetUser {

  private static RestTemplate restTemplate;
  private static UserLoginHelper userLoginHelper;
  private final String URL = "http://localhost:8080/codeassessment/users";
  private final String RESOURCE = "/login";
  private final String FAILURE_MESSAGE = "Expected error was not thrown";

  @Autowired
  UserRepository userRepository;

  @BeforeClass
  public static void setUpBeforeClass() {
    userLoginHelper = new UserLoginHelper();
    restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
  }

  @AfterClass
  public static void tearDownAfterClass() {
    userLoginHelper = null;
    restTemplate = null;
  }

  @Test
  public void testGetUserDetailsForValidToken() {
    UserLoginRequest loginRequest = userLoginHelper.createUserLoginRequest("efg@gmail.com", "test908");
    HttpEntity requestBody = new HttpEntity(loginRequest);
    ResponseEntity<String> loginResponse = restTemplate.exchange(URL + RESOURCE, HttpMethod.POST, requestBody, String.class);
    String token = loginResponse.getBody();
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("token", token);
    HttpEntity request = new HttpEntity(httpHeaders);
    ResponseEntity<UserResponse> validTokenResponse = restTemplate.exchange(URL, HttpMethod.GET, request, UserResponse.class);
    assertEquals(HttpStatus.OK, validTokenResponse.getStatusCode());
    Optional<User> userFromDB = userRepository.findById(token);
    UserResponse userFromService = validTokenResponse.getBody();
    assertEquals(userFromDB.get().getFirstName(), userFromService.getFirstName());
    assertEquals(userFromDB.get().getLastName(), userFromService.getLastName());
    assertEquals(userFromDB.get().getEmail(), userFromService.getEmail());
  }

  @Test
  public void testInvalidToken() {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("token", "token");
    HttpEntity request = new HttpEntity(httpHeaders);
    try {
      restTemplate.exchange(URL, HttpMethod.GET, request, UserResponse.class);
      fail(FAILURE_MESSAGE);
    } catch (HttpClientErrorException e) {
      assertEquals(HttpStatus.UNAUTHORIZED, e.getStatusCode());
    }
  }
}
