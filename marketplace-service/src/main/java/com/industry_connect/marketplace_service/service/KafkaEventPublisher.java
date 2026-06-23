package com.industry_connect.marketplace_service.service;

import com.industry_connect.marketplace_service.event.AuditCreatedEvent;
import com.industry_connect.marketplace_service.event.ListingCreatedEvent;
import com.industry_connect.marketplace_service.event.OfferAcceptedEvent;
import com.industry_connect.marketplace_service.event.OfferPlacedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaEventPublisher.class);

    @Autowired(required = false)
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void publishListingCreated(Long listingId, Long sellerOrgId, Long categoryId) {
        ListingCreatedEvent event = new ListingCreatedEvent(listingId, sellerOrgId, categoryId);
        sendEvent("listing-events", String.valueOf(listingId), event);
        publishAudit("LISTING_CREATED");
    }

    public void publishOfferPlaced(Long offerId, Long listingId, Long buyerOrgId, Double price) {
        OfferPlacedEvent event = new OfferPlacedEvent(offerId, listingId, buyerOrgId, price);
        sendEvent("offer-events", String.valueOf(offerId), event);
        publishAudit("OFFER_PLACED");
    }

    public void publishOfferAccepted(Long offerId, Long listingId) {
        OfferAcceptedEvent event = new OfferAcceptedEvent(offerId, listingId);
        sendEvent("offer-events", String.valueOf(offerId), event);
        publishAudit("OFFER_ACCEPTED");
    }

    public void publishAudit(String action) {
        AuditCreatedEvent event = new AuditCreatedEvent("marketplace-service", action);
        sendEvent("audit-events", action, event);
    }

    private void sendEvent(String topic, String key, Object payload) {
        log.info("Attempting to publish event to topic {}: {}", topic, payload);
        if (kafkaTemplate != null) {
            try {
                kafkaTemplate.send(topic, key, payload)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.warn("Failed to publish event to topic {} due to connection issue: {}", topic, ex.getMessage());
                        } else {
                            log.info("Successfully published event to topic {} with key {}", topic, key);
                        }
                    });
            } catch (Exception e) {
                log.warn("Error invoking KafkaTemplate send for topic {}: {}", topic, e.getMessage());
            }
        } else {
            log.warn("KafkaTemplate not autowired. Event logged but not published to Kafka broker: {}", payload);
        }
    }
}
