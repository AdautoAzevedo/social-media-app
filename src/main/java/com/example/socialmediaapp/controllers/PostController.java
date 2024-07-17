package com.example.socialmediaapp.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.socialmediaapp.models.Post;
import com.example.socialmediaapp.services.PostsService;

@RestController
@RequestMapping("/posts")
public class PostController {
    
    private PostsService postsService;
    
    @Autowired
    public PostController(PostsService postsService) {
        this.postsService = postsService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Post>> getPostsForCurrentUser() {
        List<Post> posts = postsService.getPostsForCurrentUser();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@RequestParam Long postId) {
        Optional<Post> post = postsService.getPostById(postId);
        return post.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<Post> addPost(@RequestBody Post post) {
        Post createdPost = postsService.addPost(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> editPost(@RequestParam Long postId, @RequestBody Post correctedPost) {
        try {
            Post updatedPost = postsService.editPost(postId, correctedPost);
            return ResponseEntity.ok(updatedPost);
        } catch (Exception e) {
           return ResponseEntity.status(403).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@RequestParam Long postId) {
        try {
            postsService.deletePost(postId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}
