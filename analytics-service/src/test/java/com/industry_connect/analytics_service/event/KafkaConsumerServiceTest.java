package com.industry_connect.analytics_service.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.industry_connect.analytics_service.service.AnalyticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class KafkaConsumerServiceTest {

    @Mock
    private AnalyticsService analyticsService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private KafkaConsumerService kafkaConsumerService;

    @BeforeEach
    void setUp() {
        kafkaConsumerService = new KafkaConsumerService(analyticsService, objectMapper);
    }

    @Test
    void testConsumeOrderEvent_Success() {
        String message = "{\"orderId\":900}";
        
        kafkaConsumerService.consumeOrderEvent(message);

        verify(analyticsService).processOrderCreated(900L);
    }

    @Test
    void testConsumeAuditEvent_Success() {
        String message = "{\"eventId\":\"evt-uuid\",\"service\":\"marketplace-service\",\"action\":\"LISTING_CREATED\"}";

        kafkaConsumerService.consumeAuditEvent(message);

        verify(analyticsService).processAuditEvent(message);
    }
}
