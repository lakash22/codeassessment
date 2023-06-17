package com.example.codeassessment.api.repository;

import com.example.codeassessment.api.entity.User;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@EnableScan
public interface UserRepository extends CrudRepository<User, String> {

  User findByEmail(String email);

  User findByEmailAndPassword(String email, String password);
}
