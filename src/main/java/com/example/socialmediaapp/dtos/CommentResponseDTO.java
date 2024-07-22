package com.example.socialmediaapp.dtos;

public record CommentResponseDTO(Long commentId, String text, UserDTO userDTO) {   
}
