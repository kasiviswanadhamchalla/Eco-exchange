package com.industry_connect.analytics_service.repository;

import com.industry_connect.analytics_service.entity.DailyMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyMetricRepository extends JpaRepository<DailyMetric, Long> {

    Optional<DailyMetric> findByDate(LocalDate date);

    List<DailyMetric> findByDateBetweenOrderByDateAsc(LocalDate startDate, LocalDate endDate);

    @Query("SELECT COALESCE(SUM(m.wasteReusedTons), 0.0), COALESCE(SUM(m.co2Saved), 0.0), COALESCE(SUM(m.revenue), 0.0), COALESCE(SUM(m.orders), 0) FROM DailyMetric m")
    java.util.List<Object[]> getSummaryMetrics();
}
