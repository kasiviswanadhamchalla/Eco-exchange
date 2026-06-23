package com.industry_connect.analytics_service.dto;

public class OfferResponse {
    private Long id;
    private Long listingId;
    private Long buyerOrgId;
    private Double quantity;
    private Double offeredPrice;
    private String status;

    public OfferResponse() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getListingId() { return listingId; }
    public void setListingId(Long listingId) { this.listingId = listingId; }

    public Long getBuyerOrgId() { return buyerOrgId; }
    public void setBuyerOrgId(Long buyerOrgId) { this.buyerOrgId = buyerOrgId; }

    public Double getQuantity() { return quantity; }
    public void setQuantity(Double quantity) { this.quantity = quantity; }

    public Double getOfferedPrice() { return offeredPrice; }
    public void setOfferedPrice(Double offeredPrice) { this.offeredPrice = offeredPrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
