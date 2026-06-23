package com.industry_connect.notification_service.event;

public class NotificationRequestedEvent {
    private String email;
    private String subject;
    private String message;
    private Long userId;

    public NotificationRequestedEvent() {}

    public NotificationRequestedEvent(String email, String subject, String message, Long userId) {
        this.email = email;
        this.subject = subject;
        this.message = message;
        this.userId = userId;
    }

    // Getters and Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}
