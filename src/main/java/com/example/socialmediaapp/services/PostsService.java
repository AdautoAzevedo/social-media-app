package com.example.socialmediaapp.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.socialmediaapp.dtos.PostRecordDTO;
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

    @Autowired
    private AuxMethods auxMethods;

    @Transactional
    public PostRecordDTO addPost(Post post) {
        User currentUser = auxMethods.getAuthenticatedUser();
        post.setUser(currentUser);
        Post createdPost = postRepository.save(post);
        return auxMethods.convertToPostRecordDTO(createdPost);
    }

    public List<PostRecordDTO> getPostsForCurrentUser() {
        User user = auxMethods.getAuthenticatedUser();
        List<Post> posts = postRepository.findByUser(user);
        return posts.stream()
                .map(auxMethods::convertToPostRecordDTO)
                .collect(Collectors.toList());
    }

    public PostRecordDTO getPostById(Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        return auxMethods.convertToPostRecordDTO(post.get());
    }

    public PostRecordDTO editPost(Long postId, Post correctedPost) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new RuntimeException("Post not found"));

        User currentUser = auxMethods.getAuthenticatedUser();
        if (!post.getUser().equals(currentUser)) {
            throw new RuntimeException("Unauthorized");
        }
        
        post.setCaption(correctedPost.getCaption());
        Post updatedPost = postRepository.save(post);
        return auxMethods.convertToPostRecordDTO(updatedPost);
    }

    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new RuntimeException("Post not found"));

        User currentUser = auxMethods.getAuthenticatedUser();
        if (!post.getUser().equals(currentUser)) {
            throw new RuntimeException("Unauthorized");
        }

        postRepository.delete(post);
    }

    
}
