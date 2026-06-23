package com.industry_connect.identity_service.controller;

import com.industry_connect.identity_service.entity.Organization;
import com.industry_connect.identity_service.service.OrganizationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrganizationControllerTest {

    @Mock
    private OrganizationService organizationService;

    @InjectMocks
    private OrganizationController organizationController;

    @Test
    void testGetOrganization_Found() {
        Organization org = new Organization();
        org.setId(1L);
        when(organizationService.getOrganization(1L)).thenReturn(Optional.of(org));

        ResponseEntity<Organization> response = organizationController.getOrganization(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void testGetOrganization_NotFound() {
        when(organizationService.getOrganization(1L)).thenReturn(Optional.empty());

        ResponseEntity<Organization> response = organizationController.getOrganization(1L);

        assertNotNull(response);
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void testVerifyOrganization_Success() {
        Organization org = new Organization();
        org.setId(1L);
        org.setVerificationStatus("VERIFIED");
        when(organizationService.verifyOrganization(1L)).thenReturn(org);

        ResponseEntity<Organization> response = organizationController.verifyOrganization(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("VERIFIED", response.getBody().getVerificationStatus());
    }

    @Test
    void testVerifyOrganization_Failure() {
        when(organizationService.verifyOrganization(1L)).thenThrow(new RuntimeException("Not found"));

        ResponseEntity<Organization> response = organizationController.verifyOrganization(1L);

        assertNotNull(response);
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void testSuspendOrganization_Success() {
        Organization org = new Organization();
        org.setId(1L);
        org.setVerificationStatus("SUSPENDED");
        when(organizationService.suspendOrganization(1L)).thenReturn(org);

        ResponseEntity<Organization> response = organizationController.suspendOrganization(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("SUSPENDED", response.getBody().getVerificationStatus());
    }

    @Test
    void testSuspendOrganization_Failure() {
        when(organizationService.suspendOrganization(1L)).thenThrow(new RuntimeException("Not found"));

        ResponseEntity<Organization> response = organizationController.suspendOrganization(1L);

        assertNotNull(response);
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void testGetOrganizationAdminEmail_Success() {
        when(organizationService.getOrganizationAdminEmail(10L)).thenReturn("admin@eco.com");

        ResponseEntity<String> response = organizationController.getOrganizationAdminEmail(10L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("admin@eco.com", response.getBody());
        verify(organizationService, times(1)).getOrganizationAdminEmail(10L);
    }

    @Test
    void testGetOrganizationAdminEmail_Failure() {
        when(organizationService.getOrganizationAdminEmail(10L)).thenThrow(new RuntimeException("Not found"));

        ResponseEntity<String> response = organizationController.getOrganizationAdminEmail(10L);

        assertNotNull(response);
        assertEquals(404, response.getStatusCode().value());
        verify(organizationService, times(1)).getOrganizationAdminEmail(10L);
    }
}
