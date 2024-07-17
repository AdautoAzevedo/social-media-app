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
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.socialmediaapp.models.Post;
import com.example.socialmediaapp.models.User;
import com.example.socialmediaapp.repositories.PostRepository;
import com.example.socialmediaapp.repositories.UserRepository;
import com.example.socialmediaapp.services.PostsService;

@ExtendWith(MockitoExtension.class)
public class PostsServiceTests {
    
    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private PostsService postsService;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void testAddPost() {
        User user = new User();
        Post post = new Post();
        post.setCaption("Test caption");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(postRepository.save(any(Post.class))).thenReturn(post);

        Post createdPost = postsService.addPost(post);
        assertEquals("Test caption", createdPost.getCaption());
        assertEquals(user, createdPost.getUser());
        verify(postRepository, times(1)).save(post);

    }

    @Test
    public void testGetPostForCurrentUser() {
        User user = new User();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(postRepository.findByUser(user)).thenReturn(List.of(new Post()));

        assertEquals(1, postsService.getPostsForCurrentUser().size());
        verify(postRepository, times(1)).findByUser(user);
    }

    @Test
    public void testEditPost() {
        User user = new User();
        Post existingPost = new Post();
        existingPost.setUser(user);
        existingPost.setId(1L);

        Post updatedDetails = new Post();
        updatedDetails.setCaption("Updated Caption");

        when(postRepository.findById(1L)).thenReturn(Optional.of(existingPost));
        when(postRepository.save(existingPost)).thenReturn(existingPost);

        Post updatedPost = postsService.editPost(1L, updatedDetails);
        assertEquals("Updated Caption", updatedPost.getCaption());
        verify(postRepository, times(1)).save(existingPost);

    }
}
