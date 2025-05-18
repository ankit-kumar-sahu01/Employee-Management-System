package com.employeemanagementsystem.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		 // Skip token processing for registration and login endpoints
        if (request.getRequestURI().equals("/auth/register") || request.getRequestURI().equals("/auth/login")) {

			filterChain.doFilter(request, response);
			return; // Exit early for login
		}

		String authorizationHeader = request.getHeader("Authorization");
		// Check if header contains Bearer token
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring(7);
			String userName = jwtUtil.extractUsername(token);

			// Validate token and set authentication
			  if (jwtUtil.validateToken(token, userName)) {
	                // Create an authentication token and set it in SecurityContext
	                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
	                        userName, null, null); // You can add authorities here if needed
	                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
	            } else {
	                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	                response.getWriter().write("Invalid or expired JWT token.");
	                return;
	            }
	        } else {
	            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	            response.getWriter().write("JWT token missing.");
	            return;
	        }
		// Handle JWT processing for other endpoints here (not shown in the code)
		// JWT token validation code (for example, extract the token and validate it)
		filterChain.doFilter(request, response);

	}

}
