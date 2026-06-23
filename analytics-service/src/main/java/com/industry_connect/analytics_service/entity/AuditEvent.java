package com.industry_connect.analytics_service.entity;

import com.industry_connect.analytics_service.util.MapToJsonConverter;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "audit_events")
public class AuditEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id")
    private String eventId;

    private String service;

    private String action;

    @Column(name = "actor_id")
    private Long actorId;

    @Convert(converter = MapToJsonConverter.class)
    @Column(columnDefinition = "TEXT")
    private Map<String, Object> payload;

    private LocalDateTime timestamp;

    public AuditEvent() {}

    public AuditEvent(String eventId, String service, String action, Long actorId, Map<String, Object> payload, LocalDateTime timestamp) {
        this.eventId = eventId;
        this.service = service;
        this.action = action;
        this.actorId = actorId;
        this.payload = payload;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public String getService() { return service; }
    public void setService(String service) { this.service = service; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public Long getActorId() { return actorId; }
    public void setActorId(Long actorId) { this.actorId = actorId; }

    public Map<String, Object> getPayload() { return payload; }
    public void setPayload(Map<String, Object> payload) { this.payload = payload; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
