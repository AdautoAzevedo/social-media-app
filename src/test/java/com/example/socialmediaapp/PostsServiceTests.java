package com.example.socialmediaapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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


import com.example.socialmediaapp.dtos.PostRecordDTO;
import com.example.socialmediaapp.models.Post;
import com.example.socialmediaapp.models.User;
import com.example.socialmediaapp.repositories.PostRepository;
import com.example.socialmediaapp.services.AuxMethods;
import com.example.socialmediaapp.services.PostsService;

@ExtendWith(MockitoExtension.class)
public class PostsServiceTests {
    
    @Mock
    private PostRepository postRepository;

    @Mock
    private AuxMethods auxMethods;

    @InjectMocks
    private PostsService postsService;

    private User mockUser;
    private Post post;
    private PostRecordDTO postRecordDTO;
    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        post = new Post();
        post.setId(1L);
        post.setUser(mockUser);
    }

    @Test
    public void testAddPost() {
        when(auxMethods.getAuthenticatedUser()).thenReturn(mockUser);
        when(postRepository.save(post)).thenReturn(post);
        when(auxMethods.convertToPostRecordDTO(post)).thenReturn(postRecordDTO);

        PostRecordDTO result = postsService.addPost(post);

        assertEquals(postRecordDTO, result);
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    public void testetPostsForCurrentUser() {
        when(auxMethods.getAuthenticatedUser()).thenReturn(mockUser);
        when(postRepository.findByUser(mockUser)).thenReturn(List.of(post));
        when(auxMethods.convertToPostRecordDTO(post)).thenReturn(postRecordDTO);

        List<PostRecordDTO> result = postsService.getPostsForCurrentUser();

        assertEquals(1, result.size());
        assertEquals(postRecordDTO, result.get(0));
        verify(postRepository, times(1)).findByUser(mockUser);
    }

    @Test
    public void testGetPostById() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(auxMethods.convertToPostRecordDTO(post)).thenReturn(postRecordDTO);

        PostRecordDTO result = postsService.getPostById(1L);

        assertEquals(postRecordDTO, result);
        verify(postRepository, times(1)).findById(1L);
    }

    @Test
    public void testEditPost() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(auxMethods.getAuthenticatedUser()).thenReturn(mockUser);
        when(postRepository.save(post)).thenReturn(post);
        when(auxMethods.convertToPostRecordDTO(post)).thenReturn(postRecordDTO);

        Post correctedPost = new Post();
        correctedPost.setCaption("Updated caption");

        PostRecordDTO result = postsService.editPost(1L, correctedPost);

        assertEquals(postRecordDTO, result);
        assertEquals("Updated caption", post.getCaption());
        verify(postRepository, times(1)).save(any(Post.class));

    }

    @Test
    public void testDeletePost() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(auxMethods.getAuthenticatedUser()).thenReturn(mockUser);

        postsService.deletePost(1L);

        verify(postRepository).delete(post);
    }
}
