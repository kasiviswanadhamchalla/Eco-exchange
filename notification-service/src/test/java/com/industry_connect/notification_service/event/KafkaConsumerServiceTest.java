package com.industry_connect.notification_service.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.industry_connect.notification_service.client.IdentityFeignClient;
import com.industry_connect.notification_service.client.MarketplaceFeignClient;
import com.industry_connect.notification_service.client.OrderFeignClient;
import com.industry_connect.notification_service.dto.ListingResponse;
import com.industry_connect.notification_service.dto.OfferResponse;
import com.industry_connect.notification_service.dto.OrderDto;
import com.industry_connect.notification_service.dto.ShipmentDto;
import com.industry_connect.notification_service.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KafkaConsumerServiceTest {

    @Mock
    private NotificationService notificationService;

    @Mock
    private MarketplaceFeignClient marketplaceFeignClient;

    @Mock
    private IdentityFeignClient identityFeignClient;

    @Mock
    private OrderFeignClient orderFeignClient;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private KafkaConsumerService kafkaConsumerService;

    @Test
    public void testConsumeOfferEvent_Accepted() throws Exception {
        String message = "{\"offerId\":200}";
        OfferResponse offer = new OfferResponse(200L, 100L, 30L, 5.0, 500.0, "ACCEPTED");
        ListingResponse listing = new ListingResponse(100L, 50L, "Scrap Copper", "ACTIVE");

        when(marketplaceFeignClient.getOffer(200L)).thenReturn(offer);
        when(marketplaceFeignClient.getListing(100L)).thenReturn(listing);
        when(identityFeignClient.getOrganizationAdminEmail(30L)).thenReturn("buyer@org.com");
        when(identityFeignClient.getOrganizationAdminEmail(50L)).thenReturn("seller@org.com");

        kafkaConsumerService.consumeOfferEvent(message);

        verify(notificationService, times(1)).sendEmail(eq("buyer@org.com"), anyString(), anyString());
        verify(notificationService, times(1)).sendEmail(eq("seller@org.com"), anyString(), anyString());
        verify(notificationService, times(1)).createNotification(eq(30L), anyString(), anyString());
        verify(notificationService, times(1)).createNotification(eq(50L), anyString(), anyString());
    }

    @Test
    public void testConsumeOfferEvent_Pending() throws Exception {
        String message = "{\"offerId\":200}";
        OfferResponse offer = new OfferResponse(200L, 100L, 30L, 5.0, 500.0, "PENDING");
        ListingResponse listing = new ListingResponse(100L, 50L, "Scrap Copper", "ACTIVE");

        when(marketplaceFeignClient.getOffer(200L)).thenReturn(offer);
        when(marketplaceFeignClient.getListing(100L)).thenReturn(listing);
        when(identityFeignClient.getOrganizationAdminEmail(50L)).thenReturn("seller@org.com");

        kafkaConsumerService.consumeOfferEvent(message);

        verify(notificationService, times(1)).sendEmail(eq("seller@org.com"), anyString(), anyString());
        verify(notificationService, times(1)).createNotification(eq(50L), anyString(), anyString());
        verify(notificationService, never()).sendEmail(eq("buyer@org.com"), anyString(), anyString());
    }

    @Test
    public void testConsumeShipmentEvent_Delivered() throws Exception {
        String message = "{\"shipmentId\":555}";
        ShipmentDto shipment = new ShipmentDto(555L, 900L, 12L, "TRACK-123", "DELIVERED");
        OrderDto order = new OrderDto(900L, 100L, 30L, 50L, 2500.0, "DELIVERED");

        when(orderFeignClient.getShipment(555L)).thenReturn(shipment);
        when(orderFeignClient.getOrder(900L)).thenReturn(order);
        when(identityFeignClient.getOrganizationAdminEmail(30L)).thenReturn("buyer@org.com");
        when(identityFeignClient.getOrganizationAdminEmail(50L)).thenReturn("seller@org.com");

        kafkaConsumerService.consumeShipmentEvent(message);

        verify(notificationService, times(1)).sendEmail(eq("buyer@org.com"), contains("Delivered"), anyString());
        verify(notificationService, times(1)).sendEmail(eq("seller@org.com"), contains("Delivered"), anyString());
    }
}
