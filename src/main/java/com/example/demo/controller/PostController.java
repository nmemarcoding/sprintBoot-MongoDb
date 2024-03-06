package com.example.demo.controller;


import java.util.Optional;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Post;
import com.example.demo.model.User;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.config.ErrorResponse;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public PostController(PostRepository postRepository, UserRepository UserRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.userRepository = UserRepository;
        this.commentRepository = commentRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createPost(@RequestBody Post post) {
        Optional<User> author = userRepository.findById(post.getAuthorId());
        if (!author.isPresent()) {
            // Return a custom error response object
            ErrorResponse errorResponse = new ErrorResponse("User not found");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        
        post.setAuthorFirstName(author.get().getFirstName());   
        post.setAuthorLastName(author.get().getLastName());
        


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

    @GetMapping("/all")
    public ResponseEntity<?> getAllPosts() {
        return ResponseEntity.ok(postRepository.findAll());
    }

    @PostMapping("/user")
    public ResponseEntity<?> getPostsByUser(@RequestBody String authorId) {
        return ResponseEntity.ok(postRepository.findByAuthorId(authorId));
    }

    // modify post by id
    @PostMapping("/modify")
    public ResponseEntity<?> modifyPost(@RequestBody Post modifiedPost) {
        if (modifiedPost.getId() == null) {
            ErrorResponse errorResponse = new ErrorResponse("The given id must not be null");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        try {
            Optional<Post> postOptional = postRepository.findById(modifiedPost.getId());
            if (!postOptional.isPresent()) {
                ErrorResponse errorResponse = new ErrorResponse("Post not found");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            Post existingPost = postOptional.get();
            // Update fields as necessary. Avoid overwriting non-specified fields.
            if (modifiedPost.getTitle() != null) existingPost.setTitle(modifiedPost.getTitle());
            if (modifiedPost.getContent() != null) existingPost.setContent(modifiedPost.getContent());
            if (modifiedPost.getAuthorId() != null) existingPost.setAuthorId(modifiedPost.getAuthorId()); 
            // Since authorFirstName and authorLastName are likely not to be changed in a modify operation,
            // they are not updated here. If you need to update them, include similar checks and updates.
            // Do not update createdDate as it should remain the date when the post was initially created.
            // lastModifiedDate is automatically updated by Spring Data's auditing feature if it's configured correctly.

            // Save the updated entity
            Post updatedPost = postRepository.save(existingPost);
            return ResponseEntity.ok(updatedPost);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Error modifying post: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // delete post by id
    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deletePost(@PathVariable String id) {

        if (id == null) {
            ErrorResponse errorResponse = new ErrorResponse("The given id must not be null");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        try {
            Optional<Post> postOptional = postRepository.findById(id);
            if (!postOptional.isPresent()) {
                ErrorResponse errorResponse = new ErrorResponse("Post not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
    
            postRepository.deleteById(id);
            // delete all comments for the post
            commentRepository.deleteByPostId(postOptional.get().getId());
            return ResponseEntity.ok().body(new ErrorResponse("Post successfully deleted"));
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Error deleting post: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    

    
}
