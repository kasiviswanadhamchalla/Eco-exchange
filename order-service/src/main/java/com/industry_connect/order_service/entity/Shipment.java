package com.industry_connect.order_service.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "shipments")
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "partner_id")
    private Long partnerId;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @Column(nullable = false)
    private String status;

    public Shipment() {}

    public Shipment(Long orderId, Long partnerId, String trackingNumber, String status) {
        this.orderId = orderId;
        this.partnerId = partnerId;
        this.trackingNumber = trackingNumber;
        this.status = status;
    }

    // Getters and Setters
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
