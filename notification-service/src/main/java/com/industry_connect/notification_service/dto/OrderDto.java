package com.industry_connect.notification_service.dto;

public class OrderDto {
    private Long id;
    private Long listingId;
    private Long buyerOrgId;
    private Long sellerOrgId;
    private Double totalAmount;
    private String status;

    public OrderDto() {}

    public OrderDto(Long id, Long listingId, Long buyerOrgId, Long sellerOrgId, Double totalAmount, String status) {
        this.id = id;
        this.listingId = listingId;
        this.buyerOrgId = buyerOrgId;
        this.sellerOrgId = sellerOrgId;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getListingId() { return listingId; }
    public void setListingId(Long listingId) { this.listingId = listingId; }

    public Long getBuyerOrgId() { return buyerOrgId; }
    public void setBuyerOrgId(Long buyerOrgId) { this.buyerOrgId = buyerOrgId; }

    public Long getSellerOrgId() { return sellerOrgId; }
    public void setSellerOrgId(Long sellerOrgId) { this.sellerOrgId = sellerOrgId; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
