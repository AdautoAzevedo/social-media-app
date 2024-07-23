package com.example.socialmediaapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.socialmediaapp.dtos.CommentDTO;
import com.example.socialmediaapp.dtos.CommentResponseDTO;
import com.example.socialmediaapp.dtos.UserDTO;
import com.example.socialmediaapp.models.Comment;
import com.example.socialmediaapp.models.Post;
import com.example.socialmediaapp.models.User;
import com.example.socialmediaapp.repositories.CommentRepository;
import com.example.socialmediaapp.repositories.PostRepository;
import com.example.socialmediaapp.repositories.UserRepository;
import com.example.socialmediaapp.services.AuxMethods;
import com.example.socialmediaapp.services.CommentService;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTests {
    
    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private AuxMethods auxMethods;

    @InjectMocks
    private CommentService commentService;

    private User mockUser;
    private Post mockPost;
    private Comment mockComment;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");

        mockPost = new Post();
        mockPost.setId(1L);
        mockPost.setCaption("test caption");

        mockComment = new Comment();
        mockComment.setId(1L);
        mockComment.setText("test comment");
        mockComment.setUser(mockUser);
        mockComment.setPost(mockPost);
    }


    @Test
    public void testAddComment() {
       CommentDTO commentDTO = new CommentDTO(1L, "test comment");
       UserDTO userDTO = new UserDTO(1L, "testuser");

       when(auxMethods.getAuthenticatedUser()).thenReturn(mockUser);
       when(postRepository.findById(anyLong())).thenReturn(Optional.of(mockPost));
       when(commentRepository.save(any(Comment.class))).thenReturn(mockComment);
       when(auxMethods.convertToCommentResponseDTO(any(Comment.class))).thenReturn(new CommentResponseDTO(1L, "test comment", userDTO));

       CommentResponseDTO responseDTO = commentService.addComment(commentDTO);
       assertNotNull(responseDTO);
       assertEquals(1L, responseDTO.commentId());
       assertEquals("test comment", responseDTO.text());
       assertEquals("testuser", responseDTO.userDTO().username());

       verify(commentRepository, times(1)).save(any(Comment.class));
    } 
/* 
    @Test
    public void testGetCommentById() {
        Comment comment = new Comment();
        comment.setId(1L);

        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        Comment foundComment = commentService.getCommentById(1L);

        assertEquals(comment, foundComment);
        verify(commentRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetCommentsForPosts() {
        Post post = new Post();
        post.setId(1L);

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(commentRepository.findByPost(post)).thenReturn(List.of(new Comment(), new Comment()));

        List<Comment> comments = commentService.getCommentsForPosts(1L);

        assertEquals(2, comments.size());
        verify(commentRepository, times(1)).findByPost(post);
    }

    @Test 
    public void testGetCommentsForUser() {
        User user = new User();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(commentRepository.findByUser(user)).thenReturn(List.of(new Comment(), new Comment()));

        List<Comment> comments = commentService.getCommentsForUser();

        assertEquals(2, comments.size());
        verify(commentRepository, times(1)).findByUser(user);
    }

    @Test
    public void testEditComment() {
        User user = new User();
        Comment existingComment = new Comment();
        existingComment.setId(1L);
        existingComment.setUser(user);
        existingComment.setText("Text to be changed");

        Comment commentDetails = new Comment();
        commentDetails.setText("New text");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(commentRepository.findById(1L)).thenReturn(Optional.of(existingComment));
        when(commentRepository.save(existingComment)).thenReturn(existingComment);

        Comment updatedComment = commentService.editComment(1L, commentDetails);

        assertEquals("New text", updatedComment.getText());
        assertEquals(existingComment.getId(), updatedComment.getId());
        verify(commentRepository, times(1)).save(existingComment);
        verify(commentRepository, times(1)).findById(1L);


    }

    @Test
    public void testDeleteComment() {
        User user = new User();
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setUser(user);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        
        commentService.deleteComment(1L);
        verify(commentRepository, times(1)).findById(1L);
        verify(commentRepository, times(1)).delete(comment);

    } */
}
