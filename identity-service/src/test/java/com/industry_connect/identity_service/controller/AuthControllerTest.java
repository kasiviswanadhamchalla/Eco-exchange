package com.industry_connect.identity_service.controller;

import com.industry_connect.identity_service.dto.AuthResponse;
import com.industry_connect.identity_service.dto.LoginRequest;
import com.industry_connect.identity_service.dto.RegisterRequest;
import com.industry_connect.identity_service.entity.Organization;
import com.industry_connect.identity_service.service.AuthService;
import com.industry_connect.identity_service.service.OrganizationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private OrganizationService organizationService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void testRegister() {
        RegisterRequest req = new RegisterRequest();
        Organization org = new Organization();
        org.setId(1L);

        when(organizationService.registerOrganization(req)).thenReturn(org);

        ResponseEntity<Organization> response = authController.register(req);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void testLogin() {
        LoginRequest req = new LoginRequest();
        AuthResponse authResponse = new AuthResponse("token", 1L, 2L, "email@example.com", "ADMIN");

        when(authService.login(req)).thenReturn(authResponse);

        ResponseEntity<AuthResponse> response = authController.login(req);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("token", response.getBody().getToken());
    }
}
