package com.example.socialmediaapp.dtos;

import java.util.List;

import com.example.socialmediaapp.models.Like;

public record PostRecordDTO(Long postId, String caption, UserDTO user, List<CommentResponseDTO> comments, List<UserDTO> likes) {
}
