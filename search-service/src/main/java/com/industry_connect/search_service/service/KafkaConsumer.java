package com.industry_connect.search_service.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.industry_connect.search_service.client.MarketplaceFeignClient;
import com.industry_connect.search_service.dto.CertificateDto;
import com.industry_connect.search_service.dto.ListingResponse;
import com.industry_connect.search_service.entity.SearchCertificate;
import com.industry_connect.search_service.entity.SearchListing;
import com.industry_connect.search_service.repository.SearchListingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    private final SearchListingRepository searchListingRepository;
    private final MarketplaceFeignClient marketplaceFeignClient;
    private final ObjectMapper objectMapper;

    public KafkaConsumer(SearchListingRepository searchListingRepository,
                         MarketplaceFeignClient marketplaceFeignClient,
                         ObjectMapper objectMapper) {
        this.searchListingRepository = searchListingRepository;
        this.marketplaceFeignClient = marketplaceFeignClient;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "listing-events", groupId = "search-group")
    public void consumeListingEvent(String message) {
        log.info("Received message on topic listing-events: {}", message);
        try {
            JsonNode node = objectMapper.readTree(message);
            Long listingId = null;
            if (node.has("listingId")) {
                listingId = node.get("listingId").asLong();
            }

            if (listingId != null) {
                syncListing(listingId);
            } else {
                log.warn("Listing ID not found in listing event: {}", message);
            }
        } catch (Exception e) {
            log.error("Error processing listing event: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "offer-events", groupId = "search-group")
    public void consumeOfferEvent(String message) {
        log.info("Received message on topic offer-events: {}", message);
        try {
            JsonNode node = objectMapper.readTree(message);
            Long listingId = null;
            if (node.has("listingId")) {
                listingId = node.get("listingId").asLong();
            }

            if (listingId != null) {
                syncListing(listingId);
            } else {
                log.warn("Listing ID not found in offer event: {}", message);
            }
        } catch (Exception e) {
            log.error("Error processing offer event: {}", e.getMessage(), e);
        }
    }

    private void syncListing(Long listingId) {
        log.info("Syncing listing ID {} with Marketplace Service...", listingId);
        try {
            ListingResponse response = marketplaceFeignClient.getListing(listingId);
            if (response != null) {
                SearchListing searchListing = new SearchListing();
                searchListing.setId(response.getId());
                searchListing.setSellerOrgId(response.getSellerOrgId());
                searchListing.setCategoryId(response.getCategoryId());
                searchListing.setCategoryName(response.getCategoryName());
                searchListing.setTitle(response.getTitle());
                searchListing.setDescription(response.getDescription());
                searchListing.setQuantity(response.getQuantity());
                searchListing.setAvailableQuantity(response.getAvailableQuantity());
                searchListing.setUnit(response.getUnit());
                searchListing.setPricePerUnit(response.getPricePerUnit());
                searchListing.setLocation(response.getLocation());
                searchListing.setStatus(response.getStatus());
                searchListing.setCreatedAt(response.getCreatedAt());

                // Map images
                List<String> images = new ArrayList<>();
                if (response.getImageUrls() != null) {
                    images.addAll(response.getImageUrls());
                }
                searchListing.setImageUrls(images);

                // Map certificates
                List<SearchCertificate> certs = new ArrayList<>();
                if (response.getCertificates() != null) {
                    for (CertificateDto dto : response.getCertificates()) {
                        certs.add(new SearchCertificate(dto.getCertificateName(), dto.getCertificateUrl()));
                    }
                }
                searchListing.setCertificates(certs);

                searchListingRepository.save(searchListing);
                log.info("Successfully indexed/updated listing ID {}", listingId);
            }
        } catch (Exception e) {
            log.warn("Could not sync listing ID {} from marketplace-service (it may have been deleted). Error: {}", 
                    listingId, e.getMessage());
            
            // Mark existing search listing as DELETED if it exists in local index db
            Optional<SearchListing> existing = searchListingRepository.findById(listingId);
            if (existing.isPresent()) {
                SearchListing listing = existing.get();
                listing.setStatus("DELETED");
                searchListingRepository.save(listing);
                log.info("Marked existing search listing ID {} as DELETED in local index", listingId);
            }
        }
    }
}
