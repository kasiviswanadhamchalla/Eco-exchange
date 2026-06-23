package com.industry_connect.analytics_service.controller;

import com.industry_connect.analytics_service.entity.AuditEvent;
import com.industry_connect.analytics_service.entity.DailyMetric;
import com.industry_connect.analytics_service.service.AnalyticsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyticsControllerTest {

    @Mock
    private AnalyticsService analyticsService;

    @InjectMocks
    private AnalyticsController analyticsController;

    @Test
    void testGetDailyMetrics_Success() {
        LocalDate startDate = LocalDate.of(2026, 6, 1);
        LocalDate endDate = LocalDate.of(2026, 6, 23);
        
        DailyMetric metric = new DailyMetric(LocalDate.of(2026, 6, 15));
        metric.setWasteReusedTons(10.5);
        metric.setCo2Saved(15.75);
        metric.setRevenue(25000.0);
        metric.setOrders(3);

        when(analyticsService.getDailyMetrics(startDate, endDate)).thenReturn(List.of(metric));

        ResponseEntity<List<DailyMetric>> response = analyticsController.getDailyMetrics(startDate, endDate);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        List<DailyMetric> body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.size());
        assertEquals(10.5, body.get(0).getWasteReusedTons());
        assertEquals(15.75, body.get(0).getCo2Saved());
        assertEquals(25000.0, body.get(0).getRevenue());
        assertEquals(3, body.get(0).getOrders());
    }

    @Test
    void testGetSummaryMetrics_Success() {
        Map<String, Object> summaryMap = new HashMap<>();
        summaryMap.put("totalWasteReusedTons", 150.0);
        summaryMap.put("totalCo2Saved", 225.0);
        summaryMap.put("totalRevenue", 350000.0);
        summaryMap.put("totalOrders", 45L);

        when(analyticsService.getSummaryMetrics()).thenReturn(summaryMap);

        ResponseEntity<Map<String, Object>> response = analyticsController.getSummaryMetrics();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(150.0, body.get("totalWasteReusedTons"));
        assertEquals(225.0, body.get("totalCo2Saved"));
        assertEquals(350000.0, body.get("totalRevenue"));
        assertEquals(45L, body.get("totalOrders"));
    }

    @Test
    void testGetAudits_Success() {
        AuditEvent audit = new AuditEvent(
                "evt-123",
                "marketplace-service",
                "LISTING_CREATED",
                12L,
                Map.of("listingId", 100L),
                LocalDateTime.now()
        );

        when(analyticsService.getAudits("marketplace-service", "LISTING_CREATED"))
                .thenReturn(List.of(audit));

        ResponseEntity<List<AuditEvent>> response = analyticsController.getAudits("marketplace-service", "LISTING_CREATED");

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        List<AuditEvent> body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.size());
        assertEquals("evt-123", body.get(0).getEventId());
        assertEquals("marketplace-service", body.get(0).getService());
        assertEquals("LISTING_CREATED", body.get(0).getAction());
        assertEquals(12L, body.get(0).getActorId());
        assertEquals(100L, body.get(0).getPayload().get("listingId"));
    }
}
