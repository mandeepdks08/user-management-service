package com.users.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.users.security.JwtAuthenticationFilter;

@Configuration
public class WebSecurityConfig {
	
	@Autowired
	private JwtAuthenticationFilter jwtAuthFilter;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
	            .csrf(csrf -> csrf.disable()) // Disable CSRF because we use JWTs (no cookies)
	            .cors(cors -> cors.disable())
	            .sessionManagement(session -> session
	                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // No sessions
	            .authorizeHttpRequests(auth -> auth
	            	.antMatchers("/user/v1/register", "/user/v1/login", "/user/v1/authenticate", "/user/actuator/**").permitAll() // Open endpoints
	                .anyRequest().authenticated() // Everything else needs JWT
	            )
	            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
	            .build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
}
