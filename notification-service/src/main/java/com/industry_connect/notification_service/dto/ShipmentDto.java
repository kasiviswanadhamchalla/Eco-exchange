package com.industry_connect.notification_service.dto;

public class ShipmentDto {
    private Long id;
    private Long orderId;
    private Long partnerId;
    private String trackingNumber;
    private String status;

    public ShipmentDto() {}

    public ShipmentDto(Long id, Long orderId, Long partnerId, String trackingNumber, String status) {
        this.id = id;
        this.orderId = orderId;
        this.partnerId = partnerId;
        this.trackingNumber = trackingNumber;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public Long getPartnerId() { return partnerId; }
    public void setPartnerId(Long partnerId) { this.partnerId = partnerId; }

    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
