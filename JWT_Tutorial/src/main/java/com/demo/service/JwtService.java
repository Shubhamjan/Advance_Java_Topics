package com.demo.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt_secret}")
    private String jwtSecret;

    @Value("${jwt_expiration}")
    private long expiration;

    public String generateToken(Authentication authentication){

        Date now = new Date();
        Date expiryDate = new Date(now.getTime()+expiration);

        return Jwts.
                builder()
                .setSubject(authentication.getName())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUsernameFromToken(String token){

        String subject =  Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        System.out.println("The suject is  "+subject);
        return subject;

    }

    public boolean validateToken(String token, UserDetails userDetails){

        try{

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();
            return username.equals(userDetails.getUsername())&& claims.getExpiration().after(new Date());
        }catch (Exception e){
            return false;
        }
    }

}
