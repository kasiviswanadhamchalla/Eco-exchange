package com.industry_connect.search_service.service;

import com.industry_connect.search_service.dto.CategoryDto;
import com.industry_connect.search_service.entity.SearchListing;
import com.industry_connect.search_service.repository.SearchListingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SearchServiceTest {

    @Mock
    private SearchListingRepository searchListingRepository;

    @InjectMocks
    private SearchService searchService;

    private SearchListing listing1;
    private SearchListing listing2;

    @BeforeEach
    void setUp() {
        listing1 = new SearchListing();
        listing1.setId(10L);
        listing1.setSellerOrgId(100L);
        listing1.setCategoryId(1L);
        listing1.setCategoryName("Steel Scrap");
        listing1.setTitle("Steel Pipes");
        listing1.setDescription("Heavy steel pipes");
        listing1.setQuantity(20.0);
        listing1.setAvailableQuantity(20.0);
        listing1.setUnit("TONS");
        listing1.setPricePerUnit(500.0);
        listing1.setLocation("Delhi");
        listing1.setStatus("ACTIVE");

        listing2 = new SearchListing();
        listing2.setId(20L);
        listing2.setSellerOrgId(101L);
        listing2.setCategoryId(2L);
        listing2.setCategoryName("Wood Waste");
        listing2.setTitle("Wooden Pallets");
        listing2.setDescription("Discarded wooden pallets");
        listing2.setQuantity(10.0);
        listing2.setAvailableQuantity(10.0);
        listing2.setUnit("PCS");
        listing2.setPricePerUnit(10.0);
        listing2.setLocation("Mumbai");
        listing2.setStatus("ACTIVE");
    }

    @Test
    void testSearchListingsDefaultStatus() {
        when(searchListingRepository.searchListings("Steel", 1L, 100.0, 600.0, "Delhi", "ACTIVE"))
                .thenReturn(List.of(listing1));

        List<SearchListing> results = searchService.searchListings("Steel", 1L, 100.0, 600.0, "Delhi", null);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("Steel Pipes", results.get(0).getTitle());
        verify(searchListingRepository, times(1))
                .searchListings("Steel", 1L, 100.0, 600.0, "Delhi", "ACTIVE");
    }

    @Test
    void testSearchListingsCustomStatus() {
        when(searchListingRepository.searchListings(null, null, null, null, null, "SOLD"))
                .thenReturn(List.of());

        List<SearchListing> results = searchService.searchListings(null, null, null, null, null, "SOLD");

        assertNotNull(results);
        assertTrue(results.isEmpty());
        verify(searchListingRepository, times(1))
                .searchListings(null, null, null, null, null, "SOLD");
    }

    @Test
    void testAutocomplete() {
        when(searchListingRepository.autocompleteTitle("Wood"))
                .thenReturn(List.of("Wooden Pallets"));

        List<String> results = searchService.autocomplete("Wood");

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("Wooden Pallets", results.get(0));
        verify(searchListingRepository, times(1)).autocompleteTitle("Wood");
    }

    @Test
    void testAutocompleteEmpty() {
        List<String> results = searchService.autocomplete("");
        assertNotNull(results);
        assertTrue(results.isEmpty());
        verify(searchListingRepository, never()).autocompleteTitle(anyString());
    }

    @Test
    void testGetCategories() {
        CategoryDto c1 = new CategoryDto(1L, "Steel Scrap");
        CategoryDto c2 = new CategoryDto(2L, "Wood Waste");
        
        when(searchListingRepository.findUniqueCategories()).thenReturn(List.of(c1, c2));

        List<CategoryDto> results = searchService.getCategories();

        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals("Steel Scrap", results.get(0).getName());
        assertEquals("Wood Waste", results.get(1).getName());
        verify(searchListingRepository, times(1)).findUniqueCategories();
    }
}
