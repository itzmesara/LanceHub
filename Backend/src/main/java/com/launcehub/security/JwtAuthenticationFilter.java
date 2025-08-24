package com.launcehub.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserPrincipalService userPrincipalService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        
        logger.info("JwtAuthenticationFilter processing: " + method + " " + requestURI);
        
        // Skip JWT processing for OPTIONS requests (CORS preflight)
        if ("OPTIONS".equalsIgnoreCase(method)) {
            logger.info("Skipping JWT processing for OPTIONS request: " + requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        // Skip JWT processing for public endpoints
        if (requestURI.startsWith("/api/auth/") || requestURI.startsWith("/api/public/") || 
            requestURI.startsWith("/swagger-ui/") || requestURI.startsWith("/v3/api-docs/") ||
            (requestURI.startsWith("/api/projects") && "GET".equalsIgnoreCase(method)) ||
            requestURI.startsWith("/api/test/")) {
            logger.info("Skipping JWT processing for public endpoint: " + method + " " + requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        final String authorizationHeader = request.getHeader("Authorization");
        logger.info("Authorization header present: " + (authorizationHeader != null));
        
        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwt);
                logger.info("Extracted username from JWT: " + username);
            } catch (Exception e) {
                logger.error("Cannot extract username from JWT token", e);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid JWT token");
                return;
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = this.userPrincipalService.loadUserByUsername(username);

                if (jwtUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    logger.info("Successfully authenticated user: " + username);
                } else {
                    logger.warn("Invalid or expired JWT token for user: " + username);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Invalid or expired JWT token");
                    return;
                }
            } catch (Exception e) {
                logger.error("Cannot load user details", e);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Authentication failed");
                return;
            }
        }
        
        logger.info("Proceeding with filter chain for: " + method + " " + requestURI);
        filterChain.doFilter(request, response);
    }
}
