package com.industry_connect.notification_service.service;

import com.industry_connect.notification_service.entity.Notification;
import com.industry_connect.notification_service.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetNotificationsByUserId() {
        Notification n1 = new Notification(1L, "Title1", "Msg1", "UNREAD");
        Notification n2 = new Notification(1L, "Title2", "Msg2", "READ");
        
        when(notificationRepository.findByUserIdOrderByCreatedAtDesc(1L)).thenReturn(Arrays.asList(n1, n2));

        List<Notification> list = notificationService.getNotificationsByUserId(1L);
        assertEquals(2, list.size());
        assertEquals("Title1", list.get(0).getTitle());
        verify(notificationRepository, times(1)).findByUserIdOrderByCreatedAtDesc(1L);
    }

    @Test
    void testMarkAsRead_Success() {
        Notification n1 = new Notification(1L, "Title1", "Msg1", "UNREAD");
        n1.setId(10L);

        when(notificationRepository.findById(10L)).thenReturn(Optional.of(n1));
        when(notificationRepository.save(any(Notification.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Notification result = notificationService.markAsRead(10L);
        assertEquals("READ", result.getStatus());
        verify(notificationRepository, times(1)).findById(10L);
        verify(notificationRepository, times(1)).save(n1);
    }

    @Test
    void testMarkAsRead_NotFound() {
        when(notificationRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> notificationService.markAsRead(10L));
        verify(notificationRepository, times(1)).findById(10L);
        verify(notificationRepository, never()).save(any());
    }

    @Test
    void testCreateNotification() {
        Notification n1 = new Notification(1L, "Title1", "Msg1", "UNREAD");
        when(notificationRepository.save(any(Notification.class))).thenReturn(n1);

        Notification result = notificationService.createNotification(1L, "Title1", "Msg1");
        assertNotNull(result);
        assertEquals("Title1", result.getTitle());
        assertEquals("Msg1", result.getMessage());
        assertEquals("UNREAD", result.getStatus());
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }
}
