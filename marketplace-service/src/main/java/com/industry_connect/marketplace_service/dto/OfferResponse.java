package com.industry_connect.marketplace_service.dto;

import com.industry_connect.marketplace_service.entity.Offer;
import java.time.LocalDateTime;

public class OfferResponse {
    private Long id;
    private Long listingId;
    private String listingTitle;
    private Long buyerOrgId;
    private Double quantity;
    private Double offeredPrice;
    private String status;
    private LocalDateTime createdAt;

    public OfferResponse() {}

    public OfferResponse(Offer offer) {
        this.id = offer.getId();
        if (offer.getListing() != null) {
            this.listingId = offer.getListing().getId();
            this.listingTitle = offer.getListing().getTitle();
        }
        this.buyerOrgId = offer.getBuyerOrgId();
        this.quantity = offer.getQuantity();
        this.offeredPrice = offer.getOfferedPrice();
        this.status = offer.getStatus();
        this.createdAt = offer.getCreatedAt();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getListingId() {
        return listingId;
    }

    public void setListingId(Long listingId) {
        this.listingId = listingId;
    }

    public String getListingTitle() {
        return listingTitle;
    }

    public void setListingTitle(String listingTitle) {
        this.listingTitle = listingTitle;
    }

    public Long getBuyerOrgId() {
        return buyerOrgId;
    }

    public void setBuyerOrgId(Long buyerOrgId) {
        this.buyerOrgId = buyerOrgId;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getOfferedPrice() {
        return offeredPrice;
    }

    public void setOfferedPrice(Double offeredPrice) {
        this.offeredPrice = offeredPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
