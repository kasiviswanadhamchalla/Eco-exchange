package com.industry_connect.order_service.config;

public class UserPrincipal {
    private final Long userId;
    private final String email;
    private final Long organizationId;
    private final String role;

    public UserPrincipal(Long userId, String email, Long organizationId, String role) {
        this.userId = userId;
        this.email = email;
        this.organizationId = organizationId;
        this.role = role;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "UserPrincipal{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", organizationId=" + organizationId +
                ", role='" + role + '\'' +
                '}';
    }
}
