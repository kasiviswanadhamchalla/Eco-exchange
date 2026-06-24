package com.industry_connect.identity_service.controller;

import com.industry_connect.identity_service.entity.Organization;
import com.industry_connect.identity_service.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @GetMapping("/{id}")
    public ResponseEntity<Organization> getOrganization(@PathVariable Long id) {
        return organizationService.getOrganization(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/verify")
    @PreAuthorize("hasRole('PLATFORM_ADMIN')")
    public ResponseEntity<Organization> verifyOrganization(@PathVariable Long id) {
        try {
            Organization org = organizationService.verifyOrganization(id);
            return ResponseEntity.ok(org);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/suspend")
    @PreAuthorize("hasRole('PLATFORM_ADMIN')")
    public ResponseEntity<Organization> suspendOrganization(@PathVariable Long id) {
        try {
            Organization org = organizationService.suspendOrganization(id);
            return ResponseEntity.ok(org);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/admin-email")
    public ResponseEntity<String> getOrganizationAdminEmail(@PathVariable Long id) {
        try {
            String email = organizationService.getOrganizationAdminEmail(id);
            return ResponseEntity.ok(email);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<java.util.List<Organization>> getAllOrganizations() {
        return ResponseEntity.ok(organizationService.getAllOrganizations());
    }
}
