package com.example.socialmediaapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.socialmediaapp.services.CommentService;

@RestController
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/add")
    public ResponseEntity addComment(@RequestParam Long userId, @RequestParam Long postId, @RequestParam String text) {
        return ResponseEntity.ok(commentService.addComment(userId, postId, text));
    }
}
