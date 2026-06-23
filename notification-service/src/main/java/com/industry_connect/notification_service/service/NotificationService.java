package com.industry_connect.notification_service.service;

import com.industry_connect.notification_service.entity.Notification;
import com.industry_connect.notification_service.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private NotificationRepository notificationRepository;

    public List<Notification> getNotificationsByUserId(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    @Transactional
    public Notification markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id " + id));
        notification.setStatus("READ");
        return notificationRepository.save(notification);
    }

    @Transactional
    public Notification createNotification(Long userId, String title, String message) {
        Notification notification = new Notification(userId, title, message, "UNREAD");
        notification = notificationRepository.save(notification);
        logger.info("Saved notification to DB: user={}, title={}", userId, title);
        return notification;
    }

    public void sendEmail(String email, String subject, String message) {
        // Mock email sending
        logger.info("================ MOCK EMAIL SENT ================");
        logger.info("To: {}", email);
        logger.info("Subject: {}", subject);
        logger.info("Message: {}", message);
        logger.info("================================================");
    }
}
