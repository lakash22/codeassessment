package com.example.codeassessment.api.controller;

import com.example.codeassessment.api.entity.User;
import com.example.codeassessment.api.model.*;
import com.example.codeassessment.api.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

  final private UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/register")
  public ResponseEntity<String> createUser(@Valid @RequestBody UserRegistrationRequest request) {
    Optional<String> existingUserMessage = userService.checkForExistingEmail(request.getEmail());
    if (existingUserMessage.isPresent()) {
      return ResponseEntity.badRequest().body(existingUserMessage.get());
    }
    User user = userService.createUser(request);
    if (Optional.ofNullable(user).isPresent()) {
      return ResponseEntity.status(HttpStatus.CREATED.value()).body("User successfully created");
    }
    return ResponseEntity.badRequest().body("Invalid request");
  }

  @PostMapping("/login")
  public ResponseEntity<String> userLogin(@Valid @RequestBody UserLoginRequest request) {
    Optional<User> user = userService.getUserByEmailAndPassword(request);
    if (user.isPresent()) {
      return ResponseEntity.ok(user.get().getId());
    }
    return ResponseEntity.badRequest().body("Email address or password is invalid");
  }

  @GetMapping
  public ResponseEntity<?> getUserDetails(@RequestHeader(value = "token") String token) {
    Optional<User> user = userService.getUserById(token);
    if (user.isPresent()) {
      UserResponse userResponse = new UserResponse();
      userResponse.setEmail(user.get().getEmail());
      userResponse.setFirstName(user.get().getFirstName());
      userResponse.setLastName(user.get().getLastName());
      return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body("UNAUTHORIZED");
  }
}
