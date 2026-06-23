package com.industry_connect.search_service.repository;

import com.industry_connect.search_service.dto.CategoryDto;
import com.industry_connect.search_service.entity.SearchListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchListingRepository extends JpaRepository<SearchListing, Long> {

    @Query("SELECT s FROM SearchListing s WHERE " +
           "(:status IS NULL OR s.status = :status) AND " +
           "(:categoryId IS NULL OR s.categoryId = :categoryId) AND " +
           "(:minPrice IS NULL OR s.pricePerUnit >= :minPrice) AND " +
           "(:maxPrice IS NULL OR s.pricePerUnit <= :maxPrice) AND " +
           "(:location IS NULL OR LOWER(s.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
           "(:keyword IS NULL OR LOWER(s.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(s.description) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(s.categoryName) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<SearchListing> searchListings(
            @Param("keyword") String keyword,
            @Param("categoryId") Long categoryId,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("location") String location,
            @Param("status") String status);

    @Query("SELECT DISTINCT s.title FROM SearchListing s WHERE " +
           "s.status = 'ACTIVE' AND LOWER(s.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<String> autocompleteTitle(@Param("keyword") String keyword);

    @Query("SELECT DISTINCT new com.industry_connect.search_service.dto.CategoryDto(s.categoryId, s.categoryName) " +
           "FROM SearchListing s WHERE s.categoryId IS NOT NULL AND s.categoryName IS NOT NULL AND s.status = 'ACTIVE'")
    List<CategoryDto> findUniqueCategories();
}
