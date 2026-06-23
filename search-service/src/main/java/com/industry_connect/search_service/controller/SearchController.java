package com.industry_connect.search_service.controller;

import com.industry_connect.search_service.dto.CategoryDto;
import com.industry_connect.search_service.entity.SearchListing;
import com.industry_connect.search_service.service.SearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping
    public ResponseEntity<List<SearchListing>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String status) {
        List<SearchListing> results = searchService.searchListings(keyword, categoryId, minPrice, maxPrice, location, status);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/autocomplete")
    public ResponseEntity<List<String>> autocomplete(@RequestParam String keyword) {
        List<String> suggestions = searchService.autocomplete(keyword);
        return ResponseEntity.ok(suggestions);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getCategories() {
        List<CategoryDto> categories = searchService.getCategories();
        return ResponseEntity.ok(categories);
    }
}
