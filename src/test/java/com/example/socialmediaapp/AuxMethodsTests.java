package com.example.socialmediaapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.socialmediaapp.dtos.CommentResponseDTO;
import com.example.socialmediaapp.dtos.PostRecordDTO;
import com.example.socialmediaapp.dtos.UserDTO;
import com.example.socialmediaapp.models.Comment;
import com.example.socialmediaapp.models.Post;
import com.example.socialmediaapp.models.User;
import com.example.socialmediaapp.repositories.UserRepository;
import com.example.socialmediaapp.services.AuxMethods;

@ExtendWith(MockitoExtension.class)
public class AuxMethodsTests {
    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuxMethods auxMethods;

    @Test
    public void testGetAuthenticatedUser_UserFound() {
        String username = "testuser";
        User user = new User();
        user.setUsername(username);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        SecurityContextHolder.setContext(securityContext);

        User result = auxMethods.getAuthenticatedUser();
        assertEquals(user, result);
    }

    @Test
    public void testConvertToUserDTO() {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");

        UserDTO userDTO = auxMethods.convertToUserDTO(user);
        assertEquals(1L, userDTO.userId());
        assertEquals("Test User", userDTO.username());

    }

    @Test
    public void testConvertToCommentResponseDTO() {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("Test Comment");
        comment.setUser(user);

        CommentResponseDTO commentResponseDTO = auxMethods.convertToCommentResponseDTO(comment);
        assertEquals(1L, commentResponseDTO.commentId());
        assertEquals("Test Comment", commentResponseDTO.text());
        assertEquals(1L, commentResponseDTO.userDTO().userId());
        assertEquals("Test User", commentResponseDTO.userDTO().username());
    }

    @Test
    public void testConvertToPostRecordDTO() {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("Test Comment");
        comment.setUser(user);

        Post post = new Post();
        post.setId(1L);
        post.setCaption("Test Caption");
        post.setUser(user);

        post.setComments(List.of(comment));

        PostRecordDTO postRecordDTO = auxMethods.convertToPostRecordDTO(post);
        assertEquals(1L, postRecordDTO.postId());
        assertEquals("Test Caption", postRecordDTO.caption());
        assertEquals(1L, postRecordDTO.user().userId());
        assertEquals("Test User", postRecordDTO.user().username());
        assertEquals(1L, postRecordDTO.comments().get(0).commentId());
        assertEquals("Test Comment", postRecordDTO.comments().get(0).text());
    }


}
