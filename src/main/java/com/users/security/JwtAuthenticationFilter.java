package com.users.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.users.datamodel.User;
import com.users.util.JwtUtil;
import com.users.util.SystemContextHolder;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final String authorizationHeader = request.getHeader("Authorization");
		log.info("In JWT Authentication filter");
		if (authenticate(authorizationHeader) == true) {
			String jwt = authorizationHeader.substring(7);
			String username = jwtUtil.extractUsername(jwt);
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
					userDetails, null, userDetails.getAuthorities());
			usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SystemContextHolder.setLoggedInUser((User) userDetails);
			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
		}
		filterChain.doFilter(request, response);
	}

	public boolean authenticate(String token) throws IOException {
		if (StringUtils.isNotBlank(token) && token.startsWith("Bearer ")) {
			String jwt = token.substring(7);
			String username = null;
			try {
				username = jwtUtil.extractUsername(jwt);
			} catch (ExpiredJwtException e) {
				throw new IOException("Authorization token is expired!");
			} catch (Exception e) {
				log.error("Authentication failed with an error", e);
			}
			if (username != null) {
				UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
				if (jwtUtil.validateToken(jwt, userDetails)) {
					return true;
				}
			}
		}
		return false;
	}

}
