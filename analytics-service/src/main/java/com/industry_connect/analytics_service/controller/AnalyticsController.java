package com.industry_connect.analytics_service.controller;

import com.industry_connect.analytics_service.entity.AuditEvent;
import com.industry_connect.analytics_service.entity.DailyMetric;
import com.industry_connect.analytics_service.service.AnalyticsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/metrics/daily")
    public ResponseEntity<List<DailyMetric>> getDailyMetrics(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        List<DailyMetric> metrics = analyticsService.getDailyMetrics(startDate, endDate);
        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/metrics/summary")
    public ResponseEntity<Map<String, Object>> getSummaryMetrics() {
        Map<String, Object> summary = analyticsService.getSummaryMetrics();
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/audits")
    @PreAuthorize("hasRole('PLATFORM_ADMIN')")
    public ResponseEntity<List<AuditEvent>> getAudits(
            @RequestParam(required = false) String service,
            @RequestParam(required = false) String action) {
        
        List<AuditEvent> audits = analyticsService.getAudits(service, action);
        return ResponseEntity.ok(audits);
    }
}
