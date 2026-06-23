package com.industry_connect.marketplace_service.controller;

import com.industry_connect.marketplace_service.config.UserPrincipal;
import com.industry_connect.marketplace_service.dto.*;
import com.industry_connect.marketplace_service.service.IdempotencyService;
import com.industry_connect.marketplace_service.service.MarketplaceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MarketplaceControllerTest {

    @Mock
    private MarketplaceService marketplaceService;

    @Mock
    private IdempotencyService idempotencyService;

    @InjectMocks
    private MarketplaceController marketplaceController;

    @Test
    void testCreateListing() {
        ListingRequest request = new ListingRequest();
        ListingResponse mockResponse = new ListingResponse();
        mockResponse.setId(10L);

        UserPrincipal principal = new UserPrincipal(1L, "user@example.com", 100L, "USER");

        when(marketplaceService.createListing(eq(request), eq(100L))).thenReturn(mockResponse);

        ResponseEntity<ListingResponse> response = marketplaceController.createListing(request, principal);

        assertNotNull(response);
        assertEquals(201, response.getStatusCode().value());
        assertEquals(10L, response.getBody().getId());
    }

    @Test
    void testGetListings() {
        ListingResponse mockResponse = new ListingResponse();
        mockResponse.setId(10L);
        List<ListingResponse> mockList = Collections.singletonList(mockResponse);

        when(marketplaceService.getListings(null, null, "Steel")).thenReturn(mockList);

        ResponseEntity<List<ListingResponse>> response = marketplaceController.getListings(null, null, "Steel");

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
        assertEquals(10L, response.getBody().get(0).getId());
    }

    @Test
    void testPlaceOfferWithIdempotencyCached() {
        OfferRequest request = new OfferRequest();
        UserPrincipal principal = new UserPrincipal(1L, "user@example.com", 200L, "USER");
        
        OfferResponse cachedResponse = new OfferResponse();
        cachedResponse.setId(300L);

        when(idempotencyService.get("key123")).thenReturn(Optional.of(new com.industry_connect.marketplace_service.entity.IdempotencyKey()));
        when(idempotencyService.parseResponse(any(), eq(OfferResponse.class))).thenReturn(cachedResponse);

        ResponseEntity<?> response = marketplaceController.placeOffer(10L, request, "key123", principal);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        OfferResponse body = (OfferResponse) response.getBody();
        assertEquals(300L, body.getId());
        verifyNoInteractions(marketplaceService);
    }

    @Test
    void testGetOffer_Success() {
        OfferResponse mockResponse = new OfferResponse();
        mockResponse.setId(200L);
        mockResponse.setListingId(10L);

        when(marketplaceService.getOffer(200L)).thenReturn(mockResponse);

        ResponseEntity<OfferResponse> response = marketplaceController.getOffer(200L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(200L, response.getBody().getId());
        assertEquals(10L, response.getBody().getListingId());
        verify(marketplaceService, times(1)).getOffer(200L);
    }
}
