ListingCreatedEvent
{
  "listingId": 100,
  "sellerOrgId": 50,
  "categoryId": 1
}

OfferPlacedEvent
{
  "offerId": 200,
  "listingId": 100,
  "buyerOrgId": 30,
  "price": 50000
}


OfferAcceptedEvent
{
  "offerId": 200,
  "listingId": 100
}

OrderCreatedEvent
{
  "orderId": 900
}

ShipmentRequestedEvent
{
  "orderId": 900
}

ShipmentDeliveredEvent
{
  "shipmentId": 555
}

NotificationRequestedEvent
{
  "email": "abc@company.com",
  "subject": "Offer Accepted"
}

AuditCreatedEvent
{
  "service": "marketplace-service",
  "action": "LISTING_CREATED"
}

