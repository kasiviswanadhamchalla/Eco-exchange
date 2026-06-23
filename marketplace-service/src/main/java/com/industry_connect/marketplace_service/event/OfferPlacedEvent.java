package com.industry_connect.marketplace_service.event;

public class OfferPlacedEvent {
    private Long offerId;
    private Long listingId;
    private Long buyerOrgId;
    private Double price;

    public OfferPlacedEvent() {}

    public OfferPlacedEvent(Long offerId, Long listingId, Long buyerOrgId, Double price) {
        this.offerId = offerId;
        this.listingId = listingId;
        this.buyerOrgId = buyerOrgId;
        this.price = price;
    }

    public Long getOfferId() {
        return offerId;
    }

    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }

    public Long getListingId() {
        return listingId;
    }

    public void setListingId(Long listingId) {
        this.listingId = listingId;
    }

    public Long getBuyerOrgId() {
        return buyerOrgId;
    }

    public void setBuyerOrgId(Long buyerOrgId) {
        this.buyerOrgId = buyerOrgId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
