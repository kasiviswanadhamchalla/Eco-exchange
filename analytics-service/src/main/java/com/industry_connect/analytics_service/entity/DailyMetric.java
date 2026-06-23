package com.industry_connect.analytics_service.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "daily_metrics")
public class DailyMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private LocalDate date;

    @Column(name = "waste_reused_tons", nullable = false)
    private Double wasteReusedTons = 0.0;

    @Column(name = "co2_saved", nullable = false)
    private Double co2Saved = 0.0;

    @Column(nullable = false)
    private Double revenue = 0.0;

    @Column(nullable = false)
    private Integer orders = 0;

    public DailyMetric() {}

    public DailyMetric(LocalDate date) {
        this.date = date;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Double getWasteReusedTons() { return wasteReusedTons; }
    public void setWasteReusedTons(Double wasteReusedTons) { this.wasteReusedTons = wasteReusedTons; }

    public Double getCo2Saved() { return co2Saved; }
    public void setCo2Saved(Double co2Saved) { this.co2Saved = co2Saved; }

    public Double getRevenue() { return revenue; }
    public void setRevenue(Double revenue) { this.revenue = revenue; }

    public Integer getOrders() { return orders; }
    public void setOrders(Integer orders) { this.orders = orders; }
}
