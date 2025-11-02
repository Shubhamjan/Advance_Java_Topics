package com.demo.controller;

import com.demo.config.CustomUser;
import com.demo.entity.BlackListToken;
import com.demo.entity.User;
import com.demo.repository.UserRepository;
import com.demo.service.BlackListService;
import com.demo.service.JwtService;
import com.demo.service.MyUserDetailService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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

    @Autowired
    private BlackListService blackListService;


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user){

        if(userRepository.existsByUsername(user.getUsername())){
            return ResponseEntity.status(500).body("User already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_"+user.getRole());
        userRepository.save(user);
        return ResponseEntity.status(200).body("Registered");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user){

        System.out.println("User :- "+user.toString());

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

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request){

        String authHeader = request.getHeader("Authorization");

        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            String token = authHeader.substring(7);
            LocalDateTime expiry = jwtService.extractExpiration(token);
            System.out.println("The expiraation time in logout "+expiry.toString());
            BlackListToken b = blackListService.blackListToken(token,expiry);
            if(b!=null){
                return ResponseEntity.status(HttpStatus.GONE).body("Logout successfully");
            }
        }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token");

    }




}
