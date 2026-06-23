package com.industry_connect.identity_service.repository;

import com.industry_connect.identity_service.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    Optional<Organization> findByGstNumber(String gstNumber);
    Optional<Organization> findByName(String name);
}
