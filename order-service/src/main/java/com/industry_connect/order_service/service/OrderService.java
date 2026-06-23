package com.industry_connect.order_service.service;

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
