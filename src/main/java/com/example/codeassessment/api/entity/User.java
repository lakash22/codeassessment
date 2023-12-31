package com.example.codeassessment.api.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

@DynamoDBTable(tableName = "User")
public class User {

  @DynamoDBHashKey
  @DynamoDBAutoGeneratedKey
  private String id;

  @DynamoDBAttribute(attributeName = "firstName")
  private String firstName;

  @DynamoDBAttribute(attributeName = "lastName")
  private String lastName;

  @DynamoDBAttribute(attributeName = "email")
  private String email;

  @DynamoDBAttribute(attributeName = "password")
  private String password;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
}
