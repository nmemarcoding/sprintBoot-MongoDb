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
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;

@Controller
@RequestMapping("/comments")
public class CommentController {

    public final CommentRepository commentRepository;
    public final PostRepository postRepository;



    public CommentController(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    // create a comment
    @PostMapping("/create")
    public ResponseEntity<?> createComment(@RequestBody Comment comment) {
        
        Optional<Post> post = postRepository.findById(comment.getPostId());
        if (!post.isPresent()) {
            ErrorResponse errorResponse = new ErrorResponse("Post not found");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        try {
            Comment savedComment = commentRepository.save(comment);
            return ResponseEntity.ok(savedComment);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    
}
