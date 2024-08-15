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

        CommentDTO commentDTO = new CommentDTO(1L, "test comment");
        UserDTO userDTO = new UserDTO(1L, "testuser");
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
 
    @Test
    public void testGetCommentById() {
        UserDTO userDTO = new UserDTO(1L, "testuser");
        CommentResponseDTO commentResponseDTO = new CommentResponseDTO(1L, "test comment", userDTO);

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(mockComment));
        when(auxMethods.convertToCommentResponseDTO(any(Comment.class))).thenReturn(commentResponseDTO);

        CommentResponseDTO responseDTO = commentService.getCommentById(1L);

        assertNotNull(responseDTO);
        assertEquals(1L, responseDTO.commentId());
        assertEquals("test comment", responseDTO.text());
        assertEquals("testuser", responseDTO.userDTO().username());
        verify(commentRepository, times(1)).findById(anyLong());

    }

    @Test
    public void testGetCommentsForPosts() {
        UserDTO userDTO = new UserDTO(1L, "testuser");
        CommentResponseDTO commentResponseDTO = new CommentResponseDTO(1L, "test comment", userDTO);

        when(postRepository.findById(1L)).thenReturn(Optional.of(mockPost));
        when(commentRepository.findByPost(any(Post.class))).thenReturn(List.of(mockComment));
        when(auxMethods.convertToCommentResponseDTO(any(Comment.class))).thenReturn(commentResponseDTO);

        List<CommentResponseDTO> responseDTOs = commentService.getCommentsForPosts(1L);
        assertEquals(1, responseDTOs.size());

        assertEquals(1L, responseDTOs.get(0).commentId());
        assertEquals("test comment", responseDTOs.get(0).text());
        assertEquals("testuser", responseDTOs.get(0).userDTO().username());

        verify(commentRepository, times(1)).findByPost(any(Post.class));
    }

    
    @Test 
    public void testGetCommentsForUser() {
        UserDTO userDTO = new UserDTO(1L, "testuser");
        CommentResponseDTO commentResponseDTO = new CommentResponseDTO(1L, "test comment", userDTO);

        when(auxMethods.getAuthenticatedUser()).thenReturn(mockUser);
        when(commentRepository.findByUser(any(User.class))).thenReturn(List.of(mockComment));
        when(auxMethods.convertToCommentResponseDTO(any(Comment.class))).thenReturn(commentResponseDTO);

        List<CommentResponseDTO> responseDTOs = commentService.getCommentsForUser();

        assertEquals(1, responseDTOs.size());
        assertEquals(1L, responseDTOs.get(0).commentId());
        assertEquals("test comment", responseDTOs.get(0).text());
        assertEquals("testuser", responseDTOs.get(0).userDTO().username());
        verify(commentRepository, times(1)).findByUser(any(User.class));
    }

    @Test
    public void testEditComment() {
        UserDTO userDTO = new UserDTO(1L, "testuser");
        CommentResponseDTO commentResponseDTO = new CommentResponseDTO(1L, "updated comment", userDTO);
        
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(mockComment));
        when(auxMethods.getAuthenticatedUser()).thenReturn(mockUser);
        when(commentRepository.save(any(Comment.class))).thenReturn(mockComment);
        when(auxMethods.convertToCommentResponseDTO(any(Comment.class))).thenReturn(commentResponseDTO);
      
        Comment correctedComment = new Comment();
        correctedComment.setText("updated comment");

        CommentResponseDTO responseDTO = commentService.editComment(1L, correctedComment);

        assertNotNull(responseDTO);
        assertEquals(1L, responseDTO.commentId());
        assertEquals("updated comment", responseDTO.text());
        assertEquals("testuser", responseDTO.userDTO().username());

        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    public void testDeleteComment() {
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(mockComment));
        when(auxMethods.getAuthenticatedUser()).thenReturn(mockUser);

        commentService.deleteComment(1L);

        verify(commentRepository, times(1)).delete(any(Comment.class));
    } 
}
