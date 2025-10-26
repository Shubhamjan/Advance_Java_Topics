package com.demo.filters;

import com.demo.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        // 1️⃣ Get Authorization header
        String authHeader = request.getHeader("Authorization");

        String token = null;

        String username = null;

        // 2️⃣ Check if header starts with "Bearer "

        if(authHeader!=null && authHeader.startsWith("Bearer")){
            token = authHeader.substring(7);

            try{

                username = jwtService.getUsernameFromToken(token);
            }catch (Exception e){
                logger.info("Invalid JWT : "+e.getMessage());
                throw new RuntimeException("Invalid token");
            }
        }

        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if(jwtService.validateToken(token,userDetails)){
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }else{
                throw new RuntimeException("Invalid token");
            }
        }

        // 4️⃣ Continue filter chain
        filterChain.doFilter(request, response);

    }
}
