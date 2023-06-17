package com.example.codeassessment.integration.tests;

import com.example.codeassessment.api.entity.User;
import com.example.codeassessment.api.model.UserLoginRequest;
import com.example.codeassessment.api.model.UserResponse;
import com.example.codeassessment.api.repository.UserRepository;
import com.example.codeassessment.integration.utils.UserLoginHelper;
import org.junit.*;
import org.junit.runner.RunWith;
import org.omg.CORBA.StringHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TestUserLogin {

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
  public void testValidUserLogin() {
    UserLoginRequest loginRequest = userLoginHelper.createUserLoginRequest("efg@gmail.com", "test908");
    HttpEntity requestBody = new HttpEntity(loginRequest);
    ResponseEntity<String> response = restTemplate.exchange(URL + RESOURCE, HttpMethod.POST, requestBody, String.class);
    assertEquals(userRepository.findByEmailAndPassword("efg@gmail.com", "test908").getId(), response.getBody());
  }

  @Test
  public void testInvalidEmail() {
    UserLoginRequest loginRequest = userLoginHelper.createUserLoginRequest("efg22@gmail.com", "test908");
    HttpEntity requestBody = new HttpEntity(loginRequest);
    try {
      restTemplate.exchange(URL + RESOURCE, HttpMethod.POST, requestBody, String.class);
      fail(FAILURE_MESSAGE);
    } catch (HttpClientErrorException e) {
      assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
      assertEquals("Email address or password is invalid", e.getResponseBodyAsString());
    }
  }

  @Test
  public void testInvalidPassword() {
    UserLoginRequest loginRequest = userLoginHelper.createUserLoginRequest("efg@gmail.com", "test9089");
    HttpEntity requestBody = new HttpEntity(loginRequest);
    try {
      restTemplate.exchange(URL + RESOURCE, HttpMethod.POST, requestBody, String.class);
      fail(FAILURE_MESSAGE);
    } catch (HttpClientErrorException e) {
      assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
      assertEquals("Email address or password is invalid", e.getResponseBodyAsString());
    }
  }

  @Test
  public void testMissingEmail() {
    UserLoginRequest loginRequest = userLoginHelper.createUserLoginRequest("", "test9089");
    HttpEntity requestBody = new HttpEntity(loginRequest);
    try {
      restTemplate.exchange(URL + RESOURCE, HttpMethod.POST, requestBody, String.class);
      fail(FAILURE_MESSAGE);
    } catch (HttpClientErrorException e) {
      assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
    }
  }

  @Test
  public void testMissingPassword() {
    UserLoginRequest loginRequest = userLoginHelper.createUserLoginRequest("efg@gmail.com", "");
    HttpEntity requestBody = new HttpEntity(loginRequest);
    try {
      restTemplate.exchange(URL + RESOURCE, HttpMethod.POST, requestBody, String.class);
      fail(FAILURE_MESSAGE);
    } catch (HttpClientErrorException e) {
      assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
    }
  }

}
