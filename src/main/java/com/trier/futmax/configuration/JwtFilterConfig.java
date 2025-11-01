package com.trier.futmax.configuration;


import com.trier.futmax.util.JwtAuthFilter;
import com.trier.futmax.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class JwtFilterConfig {
    @Bean
    public JwtAuthFilter jwtAuthFilter(UserDetailsService userDetailsService, JwtUtil jwtUtil) {
        return new JwtAuthFilter(userDetailsService, jwtUtil);
    }


}
