package com.industry_connect.identity_service.service;

import com.industry_connect.identity_service.dto.RegisterRequest;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrganizationServiceTest {

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrganizationService organizationService;

    private RegisterRequest request;

    @BeforeEach
    void setUp() {
        request = new RegisterRequest();
        request.setName("Eco Corp");
        request.setIndustryType("Recycler");
        request.setGstNumber("GST12345");
        request.setCity("Seattle");
        request.setState("WA");
        request.setCountry("USA");
        request.setContactName("John Doe");
        request.setEmail("john@eco.com");
        request.setPassword("password123");
    }

    @Test
    void testRegisterOrganization_Success() {
        when(organizationRepository.findByGstNumber(request.getGstNumber())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        Organization savedOrg = new Organization();
        savedOrg.setId(1L);
        savedOrg.setName(request.getName());
        savedOrg.setGstNumber(request.getGstNumber());
        savedOrg.setVerificationStatus("PENDING");

        when(organizationRepository.save(any(Organization.class))).thenReturn(savedOrg);

        Organization result = organizationService.registerOrganization(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("PENDING", result.getVerificationStatus());
        verify(organizationRepository, times(1)).save(any(Organization.class));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterOrganization_DuplicateGst() {
        when(organizationRepository.findByGstNumber(request.getGstNumber()))
                .thenReturn(Optional.of(new Organization()));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            organizationService.registerOrganization(request);
        });

        assertEquals("GST number already registered", exception.getMessage());
        verify(organizationRepository, never()).save(any(Organization.class));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRegisterOrganization_DuplicateEmail() {
        when(organizationRepository.findByGstNumber(request.getGstNumber())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(new User()));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            organizationService.registerOrganization(request);
        });

        assertEquals("Email already registered", exception.getMessage());
        verify(organizationRepository, never()).save(any(Organization.class));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testVerifyOrganization() {
        Organization org = new Organization();
        org.setId(1L);
        org.setVerificationStatus("PENDING");

        when(organizationRepository.findById(1L)).thenReturn(Optional.of(org));
        when(organizationRepository.save(any(Organization.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Organization result = organizationService.verifyOrganization(1L);

        assertEquals("VERIFIED", result.getVerificationStatus());
    }

    @Test
    void testSuspendOrganization() {
        Organization org = new Organization();
        org.setId(1L);
        org.setVerificationStatus("PENDING");

        when(organizationRepository.findById(1L)).thenReturn(Optional.of(org));
        when(organizationRepository.save(any(Organization.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Organization result = organizationService.suspendOrganization(1L);

        assertEquals("SUSPENDED", result.getVerificationStatus());
    }

    @Test
    void testGetOrganizationAdminEmail_Success() {
        User user = new User();
        user.setEmail("admin@eco.com");
        user.setRole("ADMIN");

        when(userRepository.findByOrganizationId(10L)).thenReturn(java.util.List.of(user));

        String email = organizationService.getOrganizationAdminEmail(10L);

        assertEquals("admin@eco.com", email);
        verify(userRepository, times(1)).findByOrganizationId(10L);
    }

    @Test
    void testGetOrganizationAdminEmail_NotFound() {
        when(userRepository.findByOrganizationId(10L)).thenReturn(java.util.Collections.emptyList());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            organizationService.getOrganizationAdminEmail(10L);
        });

        assertTrue(exception.getMessage().contains("No admin user found for organization ID: 10"));
        verify(userRepository, times(1)).findByOrganizationId(10L);
    }
}
