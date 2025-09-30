package com.demo.controller;

import com.demo.entity.User;
import com.demo.repository.UserRepository;
import com.demo.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/auth")
@RestController
public class Controller {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user){

        if(userRepository.existsByUsername(user.getUsername())){
            return ResponseEntity.status(500).body("User already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.status(200).body("Registered");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user){

        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    user.getUsername(),user.getPassword()
            ));

            String token = jwtService.generateToken(authentication);
            return ResponseEntity.status(201).body(token);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    "Invalid username or password"
            );
        }

    }

}
