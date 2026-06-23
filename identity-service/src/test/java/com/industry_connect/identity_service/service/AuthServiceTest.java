package com.industry_connect.identity_service.service;

import com.industry_connect.identity_service.dto.AuthResponse;
import com.industry_connect.identity_service.dto.LoginRequest;
import com.industry_connect.identity_service.entity.Organization;
import com.industry_connect.identity_service.entity.User;
import com.industry_connect.identity_service.repository.OrganizationRepository;
import com.industry_connect.identity_service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrganizationRepository organizationRepository;

    @InjectMocks
    private AuthService authService;

    private User activeUser;
    private Organization activeOrg;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(authService, "jwtSecret", "super-secret-key-for-jwt-signing-change-in-production");
        ReflectionTestUtils.setField(authService, "jwtExpiration", 86400000L);

        activeOrg = new Organization();
        activeOrg.setId(10L);
        activeOrg.setVerificationStatus("VERIFIED");

        activeUser = new User();
        activeUser.setId(1L);
        activeUser.setOrganizationId(10L);
        activeUser.setEmail("user@example.com");
        activeUser.setPasswordHash(encoder.encode("securePassword"));
        activeUser.setRole("ADMIN");
        activeUser.setStatus("ACTIVE");
    }

    @Test
    void testLogin_Success() {
        LoginRequest req = new LoginRequest();
        req.setEmail("user@example.com");
        req.setPassword("securePassword");

        when(userRepository.findByEmail(req.getEmail())).thenReturn(Optional.of(activeUser));
        when(organizationRepository.findById(10L)).thenReturn(Optional.of(activeOrg));

        AuthResponse response = authService.login(req);

        assertNotNull(response);
        assertEquals("user@example.com", response.getEmail());
        assertEquals("ADMIN", response.getRole());
        assertEquals(10L, response.getOrganizationId());
        assertNotNull(response.getToken());
    }

    @Test
    void testLogin_InvalidCredentials() {
        LoginRequest req = new LoginRequest();
        req.setEmail("user@example.com");
        req.setPassword("wrongPassword");

        when(userRepository.findByEmail(req.getEmail())).thenReturn(Optional.of(activeUser));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            authService.login(req);
        });

        assertEquals("Invalid credentials", exception.getMessage());
    }

    @Test
    void testLogin_SuspendedUser() {
        LoginRequest req = new LoginRequest();
        req.setEmail("user@example.com");
        req.setPassword("securePassword");

        activeUser.setStatus("SUSPENDED");
        when(userRepository.findByEmail(req.getEmail())).thenReturn(Optional.of(activeUser));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            authService.login(req);
        });

        assertEquals("User account is suspended", exception.getMessage());
    }

    @Test
    void testLogin_SuspendedOrg() {
        LoginRequest req = new LoginRequest();
        req.setEmail("user@example.com");
        req.setPassword("securePassword");

        activeOrg.setVerificationStatus("SUSPENDED");
        when(userRepository.findByEmail(req.getEmail())).thenReturn(Optional.of(activeUser));
        when(organizationRepository.findById(10L)).thenReturn(Optional.of(activeOrg));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            authService.login(req);
        });

        assertEquals("Organization is suspended", exception.getMessage());
    }
}
