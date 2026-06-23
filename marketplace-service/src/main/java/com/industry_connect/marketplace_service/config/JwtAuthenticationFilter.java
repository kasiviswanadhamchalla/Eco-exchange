package com.industry_connect.marketplace_service.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.secret:default-secret-key-for-development-change-in-production}")
    private String jwtSecret;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
                Claims claims = Jwts.parser()
                        .verifyWith(key)
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();

                String email = claims.getSubject();
                String role = claims.get("role", String.class);
                
                // Get userId and organizationId from claims safely
                Object userIdObj = claims.get("userId");
                Long userId = userIdObj != null ? ((Number) userIdObj).longValue() : null;

                Object orgIdObj = claims.get("organizationId");
                Long orgId = orgIdObj != null ? ((Number) orgIdObj).longValue() : null;

                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    String authorityRole;
                    if (("ADMIN".equalsIgnoreCase(role) || "PLATFORM_ADMIN".equalsIgnoreCase(role)) && orgId == null) {
                        authorityRole = "ROLE_PLATFORM_ADMIN";
                    } else if (role != null) {
                        authorityRole = "ROLE_" + role.toUpperCase();
                    } else {
                        authorityRole = "ROLE_USER";
                    }

                    List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                            new SimpleGrantedAuthority(authorityRole)
                    );

                    UserPrincipal principal = new UserPrincipal(userId, email, orgId, role);

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            principal, null, authorities
                    );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                // Invalid token, do not set authentication
            }
        }

        filterChain.doFilter(request, response);
    }
}
