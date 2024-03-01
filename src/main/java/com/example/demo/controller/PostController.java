package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Post;
import com.example.demo.model.User;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;

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
        // Find the user by ID using the UserRepository
        return userRepository.findById(post.getAuthorId())
                .map(user -> {
                    // If user is found, save the post
                    Post savedPost = postRepository.save(post);
                    return ResponseEntity.ok(savedPost);
                })
                .orElseGet(() -> ResponseEntity.notFound().build()); // If user not found, return 404 Not Found
    }
    @PostMapping("/search")
    public ResponseEntity<?> searchPost(@RequestBody String search) {
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
