package com.example.demo.controller;

import java.util.Optional;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.config.ErrorResponse;
import com.example.demo.model.Comment;
import com.example.demo.model.Post;
import com.example.demo.model.User;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;

@Controller
@RequestMapping("/comments")
public class CommentController {

    public final CommentRepository commentRepository;
    public final PostRepository postRepository;
    public final UserRepository userRepository;
    



    public CommentController(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // create a comment
    @PostMapping("/create")
    public ResponseEntity<?> createComment(@RequestBody Comment comment) {
        
        if(comment.getPostId() == null || comment.getAuthorId() == null || comment.getText() == null) {
            ErrorResponse errorResponse = new ErrorResponse("Post id, author id and text are required");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        Optional<Post> post = postRepository.findById(comment.getPostId());
        if (!post.isPresent()) {
            ErrorResponse errorResponse = new ErrorResponse("Post not found");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        Optional<User> user = userRepository.findById(comment.getAuthorId());
        if (!user.isPresent()) {
            ErrorResponse errorResponse = new ErrorResponse("User not found");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        comment.setAuthorFirstName(user.get().getFirstName());
        comment.setAuthorLastName(user.get().getLastName());

        try {
            Comment savedComment = commentRepository.save(comment);
            return ResponseEntity.ok(savedComment);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    
}
