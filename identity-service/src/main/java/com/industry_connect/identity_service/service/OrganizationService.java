package com.industry_connect.identity_service.service;

import com.industry_connect.identity_service.dto.RegisterRequest;
import com.industry_connect.identity_service.entity.Organization;
import com.industry_connect.identity_service.entity.User;
import com.industry_connect.identity_service.repository.OrganizationRepository;
import com.industry_connect.identity_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public Organization registerOrganization(RegisterRequest request) {
        if (organizationRepository.findByGstNumber(request.getGstNumber()).isPresent()) {
            throw new IllegalArgumentException("GST number already registered");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }

        Organization organization = new Organization(
                request.getName(),
                request.getIndustryType(),
                request.getGstNumber(),
                "PENDING",
                request.getCity(),
                request.getState(),
                request.getCountry()
        );

        organization = organizationRepository.save(organization);

        User user = new User(
                organization.getId(),
                request.getContactName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                "ADMIN",
                "ACTIVE"
        );
        userRepository.save(user);

        return organization;
    }

    public Optional<Organization> getOrganization(Long id) {
        return organizationRepository.findById(id);
    }

    @Transactional
    public Organization verifyOrganization(Long id) {
        Organization org = organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found"));
        org.setVerificationStatus("VERIFIED");
        return organizationRepository.save(org);
    }

    @Transactional
    public Organization suspendOrganization(Long id) {
        Organization org = organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found"));
        org.setVerificationStatus("SUSPENDED");
        return organizationRepository.save(org);
    }

    public String getOrganizationAdminEmail(Long orgId) {
        return userRepository.findByOrganizationId(orgId).stream()
                .filter(u -> "ADMIN".equalsIgnoreCase(u.getRole()))
                .map(User::getEmail)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No admin user found for organization ID: " + orgId));
    }

    public java.util.List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }
}
