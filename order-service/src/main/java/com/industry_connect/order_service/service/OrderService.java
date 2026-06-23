package com.industry_connect.order_service.service;

import com.industry_connect.order_service.client.MarketplaceFeignClient;
import com.industry_connect.order_service.dto.ListingResponse;
import com.industry_connect.order_service.dto.OfferResponse;
import com.industry_connect.order_service.dto.OrderRequest;
import com.industry_connect.order_service.entity.Order;
import com.industry_connect.order_service.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EventPublisher eventPublisher;

    @Autowired
    private MarketplaceFeignClient marketplaceFeignClient;

    @Transactional
    public Order createOrder(OrderRequest request, Long userOrgId) {
        if (request.getListingId() == null) {
            throw new IllegalArgumentException("Listing ID is required");
        }
        if (request.getTotalAmount() == null || request.getTotalAmount() <= 0) {
            throw new IllegalArgumentException("Total amount must be greater than zero");
        }

        // If userOrgId is provided, enforce that buyerOrgId matches it.
        Long buyerOrgId = request.getBuyerOrgId();
        if (buyerOrgId == null) {
            if (userOrgId != null) {
                buyerOrgId = userOrgId;
            } else {
                throw new IllegalArgumentException("Buyer Organization ID is required");
            }
        } else if (userOrgId != null && !userOrgId.equals(buyerOrgId)) {
            throw new IllegalArgumentException("Buyer Organization ID mismatch with user context");
        }

        // Synchronously validate listing via Feign client
        ListingResponse listing;
        try {
            listing = marketplaceFeignClient.getListing(request.getListingId());
        } catch (feign.FeignException.NotFound e) {
            throw new IllegalArgumentException("Listing not found with id: " + request.getListingId());
        } catch (Exception e) {
            throw new RuntimeException("Error validating listing with Marketplace service: " + e.getMessage());
        }

        if (listing == null) {
            throw new IllegalArgumentException("Listing not found with id: " + request.getListingId());
        }

        // Validate seller organization ID matches the listing
        if (!listing.getSellerOrgId().equals(request.getSellerOrgId())) {
            throw new IllegalArgumentException("Seller Organization ID does not match the listing owner");
        }

        // If offer ID is provided, validate the offer via Feign client
        if (request.getOfferId() != null) {
            OfferResponse offer;
            try {
                offer = marketplaceFeignClient.getOffer(request.getOfferId());
            } catch (feign.FeignException.NotFound e) {
                throw new IllegalArgumentException("Offer not found with id: " + request.getOfferId());
            } catch (Exception e) {
                throw new RuntimeException("Error validating offer with Marketplace service: " + e.getMessage());
            }

            if (offer == null) {
                throw new IllegalArgumentException("Offer not found with id: " + request.getOfferId());
            }

            if (!offer.getListingId().equals(request.getListingId())) {
                throw new IllegalArgumentException("Offer does not belong to the specified listing");
            }

            if (!offer.getBuyerOrgId().equals(buyerOrgId)) {
                throw new IllegalArgumentException("Offer buyer organization does not match the buyer context");
            }

            if (!"ACCEPTED".equals(offer.getStatus())) {
                throw new IllegalArgumentException("Cannot create order: Offer is not in ACCEPTED status");
            }
        }

        Order order = new Order(
                request.getListingId(),
                buyerOrgId,
                request.getSellerOrgId(),
                request.getOfferId(),
                request.getTotalAmount(),
                "PENDING"
        );

        Order savedOrder = orderRepository.save(order);

        // Publish OrderCreatedEvent
        eventPublisher.publishOrderCreated(savedOrder.getId());

        return savedOrder;
    }

    public Optional<Order> getOrder(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> getMyOrders(Long orgId) {
        if (orgId == null) {
            throw new IllegalArgumentException("Organization ID is required to fetch orders");
        }
        return orderRepository.findByOrganizationId(orgId);
    }
}
