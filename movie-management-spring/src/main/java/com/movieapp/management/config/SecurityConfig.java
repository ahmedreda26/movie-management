package com.movieapp.management.config;
import com.movieapp.management.security.CustomUserDetailsService;
import com.movieapp.management.security.JwtAuthenticationFilter;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.security.Key;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtRequestFilter;
    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtAuthenticationFilter jwtRequestFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtRequestFilter) throws Exception {
        http
                .cors(Customizer.withDefaults()) // Enable CORS with default settings
                .csrf(csrf -> csrf.disable()) // Disable CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // Allow authentication endpoints
                        .requestMatchers(HttpMethod.POST, "/api/movies/add").hasAuthority("ROLE_ADMIN") // Restrict adding movies
                        .requestMatchers(HttpMethod.POST, "/api/movies/addBatch").hasAuthority("ROLE_ADMIN") // Restrict batch adding to admin only
                        .requestMatchers(HttpMethod.DELETE, "/api/movies/delete/**").hasAuthority("ROLE_ADMIN") // Restrict deleting movies
                        .requestMatchers(HttpMethod.DELETE, "/api/movies/deleteBatch").hasAuthority("ROLE_ADMIN")

                        .requestMatchers("/api/movies/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER") // Allow movie access to both roles
                        .anyRequest().authenticated() // Authenticate all other requests
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class) // Ensure JWT filter runs first
                .httpBasic(Customizer.withDefaults()); // Use HTTP Basic authentication

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}