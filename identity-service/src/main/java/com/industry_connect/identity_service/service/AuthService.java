package com.industry_connect.identity_service.service;

import com.industry_connect.identity_service.dto.AuthResponse;
import com.industry_connect.identity_service.dto.LoginRequest;
import com.industry_connect.identity_service.entity.Organization;
import com.industry_connect.identity_service.entity.User;
import com.industry_connect.identity_service.repository.OrganizationRepository;
import com.industry_connect.identity_service.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Value("${jwt.secret:super-secret-key-for-jwt-signing-change-in-production}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400000}")
    private Long jwtExpiration;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        if ("SUSPENDED".equalsIgnoreCase(user.getStatus())) {
            throw new RuntimeException("User account is suspended");
        }

        if (user.getOrganizationId() != null) {
            Organization org = organizationRepository.findById(user.getOrganizationId())
                    .orElseThrow(() -> new RuntimeException("Organization not found"));
            if ("SUSPENDED".equalsIgnoreCase(org.getVerificationStatus())) {
                throw new RuntimeException("Organization is suspended");
            }
        }

        String token = generateToken(user);

        return new AuthResponse(token, user.getOrganizationId(), user.getId(), user.getEmail(), user.getRole());
    }

    private String generateToken(User user) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("userId", user.getId())
                .claim("organizationId", user.getOrganizationId())
                .claim("role", user.getRole())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }
}
