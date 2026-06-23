package com.industry_connect.analytics_service.repository;

import com.industry_connect.analytics_service.entity.AuditEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditEventRepository extends JpaRepository<AuditEvent, Long> {

    List<AuditEvent> findAllByOrderByTimestampDesc();

    List<AuditEvent> findByServiceOrderByTimestampDesc(String service);

    List<AuditEvent> findByActionOrderByTimestampDesc(String action);

    List<AuditEvent> findByServiceAndActionOrderByTimestampDesc(String service, String action);
}
