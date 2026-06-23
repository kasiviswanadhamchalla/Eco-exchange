package com.industry_connect.order_service.dto;

public class ListingResponse {
    private Long id;
    private Long sellerOrgId;
    private Double quantity;
    private Double availableQuantity;
    private Double pricePerUnit;
    private String status;

    public ListingResponse() {}

    public ListingResponse(Long id, Long sellerOrgId, Double quantity, Double availableQuantity, Double pricePerUnit, String status) {
        this.id = id;
        this.sellerOrgId = sellerOrgId;
        this.quantity = quantity;
        this.availableQuantity = availableQuantity;
        this.pricePerUnit = pricePerUnit;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSellerOrgId() { return sellerOrgId; }
    public void setSellerOrgId(Long sellerOrgId) { this.sellerOrgId = sellerOrgId; }

    public Double getQuantity() { return quantity; }
    public void setQuantity(Double quantity) { this.quantity = quantity; }

    public Double getAvailableQuantity() { return availableQuantity; }
    public void setAvailableQuantity(Double availableQuantity) { this.availableQuantity = availableQuantity; }

    public Double getPricePerUnit() { return pricePerUnit; }
    public void setPricePerUnit(Double pricePerUnit) { this.pricePerUnit = pricePerUnit; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
