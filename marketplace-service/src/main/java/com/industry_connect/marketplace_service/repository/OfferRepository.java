package com.industry_connect.marketplace_service.repository;

import com.industry_connect.marketplace_service.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
    List<Offer> findByListingId(Long listingId);
    List<Offer> findByBuyerOrgId(Long buyerOrgId);
}
