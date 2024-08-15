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

import com.example.socialmediaapp.dtos.CommentDTO;
import com.example.socialmediaapp.dtos.CommentResponseDTO;
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

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentResponseDTO>> getCommentsForPosts(@PathVariable Long postId) {
        List<CommentResponseDTO> commentsByPost = commentService.getCommentsForPosts(postId);
        return ResponseEntity.ok(commentsByPost);
    }

    @GetMapping("/user")
    public ResponseEntity<List<CommentResponseDTO>> getCommentsForUser() {
        List<CommentResponseDTO> commentsByUser = commentService.getCommentsForUser();
        return ResponseEntity.ok(commentsByUser);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponseDTO> getCommentById(@PathVariable Long commentId) {
        CommentResponseDTO commentResponse = commentService.getCommentById(commentId);
        return ResponseEntity.ok(commentResponse);
    }

    @PostMapping("/add")
    public ResponseEntity<CommentResponseDTO> addComment(@RequestBody CommentDTO commentDTO) {
        CommentResponseDTO createdComment = commentService.addComment(commentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDTO> editComment(@PathVariable Long commentId, @RequestBody Comment correctedComment) {
        try {
            CommentResponseDTO comment = commentService.editComment(commentId, correctedComment);
            return ResponseEntity.ok(comment);
        } catch (Exception e) {
            return ResponseEntity.status(403).body(null);
        }
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        try {
            commentService.deleteComment(commentId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }



}
