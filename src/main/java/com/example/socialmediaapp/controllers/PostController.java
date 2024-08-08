package com.example.socialmediaapp.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.socialmediaapp.dtos.PostRecordDTO;
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
    public ResponseEntity<List<PostRecordDTO>> getPostsForCurrentUser() {
        System.out.println("GET method called");
        List<PostRecordDTO> posts = postsService.getPostsForCurrentUser();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostRecordDTO> getPostById(@PathVariable Long postId) {
        try {
            PostRecordDTO post = postsService.getPostById(postId);
            return ResponseEntity.ok(post);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<PostRecordDTO> addPost(@RequestBody Post post) {
        PostRecordDTO postRecordDTO = postsService.addPost(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(postRecordDTO);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostRecordDTO> editPost(@PathVariable Long postId, @RequestBody Post correctedPost) {
        try {
            PostRecordDTO updatedPost = postsService.editPost(postId, correctedPost);
            return ResponseEntity.ok(updatedPost);
        } catch (Exception e) {
           return ResponseEntity.status(403).body(null);
        }
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        try {
            postsService.deletePost(postId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<PostRecordDTO> likePost(@PathVariable Long postId) {
        PostRecordDTO post = postsService.likePost(postId);
        return ResponseEntity.ok(post);
    }

}
