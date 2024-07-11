package com.example.socialmediaapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.socialmediaapp.models.Comment;
import com.example.socialmediaapp.models.Post;
import com.example.socialmediaapp.models.User;
import com.example.socialmediaapp.repositories.CommentRepository;
import com.example.socialmediaapp.repositories.PostRepository;
import com.example.socialmediaapp.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Comment addComment(Long userId, Long postId, String text) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPost(post);
        comment.setText(text);

        return commentRepository.save(comment);
    }
}
