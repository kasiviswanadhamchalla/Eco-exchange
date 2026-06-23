package com.industry_connect.identity_service.dto;

public class AuthResponse {
    private String token;
    private Long organizationId;
    private Long userId;
    private String email;
    private String role;

    public AuthResponse() {}

    public AuthResponse(String token, Long organizationId, Long userId, String email, String role) {
        this.token = token;
        this.organizationId = organizationId;
        this.userId = userId;
        this.email = email;
        this.role = role;
    }

    // Getters and Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public Long getOrganizationId() { return organizationId; }
    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
