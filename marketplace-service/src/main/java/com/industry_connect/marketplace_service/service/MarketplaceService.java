package com.industry_connect.marketplace_service.service;

import com.industry_connect.marketplace_service.dto.*;
import com.industry_connect.marketplace_service.entity.*;
import com.industry_connect.marketplace_service.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MarketplaceService {

    private final ListingRepository listingRepository;
    private final MaterialCategoryRepository categoryRepository;
    private final OfferRepository offerRepository;
    private final KafkaEventPublisher eventPublisher;

    public MarketplaceService(ListingRepository listingRepository,
                              MaterialCategoryRepository categoryRepository,
                              OfferRepository offerRepository,
                              KafkaEventPublisher eventPublisher) {
        this.listingRepository = listingRepository;
        this.categoryRepository = categoryRepository;
        this.offerRepository = offerRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public ListingResponse createListing(ListingRequest request, Long sellerOrgId) {
        if (sellerOrgId == null) {
            throw new IllegalArgumentException("Seller organization ID must be provided");
        }

        MaterialCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Material category not found"));

        Listing listing = new Listing();
        listing.setSellerOrgId(sellerOrgId);
        listing.setCategory(category);
        listing.setTitle(request.getTitle());
        listing.setDescription(request.getDescription());
        listing.setQuantity(request.getQuantity());
        listing.setAvailableQuantity(request.getQuantity());
        listing.setUnit(request.getUnit());
        listing.setPricePerUnit(request.getPricePerUnit());
        listing.setLocation(request.getLocation());
        listing.setStatus("DRAFT"); // Default status

        if (request.getImageUrls() != null) {
            for (String url : request.getImageUrls()) {
                listing.addImage(new ListingImage(url));
            }
        }

        if (request.getCertificates() != null) {
            for (CertificateDto certDto : request.getCertificates()) {
                listing.addCertificate(new QualityCertificate(certDto.getCertificateName(), certDto.getCertificateUrl()));
            }
        }

        listing = listingRepository.save(listing);
        return new ListingResponse(listing);
    }

    public List<ListingResponse> getListings(Long categoryId, Long sellerOrgId, String keyword) {
        List<Listing> listings;

        if (keyword != null && !keyword.trim().isEmpty()) {
            listings = listingRepository.searchActiveListings(keyword.trim(), "ACTIVE");
        } else if (categoryId != null) {
            listings = listingRepository.findByCategoryIdAndStatus(categoryId, "ACTIVE");
        } else if (sellerOrgId != null) {
            // For a specific seller, return all their listings (even if DRAFT/SOLD/INACTIVE, except DELETED)
            listings = listingRepository.findBySellerOrgId(sellerOrgId).stream()
                    .filter(l -> !"DELETED".equals(l.getStatus()))
                    .collect(Collectors.toList());
        } else {
            listings = listingRepository.findByStatus("ACTIVE");
        }

        return listings.stream()
                .map(ListingResponse::new)
                .collect(Collectors.toList());
    }

    public ListingResponse getListing(Long id) {
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Listing not found"));
        
        if ("DELETED".equals(listing.getStatus())) {
            throw new RuntimeException("Listing not found");
        }

        return new ListingResponse(listing);
    }

    @Transactional
    public ListingResponse updateListing(Long id, ListingRequest request, Long orgId, String role) {
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Listing not found"));

        if ("DELETED".equals(listing.getStatus())) {
            throw new RuntimeException("Listing not found");
        }

        boolean isOwner = listing.getSellerOrgId().equals(orgId);
        boolean isAdmin = "ADMIN".equalsIgnoreCase(role) || "PLATFORM_ADMIN".equalsIgnoreCase(role);

        if (!isOwner && !isAdmin) {
            throw new RuntimeException("Unauthorized: You do not own this listing");
        }

        MaterialCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Material category not found"));

        listing.setCategory(category);
        listing.setTitle(request.getTitle());
        listing.setDescription(request.getDescription());
        
        // Handle quantity adjustment safely
        double reservedQty = listing.getQuantity() - listing.getAvailableQuantity();
        if (request.getQuantity() < reservedQty) {
            throw new IllegalArgumentException("Cannot update quantity to be less than reserved quantity: " + reservedQty);
        }
        listing.setQuantity(request.getQuantity());
        listing.setAvailableQuantity(request.getQuantity() - reservedQty);
        
        listing.setUnit(request.getUnit());
        listing.setPricePerUnit(request.getPricePerUnit());
        listing.setLocation(request.getLocation());

        // Update images (remove old, add new)
        listing.getImages().clear();
        if (request.getImageUrls() != null) {
            for (String url : request.getImageUrls()) {
                listing.addImage(new ListingImage(url));
            }
        }

        // Update certificates (remove old, add new)
        listing.getCertificates().clear();
        if (request.getCertificates() != null) {
            for (CertificateDto certDto : request.getCertificates()) {
                listing.addCertificate(new QualityCertificate(certDto.getCertificateName(), certDto.getCertificateUrl()));
            }
        }

        listing = listingRepository.save(listing);
        return new ListingResponse(listing);
    }

    @Transactional
    public ListingResponse activateListing(Long id, Long orgId, String role) {
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Listing not found"));

        if ("DELETED".equals(listing.getStatus())) {
            throw new RuntimeException("Listing not found");
        }

        boolean isOwner = listing.getSellerOrgId().equals(orgId);
        boolean isAdmin = "ADMIN".equalsIgnoreCase(role) || "PLATFORM_ADMIN".equalsIgnoreCase(role);

        if (!isOwner && !isAdmin) {
            throw new RuntimeException("Unauthorized: You do not own this listing");
        }

        listing.setStatus("ACTIVE");
        listing = listingRepository.save(listing);

        // Publish event to Kafka
        eventPublisher.publishListingCreated(listing.getId(), listing.getSellerOrgId(), listing.getCategory().getId());

        return new ListingResponse(listing);
    }

    @Transactional
    public void deleteListing(Long id, Long orgId, String role) {
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Listing not found"));

        if ("DELETED".equals(listing.getStatus())) {
            return;
        }

        boolean isOwner = listing.getSellerOrgId().equals(orgId);
        boolean isAdmin = "ADMIN".equalsIgnoreCase(role) || "PLATFORM_ADMIN".equalsIgnoreCase(role);

        if (!isOwner && !isAdmin) {
            throw new RuntimeException("Unauthorized: You do not own this listing");
        }

        listing.setStatus("DELETED");
        listingRepository.save(listing);
    }

    @Transactional
    public OfferResponse placeOffer(Long listingId, OfferRequest request, Long buyerOrgId) {
        if (buyerOrgId == null) {
            throw new IllegalArgumentException("Buyer organization ID must be provided");
        }

        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new RuntimeException("Listing not found"));

        if (!"ACTIVE".equals(listing.getStatus())) {
            throw new RuntimeException("Cannot place offer: Listing is not active");
        }

        if (listing.getSellerOrgId().equals(buyerOrgId)) {
            throw new RuntimeException("Cannot place offer: You cannot offer on your own listing");
        }

        if (request.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        if (request.getQuantity() > listing.getAvailableQuantity()) {
            throw new IllegalArgumentException("Cannot place offer: Requested quantity exceeds available quantity (" + listing.getAvailableQuantity() + ")");
        }

        Offer offer = new Offer();
        offer.setListing(listing);
        offer.setBuyerOrgId(buyerOrgId);
        offer.setQuantity(request.getQuantity());
        offer.setOfferedPrice(request.getOfferedPrice());
        offer.setStatus("PENDING");

        offer = offerRepository.save(offer);

        // Publish event to Kafka
        eventPublisher.publishOfferPlaced(offer.getId(), listing.getId(), buyerOrgId, offer.getOfferedPrice());

        return new OfferResponse(offer);
    }

    public List<OfferResponse> getOffersForListing(Long listingId, Long orgId, String role) {
        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new RuntimeException("Listing not found"));

        boolean isOwner = listing.getSellerOrgId().equals(orgId);
        boolean isAdmin = "ADMIN".equalsIgnoreCase(role) || "PLATFORM_ADMIN".equalsIgnoreCase(role);

        if (!isOwner && !isAdmin) {
            throw new RuntimeException("Unauthorized: You do not own this listing");
        }

        return offerRepository.findByListingId(listingId).stream()
                .map(OfferResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public OfferResponse acceptOffer(Long offerId, Long sellerOrgId) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Offer not found"));

        if (!"PENDING".equals(offer.getStatus())) {
            throw new RuntimeException("Cannot accept offer: Offer is already " + offer.getStatus());
        }

        Listing listing = offer.getListing();
        if (!listing.getSellerOrgId().equals(sellerOrgId)) {
            throw new RuntimeException("Unauthorized: You do not own the listing for this offer");
        }

        if (!"ACTIVE".equals(listing.getStatus())) {
            throw new RuntimeException("Cannot accept offer: Listing is not active");
        }

        if (offer.getQuantity() > listing.getAvailableQuantity()) {
            throw new IllegalArgumentException("Cannot accept offer: Offer quantity exceeds listing's available quantity (" + listing.getAvailableQuantity() + ")");
        }

        // Accept offer
        offer.setStatus("ACCEPTED");
        offer = offerRepository.save(offer);

        // Update listing available quantity
        double newAvailableQty = listing.getAvailableQuantity() - offer.getQuantity();
        listing.setAvailableQuantity(newAvailableQty);
        if (newAvailableQty <= 0) {
            listing.setStatus("SOLD");
        }
        listingRepository.save(listing);

        // Proactively reject pending offers that exceed the new available quantity
        List<Offer> otherOffers = offerRepository.findByListingId(listing.getId());
        for (Offer other : otherOffers) {
            if ("PENDING".equals(other.getStatus()) && other.getQuantity() > newAvailableQty) {
                other.setStatus("REJECTED");
                offerRepository.save(other);
            }
        }

        // Publish event to Kafka
        eventPublisher.publishOfferAccepted(offer.getId(), listing.getId());

        return new OfferResponse(offer);
    }

    @Transactional
    public OfferResponse rejectOffer(Long offerId, Long sellerOrgId) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Offer not found"));

        if (!"PENDING".equals(offer.getStatus())) {
            throw new RuntimeException("Cannot reject offer: Offer is already " + offer.getStatus());
        }

        Listing listing = offer.getListing();
        if (!listing.getSellerOrgId().equals(sellerOrgId)) {
            throw new RuntimeException("Unauthorized: You do not own the listing for this offer");
        }

        offer.setStatus("REJECTED");
        offer = offerRepository.save(offer);

        return new OfferResponse(offer);
    }

    public OfferResponse getOffer(Long id) {
        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Offer not found with id: " + id));
        return new OfferResponse(offer);
    }
}
