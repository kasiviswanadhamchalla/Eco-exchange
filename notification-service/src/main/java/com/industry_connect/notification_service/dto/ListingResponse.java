package com.industry_connect.notification_service.dto;

public class ListingResponse {
    private Long id;
    private Long sellerOrgId;
    private String title;
    private String status;

    public ListingResponse() {}

    public ListingResponse(Long id, Long sellerOrgId, String title, String status) {
        this.id = id;
        this.sellerOrgId = sellerOrgId;
        this.title = title;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSellerOrgId() { return sellerOrgId; }
    public void setSellerOrgId(Long sellerOrgId) { this.sellerOrgId = sellerOrgId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
