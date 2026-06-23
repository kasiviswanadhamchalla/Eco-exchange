package com.industry_connect.order_service.service;

import com.industry_connect.order_service.client.MarketplaceFeignClient;
import com.industry_connect.order_service.dto.ListingResponse;
import com.industry_connect.order_service.dto.OfferResponse;
import com.industry_connect.order_service.dto.OrderRequest;
import com.industry_connect.order_service.entity.Order;
import com.industry_connect.order_service.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private MarketplaceFeignClient marketplaceFeignClient;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void testCreateOrder_Success_NoOffer() {
        OrderRequest request = new OrderRequest(100L, 30L, 50L, null, 50000.0);
        ListingResponse listing = new ListingResponse(100L, 50L, 10.0, 10.0, 5000.0, "ACTIVE");

        when(marketplaceFeignClient.getListing(100L)).thenReturn(listing);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(900L);
            return order;
        });

        Order created = orderService.createOrder(request, 30L);

        assertNotNull(created);
        assertEquals(900L, created.getId());
        assertEquals(100L, created.getListingId());
        assertEquals(30L, created.getBuyerOrgId());
        assertEquals(50L, created.getSellerOrgId());
        assertNull(created.getOfferId());
        assertEquals(50000.0, created.getTotalAmount());
        assertEquals("PENDING", created.getStatus());

        verify(eventPublisher, times(1)).publishOrderCreated(900L);
    }

    @Test
    public void testCreateOrder_Success_WithAcceptedOffer() {
        OrderRequest request = new OrderRequest(100L, 30L, 50L, 200L, 50000.0);
        ListingResponse listing = new ListingResponse(100L, 50L, 10.0, 10.0, 5000.0, "ACTIVE");
        OfferResponse offer = new OfferResponse(200L, 100L, 30L, 10.0, 5000.0, "ACCEPTED");

        when(marketplaceFeignClient.getListing(100L)).thenReturn(listing);
        when(marketplaceFeignClient.getOffer(200L)).thenReturn(offer);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(900L);
            return order;
        });

        Order created = orderService.createOrder(request, 30L);

        assertNotNull(created);
        assertEquals(900L, created.getId());
        assertEquals(200L, created.getOfferId());
    }

    @Test
    public void testCreateOrder_ListingNotFound() {
        OrderRequest request = new OrderRequest(100L, 30L, 50L, null, 50000.0);
        feign.FeignException.NotFound feignEx = mock(feign.FeignException.NotFound.class);
        when(marketplaceFeignClient.getListing(100L)).thenThrow(feignEx);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(request, 30L);
        });

        assertTrue(ex.getMessage().contains("Listing not found with id: 100"));
    }

    @Test
    public void testCreateOrder_SellerMismatch() {
        OrderRequest request = new OrderRequest(100L, 30L, 50L, null, 50000.0);
        ListingResponse listing = new ListingResponse(100L, 60L, 10.0, 10.0, 5000.0, "ACTIVE");

        when(marketplaceFeignClient.getListing(100L)).thenReturn(listing);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(request, 30L);
        });

        assertTrue(ex.getMessage().contains("Seller Organization ID does not match the listing owner"));
    }

    @Test
    public void testCreateOrder_OfferNotAccepted() {
        OrderRequest request = new OrderRequest(100L, 30L, 50L, 200L, 50000.0);
        ListingResponse listing = new ListingResponse(100L, 50L, 10.0, 10.0, 5000.0, "ACTIVE");
        OfferResponse offer = new OfferResponse(200L, 100L, 30L, 10.0, 5000.0, "PENDING");

        when(marketplaceFeignClient.getListing(100L)).thenReturn(listing);
        when(marketplaceFeignClient.getOffer(200L)).thenReturn(offer);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(request, 30L);
        });

        assertTrue(ex.getMessage().contains("Cannot create order: Offer is not in ACCEPTED status"));
    }
}
