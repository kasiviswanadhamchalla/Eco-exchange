package com.industry_connect.search_service.event;

public class OfferAcceptedEvent {
    private Long offerId;
    private Long listingId;

    public OfferAcceptedEvent() {}

    public OfferAcceptedEvent(Long offerId, Long listingId) {
        this.offerId = offerId;
        this.listingId = listingId;
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
}
