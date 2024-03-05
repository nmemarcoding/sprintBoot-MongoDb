package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.demo.model.Post;

public interface PostRepository extends MongoRepository<Post, String>{
    
    @Query("{'authorId' : ?0}")
    Optional<Post> findByAuthorId(String authorId);

    // get all the posts
    @Query("{}")
    Optional<Post> findAllPosts();

    // find buy user serach on title or content
    @Query("{'$or':[ {'title': {$regex: ?0, $options: 'i'}}, {'content': {$regex: ?0, $options: 'i'}}]}")
    List<Post> findByTitleOrContent(String search);


    
  
}
