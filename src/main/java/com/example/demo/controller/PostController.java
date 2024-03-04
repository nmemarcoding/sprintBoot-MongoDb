package com.example.demo.controller;


import java.util.Optional;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Post;
import com.example.demo.model.User;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.config.ErrorResponse;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostController(PostRepository postRepository, UserRepository UserRepository) {
        this.postRepository = postRepository;
        this.userRepository = UserRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createPost(@RequestBody Post post) {
        Optional<User> author = userRepository.findById(post.getAuthorId());
        if (!author.isPresent()) {
            // Return a custom error response object
            ErrorResponse errorResponse = new ErrorResponse("User not found");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        
        try {
            postRepository.save(post);
            // Return a standard success response
            return ResponseEntity.ok(post);
        } catch (Exception e) {
            // Handle persistence errors
            ErrorResponse errorResponse = new ErrorResponse("Error saving post: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }





    @GetMapping("/search")
    public ResponseEntity<?> searchPost(@RequestParam String search) {
        return ResponseEntity.ok(postRepository.findByTitleOrContent(search));
    }

    @PostMapping("/all")
    public ResponseEntity<?> getAllPosts() {
        return ResponseEntity.ok(postRepository.findAll());
    }

    @PostMapping("/user")

    public ResponseEntity<?> getPostsByUser(@RequestBody String authorId) {
        return ResponseEntity.ok(postRepository.findByAuthorId(authorId));
    }
}
