package com.example.socialmediaapp.dtos;

import java.util.List;

public record PostRecordDTO(Long postId, String caption, UserDTO user, List<CommentResponseDTO> comments) {
}
