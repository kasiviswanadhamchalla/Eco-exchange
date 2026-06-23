package com.industry_connect.marketplace_service.repository;

import com.industry_connect.marketplace_service.entity.Listing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListingRepository extends JpaRepository<Listing, Long> {
    List<Listing> findByStatus(String status);
    
    List<Listing> findByCategoryIdAndStatus(Long categoryId, String status);
    
    List<Listing> findBySellerOrgId(Long sellerOrgId);
    
    List<Listing> findBySellerOrgIdAndStatus(Long sellerOrgId, String status);

    @Query("SELECT l FROM Listing l WHERE l.status = :status AND " +
           "(LOWER(l.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(l.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Listing> searchActiveListings(@Param("keyword") String keyword, @Param("status") String status);
}
