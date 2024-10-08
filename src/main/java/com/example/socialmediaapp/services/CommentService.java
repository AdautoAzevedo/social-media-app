package com.example.socialmediaapp.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.socialmediaapp.dtos.CommentDTO;
import com.example.socialmediaapp.dtos.CommentResponseDTO;
import com.example.socialmediaapp.exceptions.CommentNotFoundException;
import com.example.socialmediaapp.exceptions.PostNotFoundException;
import com.example.socialmediaapp.models.Comment;
import com.example.socialmediaapp.models.Post;
import com.example.socialmediaapp.models.User;
import com.example.socialmediaapp.repositories.CommentRepository;
import com.example.socialmediaapp.repositories.PostRepository;

import jakarta.transaction.Transactional;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AuxMethods auxMethods;

    @Transactional
    public CommentResponseDTO addComment(CommentDTO commentDTO ) {
        User currentUser = auxMethods.getAuthenticatedUser();
        Post currentPost = postRepository.findById(commentDTO.postId())
            .orElseThrow(() -> new PostNotFoundException("Post not found"));
        
        Comment comment = new Comment();
        comment.setPost(currentPost);
        comment.setUser(currentUser);
        comment.setText(commentDTO.text());
        Comment createdComment = commentRepository.save(comment);
        return auxMethods.convertToCommentResponseDTO(createdComment);
    }

    public CommentResponseDTO getCommentById(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new CommentNotFoundException("Comment not found"));

        return auxMethods.convertToCommentResponseDTO(comment);
    }

    public List<CommentResponseDTO> getCommentsForPosts(Long postId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new PostNotFoundException("Post not found"));
        
        List<Comment> comments = commentRepository.findByPost(post);
        return comments.stream()
                .map(auxMethods::convertToCommentResponseDTO)
                .collect(Collectors.toList());
    }

    public List<CommentResponseDTO> getCommentsForUser() {
        User user = auxMethods.getAuthenticatedUser();
        List<Comment> comments = commentRepository.findByUser(user);
        return comments.stream()
                .map(auxMethods::convertToCommentResponseDTO)
                .collect(Collectors.toList());
    }

    public CommentResponseDTO editComment(Long commentId ,Comment correctedComment) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new CommentNotFoundException("Comment not found"));

        User currentUser = auxMethods.getAuthenticatedUser();
        if (!comment.getUser().equals(currentUser)) {
            throw new RuntimeException("Unauthorized");
        }

        comment.setText(correctedComment.getText());
        return auxMethods.convertToCommentResponseDTO(commentRepository.save(comment));
    }

    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new CommentNotFoundException("Comment not found"));

        User currentUser = auxMethods.getAuthenticatedUser();
        if (!comment.getUser().equals(currentUser)) {
            throw new RuntimeException("Unauthorized");
        }

        commentRepository.delete(comment);
    }


}