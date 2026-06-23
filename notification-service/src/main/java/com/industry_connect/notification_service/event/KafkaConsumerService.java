package com.industry_connect.notification_service.event;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.industry_connect.notification_service.client.IdentityFeignClient;
import com.industry_connect.notification_service.client.MarketplaceFeignClient;
import com.industry_connect.notification_service.client.OrderFeignClient;
import com.industry_connect.notification_service.dto.ListingResponse;
import com.industry_connect.notification_service.dto.OfferResponse;
import com.industry_connect.notification_service.dto.OrderDto;
import com.industry_connect.notification_service.dto.ShipmentDto;
import com.industry_connect.notification_service.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private MarketplaceFeignClient marketplaceFeignClient;

    @Autowired
    private IdentityFeignClient identityFeignClient;

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    private ObjectMapper objectMapper;

    // Existing generic notification requests listener
    @KafkaListener(topics = "notification-events", groupId = "notification-group")
    public void consumeNotificationEvent(NotificationRequestedEvent event) {
        logger.info("Received notification request via Kafka: email={}, subject={}", event.getEmail(), event.getSubject());
        
        String subject = event.getSubject() != null ? event.getSubject() : "Notification";
        String message = event.getMessage() != null ? event.getMessage() : "Details: " + subject;
        Long userId = event.getUserId();

        // Simulate sending email
        notificationService.sendEmail(event.getEmail(), subject, message);

        // Store notification in database if userId is provided
        if (userId != null) {
            notificationService.createNotification(userId, subject, message);
        } else {
            logger.warn("Skipping DB save for notification due to missing userId for email={}", event.getEmail());
        }
    }

    // New business event listener: Offer Placed or Offer Accepted
    @KafkaListener(topics = "offer-events", groupId = "notification-group")
    public void consumeOfferEvent(String message) {
        logger.info("Notification Service received offer event message: {}", message);
        try {
            JsonNode node = objectMapper.readTree(message);
            if (node.has("offerId")) {
                Long offerId = node.get("offerId").asLong();
                OfferResponse offer = marketplaceFeignClient.getOffer(offerId);
                ListingResponse listing = marketplaceFeignClient.getListing(offer.getListingId());

                if ("ACCEPTED".equals(offer.getStatus())) {
                    // Fetch organization admin emails
                    String buyerEmail = identityFeignClient.getOrganizationAdminEmail(offer.getBuyerOrgId());
                    String sellerEmail = identityFeignClient.getOrganizationAdminEmail(listing.getSellerOrgId());

                    // Notify Buyer
                    String buyerSubject = "Offer Accepted for Listing: " + listing.getTitle();
                    String buyerBody = String.format("Dear Partner, your offer for listing '%s' has been accepted. Quantity: %.2f, Price: %.2f.",
                            listing.getTitle(), offer.getQuantity(), offer.getOfferedPrice());
                    notificationService.sendEmail(buyerEmail, buyerSubject, buyerBody);
                    notificationService.createNotification(offer.getBuyerOrgId(), buyerSubject, buyerBody);

                    // Notify Seller
                    String sellerSubject = "Listing Sold: " + listing.getTitle();
                    String sellerBody = String.format("Dear Partner, you have accepted the offer for listing '%s'. Quantity: %.2f, Price: %.2f. Order generation is in progress.",
                            listing.getTitle(), offer.getQuantity(), offer.getOfferedPrice());
                    notificationService.sendEmail(sellerEmail, sellerSubject, sellerBody);
                    notificationService.createNotification(listing.getSellerOrgId(), sellerSubject, sellerBody);

                } else if ("PENDING".equals(offer.getStatus())) {
                    // Notify Seller of new bid
                    String sellerEmail = identityFeignClient.getOrganizationAdminEmail(listing.getSellerOrgId());
                    String subject = "New Offer Placed on Listing: " + listing.getTitle();
                    String body = String.format("Dear Partner, a new offer of %.2f per unit has been placed on your listing '%s'. Quantity: %.2f.",
                            offer.getOfferedPrice(), listing.getTitle(), offer.getQuantity());
                    notificationService.sendEmail(sellerEmail, subject, body);
                    notificationService.createNotification(listing.getSellerOrgId(), subject, body);
                }
            }
        } catch (Exception e) {
            logger.error("Error processing offer event for notification: {}", e.getMessage(), e);
        }
    }

    // New business event listener: Shipment Requested, Assigned, or Delivered
    @KafkaListener(topics = "shipment-events", groupId = "notification-group")
    public void consumeShipmentEvent(String message) {
        logger.info("Notification Service received shipment event message: {}", message);
        try {
            JsonNode node = objectMapper.readTree(message);
            Long orderId = null;
            Long shipmentId = null;

            if (node.has("orderId")) {
                orderId = node.get("orderId").asLong();
            }
            if (node.has("shipmentId")) {
                shipmentId = node.get("shipmentId").asLong();
            }

            OrderDto order = null;
            ShipmentDto shipment = null;

            if (shipmentId != null) {
                shipment = orderFeignClient.getShipment(shipmentId);
                orderId = shipment.getOrderId();
            }

            if (orderId != null) {
                order = orderFeignClient.getOrder(orderId);
                if (shipment == null) {
                    // Try getting shipment by order ID, or standard fallback if needed.
                    // (Here we fallback to fetching order-related details).
                }
            }

            if (order != null) {
                String buyerEmail = identityFeignClient.getOrganizationAdminEmail(order.getBuyerOrgId());
                String sellerEmail = identityFeignClient.getOrganizationAdminEmail(order.getSellerOrgId());

                if (shipment != null && "DELIVERED".equals(shipment.getStatus())) {
                    // Notify Buyer of delivery
                    String buyerSubject = "Shipment Delivered - Order ID: " + order.getId();
                    String buyerBody = String.format("Dear Partner, your shipment for Order ID: %d has been successfully delivered.", order.getId());
                    notificationService.sendEmail(buyerEmail, buyerSubject, buyerBody);
                    notificationService.createNotification(order.getBuyerOrgId(), buyerSubject, buyerBody);

                    // Notify Seller of delivery
                    String sellerSubject = "Shipment Delivered - Order ID: " + order.getId();
                    String sellerBody = String.format("Dear Partner, the shipment for Order ID: %d has been confirmed as delivered to the buyer.", order.getId());
                    notificationService.sendEmail(sellerEmail, sellerSubject, sellerBody);
                    notificationService.createNotification(order.getSellerOrgId(), sellerSubject, sellerBody);
                } else {
                    // Default to Shipment Scheduled / Requested
                    String buyerSubject = "Shipment Initiated - Order ID: " + order.getId();
                    String buyerBody = String.format("Dear Partner, the logistics shipment for your Order ID: %d has been scheduled/initiated.", order.getId());
                    notificationService.sendEmail(buyerEmail, buyerSubject, buyerBody);
                    notificationService.createNotification(order.getBuyerOrgId(), buyerSubject, buyerBody);

                    String sellerSubject = "Shipment Requested - Order ID: " + order.getId();
                    String sellerBody = String.format("Dear Partner, the shipment request for Order ID: %d has been sent to the logistics team.", order.getId());
                    notificationService.sendEmail(sellerEmail, sellerSubject, sellerBody);
                    notificationService.createNotification(order.getSellerOrgId(), sellerSubject, sellerBody);
                }
            }
        } catch (Exception e) {
            logger.error("Error processing shipment event for notification: {}", e.getMessage(), e);
        }
    }
}
