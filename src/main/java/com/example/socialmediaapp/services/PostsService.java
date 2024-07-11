package com.example.socialmediaapp.services;

import org.springframework.beans.factory.annotation.Autowired;
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
    public Post addPost(Long userId, String caption) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Post post = new Post();
        post.setUser(user);
        post.setCaption(caption);
        return postRepository.save(post);
    }
}
