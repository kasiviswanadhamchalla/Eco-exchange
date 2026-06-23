package com.industry_connect.order_service.dto;

public class AssignPartnerRequest {

    private Long partnerId;

    public AssignPartnerRequest() {}

    public AssignPartnerRequest(Long partnerId) {
        this.partnerId = partnerId;
    }

    public Long getPartnerId() { return partnerId; }
    public void setPartnerId(Long partnerId) { this.partnerId = partnerId; }
}
