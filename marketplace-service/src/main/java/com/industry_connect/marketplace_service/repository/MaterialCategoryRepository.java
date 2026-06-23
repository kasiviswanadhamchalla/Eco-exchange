package com.industry_connect.marketplace_service.repository;

import com.industry_connect.marketplace_service.entity.MaterialCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MaterialCategoryRepository extends JpaRepository<MaterialCategory, Long> {
    Optional<MaterialCategory> findByName(String name);
}
