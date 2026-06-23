package com.industry_connect.analytics_service.service;

import com.fasterxml.jackson.databind.JsonNode;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class AnalyticsService {

    private static final Logger log = LoggerFactory.getLogger(AnalyticsService.class);

    private final DailyMetricRepository dailyMetricRepository;
    private final AuditEventRepository auditEventRepository;
    private final MarketplaceFeignClient marketplaceFeignClient;
    private final OrderFeignClient orderFeignClient;
    private final ObjectMapper objectMapper;

    // CO2 saving factors in tons of CO2 per ton of material reused
    private static final Map<String, Double> CO2_FACTORS = new HashMap<>();
    static {
        CO2_FACTORS.put("steel scrap", 1.5);
        CO2_FACTORS.put("fly ash", 0.8);
        CO2_FACTORS.put("plastic scrap", 1.7);
        CO2_FACTORS.put("textile waste", 1.2);
        CO2_FACTORS.put("wood waste", 0.9);
        CO2_FACTORS.put("glass waste", 0.3);
    }

    public AnalyticsService(DailyMetricRepository dailyMetricRepository,
                            AuditEventRepository auditEventRepository,
                            MarketplaceFeignClient marketplaceFeignClient,
                            OrderFeignClient orderFeignClient,
                            ObjectMapper objectMapper) {
        this.dailyMetricRepository = dailyMetricRepository;
        this.auditEventRepository = auditEventRepository;
        this.marketplaceFeignClient = marketplaceFeignClient;
        this.orderFeignClient = orderFeignClient;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public void processOrderCreated(Long orderId) {
        log.info("Processing order created event for order ID: {}", orderId);
        try {
            // 1. Fetch Order details
            OrderResponse order = orderFeignClient.getOrder(orderId);
            if (order == null) {
                log.warn("Order details not found for order ID: {}", orderId);
                return;
            }

            // 2. Fetch Listing details
            ListingResponse listing = marketplaceFeignClient.getListing(order.getListingId());
            if (listing == null) {
                log.warn("Listing details not found for listing ID: {} (Order ID: {})", order.getListingId(), orderId);
                return;
            }

            // 3. Determine quantity
            Double quantity = null;
            if (order.getOfferId() != null) {
                try {
                    OfferResponse offer = marketplaceFeignClient.getOffer(order.getOfferId());
                    if (offer != null) {
                        quantity = offer.getQuantity();
                    }
                } catch (Exception e) {
                    log.warn("Failed to fetch offer details for offer ID: {} (Order ID: {}). Error: {}", order.getOfferId(), orderId, e.getMessage());
                }
            }

            // Fallback for quantity if offer detail fetch failed or offerId was null
            if (quantity == null) {
                if (listing.getPricePerUnit() != null && listing.getPricePerUnit() > 0) {
                    quantity = order.getTotalAmount() / listing.getPricePerUnit();
                } else {
                    quantity = listing.getQuantity(); // fallback to listing quantity
                }
            }

            // 4. Convert quantity to tons
            Double quantityInTons = quantity;
            String unit = listing.getUnit() != null ? listing.getUnit().toLowerCase().trim() : "";
            if (unit.equals("kg") || unit.equals("kgs") || unit.equals("kilogram") || unit.equals("kilograms")) {
                quantityInTons = quantity / 1000.0;
            } else if (unit.equals("lbs") || unit.equals("pound") || unit.equals("pounds")) {
                quantityInTons = quantity * 0.00045359237;
            }

            // 5. Calculate CO2 saved
            String category = listing.getCategoryName() != null ? listing.getCategoryName().toLowerCase().trim() : "";
            Double co2Factor = CO2_FACTORS.getOrDefault(category, 1.0); // default factor = 1.0
            Double co2Saved = quantityInTons * co2Factor;

            // 6. Update/Insert daily metrics
            LocalDate orderDate = order.getCreatedAt() != null ? order.getCreatedAt().toLocalDate() : LocalDate.now();
            
            DailyMetric dailyMetric = dailyMetricRepository.findByDate(orderDate)
                    .orElseGet(() -> new DailyMetric(orderDate));

            dailyMetric.setWasteReusedTons(dailyMetric.getWasteReusedTons() + quantityInTons);
            dailyMetric.setCo2Saved(dailyMetric.getCo2Saved() + co2Saved);
            dailyMetric.setRevenue(dailyMetric.getRevenue() + order.getTotalAmount());
            dailyMetric.setOrders(dailyMetric.getOrders() + 1);

            dailyMetricRepository.save(dailyMetric);
            log.info("Successfully updated daily metrics for date {}: waste_reused_tons={}, co2_saved={}, revenue={}, orders={}",
                    orderDate, dailyMetric.getWasteReusedTons(), dailyMetric.getCo2Saved(), dailyMetric.getRevenue(), dailyMetric.getOrders());

        } catch (Exception e) {
            log.error("Error processing order created metrics for order ID {}: {}", orderId, e.getMessage(), e);
        }
    }

    public void processAuditEvent(String message) {
        log.info("Processing audit event: {}", message);
        try {
            JsonNode node = objectMapper.readTree(message);
            
            String eventId = node.has("eventId") ? node.get("eventId").asText() : UUID.randomUUID().toString();
            String service = node.has("service") ? node.get("service").asText() : "unknown-service";
            String action = node.has("action") ? node.get("action").asText() : "UNKNOWN";
            Long actorId = node.has("actorId") ? node.get("actorId").asLong() : null;
            
            Map<String, Object> payload = null;
            if (node.has("payload") && node.get("payload").isObject()) {
                payload = objectMapper.convertValue(node.get("payload"), Map.class);
            } else {
                payload = new HashMap<>();
            }

            LocalDateTime timestamp = LocalDateTime.now();
            if (node.has("timestamp")) {
                try {
                    timestamp = LocalDateTime.parse(node.get("timestamp").asText());
                } catch (Exception ex) {
                    log.warn("Failed to parse audit event timestamp: {}", node.get("timestamp").asText());
                }
            }

            AuditEvent auditEvent = new AuditEvent(eventId, service, action, actorId, payload, timestamp);
            
            try {
                auditEventRepository.save(auditEvent);
                log.info("Successfully saved audit event: service={}, action={}", service, action);
            } catch (Exception ex) {
                log.error("Failed to save audit event to MongoDB: {}. Storing fallback log statement.", ex.getMessage());
            }

        } catch (Exception e) {
            log.error("Error processing/saving audit event message: {}", e.getMessage(), e);
        }
    }

    public List<DailyMetric> getDailyMetrics(LocalDate startDate, LocalDate endDate) {
        LocalDate start = startDate != null ? startDate : LocalDate.now().minusDays(30);
        LocalDate end = endDate != null ? endDate : LocalDate.now();
        return dailyMetricRepository.findByDateBetweenOrderByDateAsc(start, end);
    }

    public Map<String, Object> getSummaryMetrics() {
        List<Object[]> results = dailyMetricRepository.getSummaryMetrics();
        Double totalWasteReusedTons = 0.0;
        Double totalCo2Saved = 0.0;
        Double totalRevenue = 0.0;
        Long totalOrders = 0L;

        if (results != null && !results.isEmpty() && results.get(0) != null) {
            Object[] summary = results.get(0);
            if (summary[0] != null) totalWasteReusedTons = ((Number) summary[0]).doubleValue();
            if (summary[1] != null) totalCo2Saved = ((Number) summary[1]).doubleValue();
            if (summary[2] != null) totalRevenue = ((Number) summary[2]).doubleValue();
            if (summary[3] != null) totalOrders = ((Number) summary[3]).longValue();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("totalWasteReusedTons", totalWasteReusedTons);
        response.put("totalCo2Saved", totalCo2Saved);
        response.put("totalRevenue", totalRevenue);
        response.put("totalOrders", totalOrders);
        return response;
    }

    public List<AuditEvent> getAudits(String service, String action) {
        if (service != null && !service.trim().isEmpty() && action != null && !action.trim().isEmpty()) {
            return auditEventRepository.findByServiceAndActionOrderByTimestampDesc(service.trim(), action.trim());
        } else if (service != null && !service.trim().isEmpty()) {
            return auditEventRepository.findByServiceOrderByTimestampDesc(service.trim());
        } else if (action != null && !action.trim().isEmpty()) {
            return auditEventRepository.findByActionOrderByTimestampDesc(action.trim());
        } else {
            return auditEventRepository.findAllByOrderByTimestampDesc();
        }
    }
}
