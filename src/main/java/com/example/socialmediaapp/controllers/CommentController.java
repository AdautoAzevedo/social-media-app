package com.example.socialmediaapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.socialmediaapp.models.Comment;
import com.example.socialmediaapp.services.CommentService;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }


    @PostMapping("/add")
    public ResponseEntity addComment(@RequestBody Comment comment, @RequestBody Long postId) {
        return ResponseEntity.ok(commentService.addComment(comment, postId));
    }
}
