package com.industry_connect.order_service.dto;

public class ShipmentRequest {

    private Long partnerId;
    private String trackingNumber;

    public ShipmentRequest() {}

    public ShipmentRequest(Long partnerId, String trackingNumber) {
        this.partnerId = partnerId;
        this.trackingNumber = trackingNumber;
    }

    public Long getPartnerId() { return partnerId; }
    public void setPartnerId(Long partnerId) { this.partnerId = partnerId; }

    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }
}
