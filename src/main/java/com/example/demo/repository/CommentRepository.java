package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.demo.model.Comment;

public interface CommentRepository extends MongoRepository<Comment, String>{
    
    // find by post id
    @Query("{'postId' : ?0}")
    Optional<Comment> findByPostId(String postId);
}
