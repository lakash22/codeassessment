package com.example.codeassessment.integration.tests;

import com.example.codeassessment.api.model.UserRegistrationRequest;
import com.example.codeassessment.api.repository.UserRepository;
import com.example.codeassessment.integration.utils.UserRegistrationHelper;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TestUserRegistration {

  private static RestTemplate restTemplate;
  private static UserRegistrationHelper userRegistrationHelper;
  private final String URL = "http://localhost:8080/codeassessment/users";
  private final String RESOURCE = "/register";
  private final String FIRST_NAME = "First";
  private final String LAST_NAME = "Last";
  private final String PASSWORD = "Tester123";
  private final String FAILURE_MESSAGE = "Expected error was not thrown";

  @Autowired
  UserRepository userRepository;

  @BeforeClass
  public static void setUpBeforeClass() {
    userRegistrationHelper = new UserRegistrationHelper();
    restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
  }

  @Test
  public void testNewUserRegistrationSuccess() {
    String emailAddress = userRegistrationHelper.generateRandomString(8) + "@gmail.com";
    UserRegistrationRequest registrationRequest = userRegistrationHelper.createUserRegistrationRequest(emailAddress,
        FIRST_NAME, LAST_NAME, PASSWORD);
    HttpEntity requestBody = new HttpEntity(registrationRequest);
    ResponseEntity<String> response = restTemplate.exchange(URL + RESOURCE, HttpMethod.POST, requestBody, String.class);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(userRepository.findByEmail(emailAddress));
  }

  @Test
  public void testExistingEmailAddress() {
    UserRegistrationRequest registrationRequest = userRegistrationHelper.createUserRegistrationRequest("efg@gmail.com",
        FIRST_NAME, LAST_NAME, PASSWORD);

    HttpEntity requestBody = new HttpEntity(registrationRequest);
    try {
      restTemplate.exchange(URL + RESOURCE, HttpMethod.POST, requestBody, String.class);
      fail(FAILURE_MESSAGE);
    } catch (HttpClientErrorException e) {
      assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
      assertEquals("User already exists. Please login", e.getResponseBodyAsString());
    }
  }

  @Test
  public void testMissingEmailAddress() {
    UserRegistrationRequest registrationRequest = userRegistrationHelper.createUserRegistrationRequest("",
        FIRST_NAME, LAST_NAME, PASSWORD);
    HttpEntity requestBody = new HttpEntity(registrationRequest);
    try {
      restTemplate.exchange(URL + RESOURCE, HttpMethod.POST, requestBody, String.class);
      fail(FAILURE_MESSAGE);
    } catch (HttpClientErrorException e) {
      assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
    }
  }

  @Test
  public void testMissingPassword() {
    UserRegistrationRequest registrationRequest = userRegistrationHelper.createUserRegistrationRequest("testUser@gmail.com",
        FIRST_NAME, LAST_NAME, "");
    HttpEntity requestBody = new HttpEntity(registrationRequest);
    try {
      restTemplate.exchange(URL + RESOURCE, HttpMethod.POST, requestBody, String.class);
      fail(FAILURE_MESSAGE);
    } catch (HttpClientErrorException e) {
      assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
    }
  }

  @Test
  @Ignore("Invalid Email that doesnt have @ and domain should give error")
  public void testBadEmailAddress() {
    UserRegistrationRequest registrationRequest = userRegistrationHelper.createUserRegistrationRequest("test",
        FIRST_NAME, LAST_NAME, "");
    HttpEntity requestBody = new HttpEntity(registrationRequest);
    try {
      restTemplate.exchange(URL + RESOURCE, HttpMethod.POST, requestBody, String.class);
      fail(FAILURE_MESSAGE);
    } catch (HttpClientErrorException e) {
      assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
    }
  }

  @Test
  @Ignore("Invalid password less than 8 characters")
  public void testBadPasswordLessCharacters() {
    UserRegistrationRequest registrationRequest = userRegistrationHelper.createUserRegistrationRequest("testweb@gmail.com",
        FIRST_NAME, LAST_NAME, "abc");
    HttpEntity requestBody = new HttpEntity(registrationRequest);
    try {
      restTemplate.exchange(URL + RESOURCE, HttpMethod.POST, requestBody, String.class);
      fail(FAILURE_MESSAGE);
    } catch (HttpClientErrorException e) {
      assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
    }
  }

  @Test
  @Ignore("Invalid password more than 20 characters")
  public void testBadPasswordMoreCharacters() {
    String password = userRegistrationHelper.generateRandomString(22);
    UserRegistrationRequest registrationRequest = userRegistrationHelper.createUserRegistrationRequest("testweb@gmail.com",
        FIRST_NAME, LAST_NAME, password);
    HttpEntity requestBody = new HttpEntity(registrationRequest);
    try {
      restTemplate.exchange(URL + RESOURCE, HttpMethod.POST, requestBody, String.class);
      fail(FAILURE_MESSAGE);
    } catch (HttpClientErrorException e) {
      assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
    }
  }

}
