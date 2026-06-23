package com.industry_connect.marketplace_service.controller;

import com.industry_connect.marketplace_service.config.UserPrincipal;
import com.industry_connect.marketplace_service.dto.*;
import com.industry_connect.marketplace_service.entity.IdempotencyKey;
import com.industry_connect.marketplace_service.service.IdempotencyService;
import com.industry_connect.marketplace_service.service.MarketplaceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class MarketplaceController {

    private final MarketplaceService marketplaceService;
    private final IdempotencyService idempotencyService;

    public MarketplaceController(MarketplaceService marketplaceService, IdempotencyService idempotencyService) {
        this.marketplaceService = marketplaceService;
        this.idempotencyService = idempotencyService;
    }

    // LISTINGS ENDPOINTS

    @PostMapping("/listings")
    public ResponseEntity<ListingResponse> createListing(
            @RequestBody ListingRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null || principal.getOrganizationId() == null) {
            throw new RuntimeException("Unauthorized: User must be associated with an organization to create listings");
        }
        ListingResponse response = marketplaceService.createListing(request, principal.getOrganizationId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/listings")
    public ResponseEntity<List<ListingResponse>> getListings(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long sellerOrgId,
            @RequestParam(required = false) String keyword) {
        List<ListingResponse> responses = marketplaceService.getListings(categoryId, sellerOrgId, keyword);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/listings/{id}")
    public ResponseEntity<ListingResponse> getListing(@PathVariable Long id) {
        ListingResponse response = marketplaceService.getListing(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/listings/{id}")
    public ResponseEntity<ListingResponse> updateListing(
            @PathVariable Long id,
            @RequestBody ListingRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            throw new RuntimeException("Unauthorized");
        }
        ListingResponse response = marketplaceService.updateListing(id, request, principal.getOrganizationId(), principal.getRole());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/listings/{id}")
    public ResponseEntity<Void> deleteListing(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            throw new RuntimeException("Unauthorized");
        }
        marketplaceService.deleteListing(id, principal.getOrganizationId(), principal.getRole());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/listings/{id}/activate")
    public ResponseEntity<ListingResponse> activateListing(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            throw new RuntimeException("Unauthorized");
        }
        ListingResponse response = marketplaceService.activateListing(id, principal.getOrganizationId(), principal.getRole());
        return ResponseEntity.ok(response);
    }

    // OFFERS ENDPOINTS

    @PostMapping("/listings/{id}/offers")
    public ResponseEntity<?> placeOffer(
            @PathVariable Long id,
            @RequestBody OfferRequest request,
            @RequestHeader(value = "X-Idempotency-Key", required = false) String idempotencyKey,
            @AuthenticationPrincipal UserPrincipal principal) {
        
        if (principal == null || principal.getOrganizationId() == null) {
            throw new RuntimeException("Unauthorized: User must be associated with an organization to place offers");
        }

        // Apply idempotency check
        if (idempotencyKey != null && !idempotencyKey.trim().isEmpty()) {
            Optional<IdempotencyKey> existing = idempotencyService.get(idempotencyKey);
            if (existing.isPresent()) {
                OfferResponse cached = idempotencyService.parseResponse(existing.get(), OfferResponse.class);
                return ResponseEntity.ok(cached);
            }
        }

        OfferResponse response = marketplaceService.placeOffer(id, request, principal.getOrganizationId());

        if (idempotencyKey != null && !idempotencyKey.trim().isEmpty()) {
            idempotencyService.save(idempotencyKey, String.valueOf(request.hashCode()), response);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/listings/{id}/offers")
    public ResponseEntity<List<OfferResponse>> getOffersForListing(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            throw new RuntimeException("Unauthorized");
        }
        List<OfferResponse> responses = marketplaceService.getOffersForListing(id, principal.getOrganizationId(), principal.getRole());
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/offers/{id}/accept")
    public ResponseEntity<?> acceptOffer(
            @PathVariable Long id,
            @RequestHeader(value = "X-Idempotency-Key", required = false) String idempotencyKey,
            @AuthenticationPrincipal UserPrincipal principal) {

        if (principal == null || principal.getOrganizationId() == null) {
            throw new RuntimeException("Unauthorized: User must be associated with an organization to accept offers");
        }

        // Apply idempotency check
        if (idempotencyKey != null && !idempotencyKey.trim().isEmpty()) {
            Optional<IdempotencyKey> existing = idempotencyService.get(idempotencyKey);
            if (existing.isPresent()) {
                OfferResponse cached = idempotencyService.parseResponse(existing.get(), OfferResponse.class);
                return ResponseEntity.ok(cached);
            }
        }

        OfferResponse response = marketplaceService.acceptOffer(id, principal.getOrganizationId());

        if (idempotencyKey != null && !idempotencyKey.trim().isEmpty()) {
            idempotencyService.save(idempotencyKey, "", response);
        }

        return ResponseEntity.ok(response);
    }

    @PutMapping("/offers/{id}/reject")
    public ResponseEntity<OfferResponse> rejectOffer(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal) {

        if (principal == null || principal.getOrganizationId() == null) {
            throw new RuntimeException("Unauthorized: User must be associated with an organization to reject offers");
        }

        OfferResponse response = marketplaceService.rejectOffer(id, principal.getOrganizationId());
        return ResponseEntity.ok(response);
     }

    @GetMapping("/offers/{id}")
    public ResponseEntity<OfferResponse> getOffer(@PathVariable Long id) {
        OfferResponse response = marketplaceService.getOffer(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/offers/{id}")
    public ResponseEntity<OfferResponse> getOffer(@PathVariable Long id) {
        OfferResponse response = marketplaceService.getOffer(id);
        return ResponseEntity.ok(response);
    }
}
