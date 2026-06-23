package com.industry_connect.order_service.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "listing_id", nullable = false)
    private Long listingId;

    @Column(name = "buyer_org_id", nullable = false)
    private Long buyerOrgId;

    @Column(name = "seller_org_id", nullable = false)
    private Long sellerOrgId;

    @Column(name = "offer_id")
    private Long offerId;

    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;

    @Column(nullable = false)
    private String status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Order() {}

    public Order(Long listingId, Long buyerOrgId, Long sellerOrgId, Long offerId, Double totalAmount, String status) {
        this.listingId = listingId;
        this.buyerOrgId = buyerOrgId;
        this.sellerOrgId = sellerOrgId;
        this.offerId = offerId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

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
