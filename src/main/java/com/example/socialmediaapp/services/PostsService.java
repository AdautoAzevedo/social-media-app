package com.example.socialmediaapp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.socialmediaapp.models.Post;
import com.example.socialmediaapp.models.User;
import com.example.socialmediaapp.repositories.PostRepository;
import com.example.socialmediaapp.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class PostsService {
    
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Post addPost(Post post) {
        User currentUser = getAuthenticatedUser();
        post.setUser(currentUser);
        return postRepository.save(post);
    }

    public List<Post> getPostsForCurrentUser() {
        User user = getAuthenticatedUser();
        return postRepository.findByUser(user);
    }

    public Optional<Post> getPostById(Long postId) {
        return postRepository.findById(postId);
    }

    public Post editPost(Long postId, Post correctedPost) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new RuntimeException("Post not found"));
        
        post.setCaption(correctedPost.getCaption());
        return postRepository.save(post);
    }

    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new RuntimeException("Post not found"));

        User currentUser = getAuthenticatedUser();
        if (!post.getUser().equals(currentUser)) {
            throw new RuntimeException("Unauthorized");
        }

        postRepository.delete(post);
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
