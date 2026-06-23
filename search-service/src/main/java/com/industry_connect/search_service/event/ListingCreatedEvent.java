package com.industry_connect.search_service.event;

public class ListingCreatedEvent {
    private Long listingId;
    private Long sellerOrgId;
    private Long categoryId;

    public ListingCreatedEvent() {}

    public ListingCreatedEvent(Long listingId, Long sellerOrgId, Long categoryId) {
        this.listingId = listingId;
        this.sellerOrgId = sellerOrgId;
        this.categoryId = categoryId;
    }

    public Long getListingId() {
        return listingId;
    }

    public void setListingId(Long listingId) {
        this.listingId = listingId;
    }

    public Long getSellerOrgId() {
        return sellerOrgId;
    }

    public void setSellerOrgId(Long sellerOrgId) {
        this.sellerOrgId = sellerOrgId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
