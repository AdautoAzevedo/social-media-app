package com.example.socialmediaapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.socialmediaapp.dtos.AuthenticationDTO;
import com.example.socialmediaapp.dtos.LoginResponseDTO;
import com.example.socialmediaapp.dtos.RegisterDTO;
import com.example.socialmediaapp.models.User;
import com.example.socialmediaapp.models.UserPrincipal;
import com.example.socialmediaapp.repositories.UserRepository;
import com.example.socialmediaapp.security.TokenService;

@RestController
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        System.out.println(data.login());
        System.out.println(data.password());

        try {
            System.out.println(usernamePassword);
            var auth = this.authenticationManager.authenticate(usernamePassword);
            System.out.println(auth);
            if (auth.getPrincipal() instanceof UserPrincipal) {
                var userPrincipal = (UserPrincipal) auth.getPrincipal();
                var token = tokenService.generateToken(userPrincipal);
                return ResponseEntity.ok(new LoginResponseDTO(token));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();    
        }
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterDTO data) {
        if (data.login() == null || data.password() == null || data.name() == null) {
            return ResponseEntity.badRequest().body("All fields are required.");
        }

        if (this.userRepository.findByUsername(data.login()) != null) return ResponseEntity.badRequest().build();

        

        try {
            String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
            User newUser = new User(data.login(), encryptedPassword, data.name());
            this.userRepository.save(newUser);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
       
    }
}
