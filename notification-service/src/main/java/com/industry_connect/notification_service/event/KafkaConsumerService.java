package com.industry_connect.notification_service.event;

import com.industry_connect.notification_service.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    @Autowired
    private NotificationService notificationService;

    @KafkaListener(topics = "notification-events", groupId = "notification-group")
    public void consumeNotificationEvent(NotificationRequestedEvent event) {
        logger.info("Received notification request via Kafka: email={}, subject={}", event.getEmail(), event.getSubject());
        
        String subject = event.getSubject() != null ? event.getSubject() : "Notification";
        String message = event.getMessage() != null ? event.getMessage() : "Details: " + subject;
        Long userId = event.getUserId();

        // Simulate sending email
        notificationService.sendEmail(event.getEmail(), subject, message);

        // Store notification in database if userId is provided
        if (userId != null) {
            notificationService.createNotification(userId, subject, message);
        } else {
            logger.warn("Skipping DB save for notification due to missing userId for email={}", event.getEmail());
        }
    }
}
