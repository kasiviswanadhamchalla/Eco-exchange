package com.industry_connect.order_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EventPublisher {

    private static final Logger log = LoggerFactory.getLogger(EventPublisher.class);

    @Autowired(required = false)
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void publishOrderCreated(Long orderId) {
        Map<String, Object> event = new HashMap<>();
        event.put("orderId", orderId);
        sendEvent("order-events", event);
    }

    public void publishShipmentRequested(Long orderId) {
        Map<String, Object> event = new HashMap<>();
        event.put("orderId", orderId);
        sendEvent("shipment-events", event);
    }

    public void publishShipmentDelivered(Long shipmentId) {
        Map<String, Object> event = new HashMap<>();
        event.put("shipmentId", shipmentId);
        sendEvent("shipment-events", event);
    }

    private void sendEvent(String topic, Object payload) {
        log.info("Publishing event to topic {}: {}", topic, payload);
        if (kafkaTemplate != null) {
            try {
                kafkaTemplate.send(topic, payload).whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish event to {} due to: {}", topic, ex.getMessage());
                    } else {
                        log.info("Successfully published event to {} at offset {}", topic, result.getRecordMetadata().offset());
                    }
                });
            } catch (Exception e) {
                log.error("Error sending message to Kafka: {}", e.getMessage());
            }
        } else {
            log.warn("KafkaTemplate not autowired. Skipping Kafka send for topic {}. Payload: {}", topic, payload);
        }
    }
}
