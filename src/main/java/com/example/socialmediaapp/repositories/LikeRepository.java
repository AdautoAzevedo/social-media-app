package com.example.socialmediaapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.socialmediaapp.models.Like;
import com.example.socialmediaapp.models.Post;
import com.example.socialmediaapp.models.User;

public interface LikeRepository extends JpaRepository<Like, Long>{
    boolean existsByUserAndPost(User user, Post post);
}
