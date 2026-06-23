package com.industry_connect.notification_service.controller;

import com.industry_connect.notification_service.entity.Notification;
import com.industry_connect.notification_service.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class NotificationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationController notificationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(notificationController).build();
    }

    @Test
    void testGetNotificationsByUser() throws Exception {
        Notification n1 = new Notification(1L, "Title1", "Msg1", "UNREAD");
        when(notificationService.getNotificationsByUserId(1L)).thenReturn(Arrays.asList(n1));

        mockMvc.perform(get("/api/notifications/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Title1")));

        verify(notificationService, times(1)).getNotificationsByUserId(1L);
    }

    @Test
    void testMarkAsRead_Success() throws Exception {
        Notification n1 = new Notification(1L, "Title1", "Msg1", "READ");
        n1.setId(10L);

        when(notificationService.markAsRead(10L)).thenReturn(n1);

        mockMvc.perform(put("/api/notifications/10/read"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("READ")));

        verify(notificationService, times(1)).markAsRead(10L);
    }

    @Test
    void testMarkAsRead_NotFound() throws Exception {
        when(notificationService.markAsRead(10L)).thenThrow(new RuntimeException("Notification not found"));

        mockMvc.perform(put("/api/notifications/10/read"))
                .andExpect(status().isNotFound());

        verify(notificationService, times(1)).markAsRead(10L);
    }
}
