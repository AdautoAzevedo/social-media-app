package com.example.socialmediaapp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.socialmediaapp.models.User;

public interface UserRepository extends JpaRepository<User, Long>{
    //Maybe change for just user
    Optional<User> findByUsername(String username);
}
