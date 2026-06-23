package com.industry_connect.analytics_service.dto;

import java.time.LocalDateTime;

public class OrderResponse {
    private Long id;
    private Long listingId;
    private Long buyerOrgId;
    private Long sellerOrgId;
    private Long offerId;
    private Double totalAmount;
    private String status;
    private LocalDateTime createdAt;

    public OrderResponse() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getListingId() { return listingId; }
    public void setListingId(Long listingId) { this.listingId = listingId; }

    public Long getBuyerOrgId() { return buyerOrgId; }
    public void setBuyerOrgId(Long buyerOrgId) { this.buyerOrgId = buyerOrgId; }

    public Long getSellerOrgId() { return sellerOrgId; }
    public void setSellerOrgId(Long sellerOrgId) { this.sellerOrgId = sellerOrgId; }

    public Long getOfferId() { return offerId; }
    public void setOfferId(Long offerId) { this.offerId = offerId; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
