package com.example.socialmediaapp.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.socialmediaapp.dtos.CommentResponseDTO;
import com.example.socialmediaapp.dtos.PostRecordDTO;
import com.example.socialmediaapp.dtos.UserDTO;
import com.example.socialmediaapp.models.Comment;
import com.example.socialmediaapp.models.Like;
import com.example.socialmediaapp.models.Post;
import com.example.socialmediaapp.models.User;
import com.example.socialmediaapp.repositories.UserRepository;

@Service
public class AuxMethods {
    @Autowired
    private UserRepository userRepository;

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserDTO convertToUserDTO(User user) {
        UserDTO userDTO = new UserDTO(user.getId(), user.getName());
        return userDTO;
    }

    public CommentResponseDTO convertToCommentResponseDTO(Comment comment) {
        CommentResponseDTO commentResponseDTO = new CommentResponseDTO(comment.getId(), comment.getText(), convertToUserDTO(comment.getUser()));
        return commentResponseDTO;
    }

    public PostRecordDTO convertToPostRecordDTO(Post post) {
        List<UserDTO> likeUserDTOs = post.getLikes().stream()
            .map(Like::getUser)
            .map(this::convertToUserDTO)
            .collect(Collectors.toList());

        PostRecordDTO postRecordDTO = new PostRecordDTO(
            post.getId(), 
            post.getCaption(), 
            convertToUserDTO(post.getUser()), 
            post.getComments().stream()
                    .map(this::convertToCommentResponseDTO)
                    .collect(Collectors.toList()),
            likeUserDTOs);
                
        return postRecordDTO;
    }
}
