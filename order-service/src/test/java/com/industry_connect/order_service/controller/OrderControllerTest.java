package com.industry_connect.order_service.controller;

import com.industry_connect.order_service.dto.OrderRequest;
import com.industry_connect.order_service.entity.Order;
import com.industry_connect.order_service.service.IdempotencyService;
import com.industry_connect.order_service.service.OrderService;
import com.industry_connect.order_service.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @Mock
    private IdempotencyService idempotencyService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private OrderController orderController;

    @Test
    void testCreateOrder_Success() {
        OrderRequest request = new OrderRequest(100L, 30L, 50L, 200L, 50000.0);
        Order order = new Order(100L, 30L, 50L, 200L, 50000.0, "PENDING");
        order.setId(900L);

        when(orderService.createOrder(any(OrderRequest.class), any())).thenReturn(order);

        ResponseEntity<?> response = orderController.createOrder(request, null, 30L, null);

        assertNotNull(response);
        assertEquals(201, response.getStatusCode().value());
        Order body = (Order) response.getBody();
        assertEquals(900L, body.getId());
        assertEquals("PENDING", body.getStatus());
    }

    @Test
    void testGetOrder_Found() {
        Order order = new Order();
        order.setId(900L);
        when(orderService.getOrder(900L)).thenReturn(Optional.of(order));

        ResponseEntity<Order> response = orderController.getOrder(900L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(900L, response.getBody().getId());
    }

    @Test
    void testGetOrder_NotFound() {
        when(orderService.getOrder(900L)).thenReturn(Optional.empty());

        ResponseEntity<Order> response = orderController.getOrder(900L);

        assertNotNull(response);
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void testGetMyOrders_Success() {
        Order order = new Order();
        order.setId(900L);
        when(orderService.getMyOrders(30L)).thenReturn(List.of(order));

        ResponseEntity<?> response = orderController.getMyOrders(30L, null);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        List<Order> body = (List<Order>) response.getBody();
        assertEquals(1, body.size());
        assertEquals(900L, body.get(0).getId());
    }
}
