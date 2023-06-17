package com.example.codeassessment.integration.utils;

import com.example.codeassessment.api.model.UserLoginRequest;

public class UserLoginHelper {

  /**
   * Creates UserLoginRequest based on provided email and password
   *
   * @param email
   * @param password
   * @return UserLoginRequest object
   */
  public UserLoginRequest createUserLoginRequest(String email, String password) {
    UserLoginRequest userLoginRequest = new UserLoginRequest();
    userLoginRequest.setEmail(email);
    userLoginRequest.setPassword(password);
    return userLoginRequest;
  }

}
