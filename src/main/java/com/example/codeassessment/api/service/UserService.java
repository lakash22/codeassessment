package com.example.codeassessment.api.service;

import com.example.codeassessment.api.entity.User;
import com.example.codeassessment.api.model.UserLoginRequest;
import com.example.codeassessment.api.model.UserRegistrationRequest;
import com.example.codeassessment.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

  @Autowired
  UserRepository userRepository;

  /**
   * Create user based on userRequest
   *
   * @param userRequest
   * @return newly created User
   */
  public User createUser(UserRegistrationRequest userRequest) {
    User user = new User();
    user.setEmail(userRequest.getEmail());
    user.setFirstName(userRequest.getFirstName());
    user.setLastName(userRequest.getLastName());
    user.setPassword(userRequest.getPassword());
    return userRepository.save(user);
  }

  /**
   * Check if given email exists in repository
   *
   * @param email
   * @return an {@code Optional} with the User value present
   * otherwise an empty {@code Optional}
   */

  public Optional<String> checkForExistingEmail(String email) {
    User existingUser = userRepository.findByEmail(email);
    if (Optional.ofNullable(existingUser).isPresent()) {
      return Optional.of("User already exists. Please login");
    }
    return Optional.empty();
  }

  /**
   * Method looks up user by email and password
   *
   * @param request
   * @return an {@code Optional} with the User value present
   */
  public Optional<User> getUserByEmailAndPassword(UserLoginRequest request) {
    User user = userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword());
    if (Optional.ofNullable(user).isPresent()) {
      return Optional.of(user);
    }
    return Optional.empty();
  }

  /**
   * Look up user by id
   *
   * @param id
   * @return an {@code Optional} with the User value present
   */
  public Optional<User> getUserById(String id) {
    return userRepository.findById(id);
  }
}
