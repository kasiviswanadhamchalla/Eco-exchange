package com.industry_connect.search_service.service;

import com.industry_connect.search_service.dto.CategoryDto;
import com.industry_connect.search_service.entity.SearchListing;
import com.industry_connect.search_service.repository.SearchListingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    private final SearchListingRepository searchListingRepository;

    public SearchService(SearchListingRepository searchListingRepository) {
        this.searchListingRepository = searchListingRepository;
    }

    public List<SearchListing> searchListings(String keyword, Long categoryId, Double minPrice, Double maxPrice, String location, String status) {
        // Default to ACTIVE listings if no status is specified
        String searchStatus = (status == null || status.trim().isEmpty()) ? "ACTIVE" : status;
        
        // Clean keyword
        String cleanKeyword = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null;
        String cleanLocation = (location != null && !location.trim().isEmpty()) ? location.trim() : null;

        return searchListingRepository.searchListings(cleanKeyword, categoryId, minPrice, maxPrice, cleanLocation, searchStatus);
    }

    public List<String> autocomplete(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return List.of();
        }
        return searchListingRepository.autocompleteTitle(keyword.trim());
    }

    public List<CategoryDto> getCategories() {
        return searchListingRepository.findUniqueCategories();
    }
}
