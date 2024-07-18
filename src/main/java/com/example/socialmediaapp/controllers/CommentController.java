package com.example.socialmediaapp.controllers;

import java.util.List;

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

    @GetMapping("/post/{id}")
    public ResponseEntity<List<Comment>> getCommentsForPosts(@RequestParam Long postId) {
        List<Comment> commentsByPost = commentService.getCommentsForPosts(postId);
        return ResponseEntity.ok(commentsByPost);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Comment>> getCommentsForUser() {
        List<Comment> commentsByUser = commentService.getCommentsForUser();
        return ResponseEntity.ok(commentsByUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@RequestParam Long commentId) {
        Comment comment = commentService.getCommentById(commentId);
        return ResponseEntity.ok(comment);
    }

    @PostMapping("/add")
    public ResponseEntity<Comment> addComment(@RequestBody Comment comment, @RequestBody Long postId) {
        Comment createdComment = commentService.addComment(comment, postId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comment> editComment(@RequestParam Long commentId, @RequestBody Comment correctedComment) {
        try {
            Comment comment = commentService.editComment(commentId, correctedComment);
            return ResponseEntity.ok(comment);
        } catch (Exception e) {
            return ResponseEntity.status(403).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@RequestParam Long commentId) {
        try {
            commentService.deleteComment(commentId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }



}
