package com.industry_connect.analytics_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.industry_connect.analytics_service.client.MarketplaceFeignClient;
import com.industry_connect.analytics_service.client.OrderFeignClient;
import com.industry_connect.analytics_service.entity.AuditEvent;
import com.industry_connect.analytics_service.dto.ListingResponse;
import com.industry_connect.analytics_service.dto.OfferResponse;
import com.industry_connect.analytics_service.dto.OrderResponse;
import com.industry_connect.analytics_service.entity.DailyMetric;
import com.industry_connect.analytics_service.repository.AuditEventRepository;
import com.industry_connect.analytics_service.repository.DailyMetricRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AnalyticsServiceTest {

    @Mock
    private DailyMetricRepository dailyMetricRepository;

    @Mock
    private AuditEventRepository auditEventRepository;

    @Mock
    private MarketplaceFeignClient marketplaceFeignClient;

    @Mock
    private OrderFeignClient orderFeignClient;

    private ObjectMapper objectMapper = new ObjectMapper();

    private AnalyticsService analyticsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        analyticsService = new AnalyticsService(
                dailyMetricRepository,
                auditEventRepository,
                marketplaceFeignClient,
                orderFeignClient,
                objectMapper
        );
    }

    @Test
    void testProcessOrderCreated_withOffer() {
        Long orderId = 1L;
        Long listingId = 2L;
        Long offerId = 3L;
        LocalDate date = LocalDate.of(2026, 6, 23);
        LocalDateTime dateTime = date.atStartOfDay();

        OrderResponse order = new OrderResponse();
        order.setId(orderId);
        order.setListingId(listingId);
        order.setOfferId(offerId);
        order.setTotalAmount(50000.0);
        order.setCreatedAt(dateTime);

        ListingResponse listing = new ListingResponse();
        listing.setId(listingId);
        listing.setCategoryName("Steel Scrap");
        listing.setUnit("Tons");

        OfferResponse offer = new OfferResponse();
        offer.setId(offerId);
        offer.setQuantity(20.0);

        when(orderFeignClient.getOrder(orderId)).thenReturn(order);
        when(marketplaceFeignClient.getListing(listingId)).thenReturn(listing);
        when(marketplaceFeignClient.getOffer(offerId)).thenReturn(offer);
        when(dailyMetricRepository.findByDate(date)).thenReturn(Optional.empty());

        analyticsService.processOrderCreated(orderId);

        ArgumentCaptor<DailyMetric> metricCaptor = ArgumentCaptor.forClass(DailyMetric.class);
        verify(dailyMetricRepository).save(metricCaptor.capture());

        DailyMetric savedMetric = metricCaptor.getValue();
        assertEquals(date, savedMetric.getDate());
        assertEquals(20.0, savedMetric.getWasteReusedTons());
        // CO2 factor for steel scrap is 1.5, so 20.0 * 1.5 = 30.0
        assertEquals(30.0, savedMetric.getCo2Saved());
        assertEquals(50000.0, savedMetric.getRevenue());
        assertEquals(1, savedMetric.getOrders());
    }

    @Test
    void testProcessOrderCreated_withKgsUnit() {
        Long orderId = 10L;
        Long listingId = 20L;
        LocalDate date = LocalDate.of(2026, 6, 23);
        LocalDateTime dateTime = date.atStartOfDay();

        OrderResponse order = new OrderResponse();
        order.setId(orderId);
        order.setListingId(listingId);
        order.setOfferId(null); // direct purchase/no offer details
        order.setTotalAmount(5000.0);
        order.setCreatedAt(dateTime);

        ListingResponse listing = new ListingResponse();
        listing.setId(listingId);
        listing.setCategoryName("Plastic Scrap");
        listing.setUnit("kgs");
        listing.setPricePerUnit(10.0); // 5000 / 10 = 500 kgs = 0.5 tons

        when(orderFeignClient.getOrder(orderId)).thenReturn(order);
        when(marketplaceFeignClient.getListing(listingId)).thenReturn(listing);
        when(dailyMetricRepository.findByDate(date)).thenReturn(Optional.empty());

        analyticsService.processOrderCreated(orderId);

        ArgumentCaptor<DailyMetric> metricCaptor = ArgumentCaptor.forClass(DailyMetric.class);
        verify(dailyMetricRepository).save(metricCaptor.capture());

        DailyMetric savedMetric = metricCaptor.getValue();
        assertEquals(date, savedMetric.getDate());
        assertEquals(0.5, savedMetric.getWasteReusedTons()); // 500 kgs / 1000 = 0.5 tons
        // CO2 factor for plastic scrap is 1.7, so 0.5 * 1.7 = 0.85
        assertEquals(0.85, savedMetric.getCo2Saved());
        assertEquals(5000.0, savedMetric.getRevenue());
        assertEquals(1, savedMetric.getOrders());
    }

    @Test
    void testProcessAuditEvent() {
        String json = "{"
                + "\"eventId\":\"test-uuid\","
                + "\"service\":\"marketplace-service\","
                + "\"action\":\"LISTING_CREATED\","
                + "\"actorId\":123,"
                + "\"payload\":{\"listingId\":100}"
                + "}";

        analyticsService.processAuditEvent(json);

        ArgumentCaptor<AuditEvent> eventCaptor = ArgumentCaptor.forClass(AuditEvent.class);
        verify(auditEventRepository).save(eventCaptor.capture());

        AuditEvent savedEvent = eventCaptor.getValue();
        assertEquals("test-uuid", savedEvent.getEventId());
        assertEquals("marketplace-service", savedEvent.getService());
        assertEquals("LISTING_CREATED", savedEvent.getAction());
        assertEquals(123L, savedEvent.getActorId());
        assertNotNull(savedEvent.getPayload());
        assertEquals(100, savedEvent.getPayload().get("listingId"));
    }
}
