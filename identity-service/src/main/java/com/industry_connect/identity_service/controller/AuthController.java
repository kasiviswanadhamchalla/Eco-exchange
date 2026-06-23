package com.industry_connect.identity_service.controller;

import com.industry_connect.identity_service.dto.AuthResponse;
import com.industry_connect.identity_service.dto.LoginRequest;
import com.industry_connect.identity_service.dto.RegisterRequest;
import com.industry_connect.identity_service.entity.Organization;
import com.industry_connect.identity_service.service.AuthService;
import com.industry_connect.identity_service.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Organization> register(@RequestBody RegisterRequest request) {
        Organization org = organizationService.registerOrganization(request);
        return ResponseEntity.ok(org);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
