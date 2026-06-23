package com.industry_connect.analytics_service.event;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.industry_connect.analytics_service.service.AnalyticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerService.class);

    private final AnalyticsService analyticsService;
    private final ObjectMapper objectMapper;

    public KafkaConsumerService(AnalyticsService analyticsService, ObjectMapper objectMapper) {
        this.analyticsService = analyticsService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "order-events", groupId = "analytics-group")
    public void consumeOrderEvent(String message) {
        log.info("Received message on topic order-events: {}", message);
        try {
            JsonNode node = objectMapper.readTree(message);
            Long orderId = null;
            if (node.has("orderId")) {
                orderId = node.get("orderId").asLong();
            }

            if (orderId != null) {
                analyticsService.processOrderCreated(orderId);
            } else {
                log.warn("Order ID not found in order event payload: {}", message);
            }
        } catch (Exception e) {
            log.error("Error processing order event: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "audit-events", groupId = "analytics-group")
    public void consumeAuditEvent(String message) {
        log.info("Received message on topic audit-events: {}", message);
        try {
            analyticsService.processAuditEvent(message);
        } catch (Exception e) {
            log.error("Error processing audit event: {}", e.getMessage(), e);
        }
    }
}
