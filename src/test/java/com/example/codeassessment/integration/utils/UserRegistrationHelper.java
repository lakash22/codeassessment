package com.example.codeassessment.integration.utils;

import com.example.codeassessment.api.model.UserRegistrationRequest;
import org.apache.commons.lang3.RandomStringUtils;

public class UserRegistrationHelper {

  /**
   * Create UserRegistrationRequest based on input parameters
   * @param email
   * @param firstName
   * @param lastName
   * @param password
   * @return newly created UserRegistrationRequest object
   */
  public UserRegistrationRequest createUserRegistrationRequest(String email, String firstName, String lastName,
                                                               String password) {
    UserRegistrationRequest registrationRequest = new UserRegistrationRequest();
    registrationRequest.setEmail(email);
    registrationRequest.setFirstName(firstName);
    registrationRequest.setLastName(lastName);
    registrationRequest.setPassword(password);
    return registrationRequest;
  }

  /**
   * Random string generator
   * @param length
   * @return randomly generated string
   */
  public String generateRandomString(int length) {
    String generatedString = RandomStringUtils.randomAlphabetic(length);
    return generatedString.toLowerCase();
  }


}
