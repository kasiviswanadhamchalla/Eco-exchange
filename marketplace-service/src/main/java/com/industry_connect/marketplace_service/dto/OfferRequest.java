package com.industry_connect.marketplace_service.dto;

public class OfferRequest {
    private Double quantity;
    private Double offeredPrice;

    public OfferRequest() {}

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
}
