package com.industry_connect.order_service.dto;

public class OrderRequest {

    private Long listingId;
    private Long buyerOrgId;
    private Long sellerOrgId;
    private Long offerId;
    private Double totalAmount;

    public OrderRequest() {}

    public OrderRequest(Long listingId, Long buyerOrgId, Long sellerOrgId, Long offerId, Double totalAmount) {
        this.listingId = listingId;
        this.buyerOrgId = buyerOrgId;
        this.sellerOrgId = sellerOrgId;
        this.offerId = offerId;
        this.totalAmount = totalAmount;
    }

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
}
