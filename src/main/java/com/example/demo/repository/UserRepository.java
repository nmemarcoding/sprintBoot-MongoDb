package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.demo.model.User;

public interface UserRepository extends MongoRepository<User, String>{
    @Query("{'email' : ?0}")
    Optional<User> findByEmail(String email);

    // get all the users
    @Query("{}")
    Optional<User> findAllUsers();

    
}
