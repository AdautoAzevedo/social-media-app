package com.example.socialmediaapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.socialmediaapp.models.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

}
