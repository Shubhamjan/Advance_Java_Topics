package com.demo.controller;

import com.demo.config.CustomUser;
import com.demo.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/check")
@RestController
public class CheckController {

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/profile")
    public ResponseEntity<String> getProfile(@RequestBody User user){

        return ResponseEntity.status(200).body(user.getUsername()+" has "+"password "+user.getPassword());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/context")
    public ResponseEntity<String> context(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        try{
            CustomUser customUser = (CustomUser) authentication.getPrincipal();
            return ResponseEntity.status(200).body("The username is "+customUser.getUsername()+ " and password is "+
                    customUser.getPassword()+" in security contextz");
        }catch (Exception e){
            return ResponseEntity.status(200).body("No data found");
        }

    }
}
