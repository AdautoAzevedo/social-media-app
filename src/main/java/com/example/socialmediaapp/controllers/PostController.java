package com.example.socialmediaapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.socialmediaapp.models.Post;
import com.example.socialmediaapp.services.PostsService;

@RestController
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private PostsService postsService;

    @PostMapping("/add")
    public ResponseEntity<Post> addPost(@RequestParam Long userId, @RequestParam String caption) {
        return ResponseEntity.ok(postsService.addPost(userId, caption));
    }

}
