package com.industry_connect.order_service.controller;

import com.industry_connect.order_service.dto.AssignPartnerRequest;
import com.industry_connect.order_service.entity.Shipment;
import com.industry_connect.order_service.service.ShipmentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShipmentControllerTest {

    @Mock
    private ShipmentService shipmentService;

    @InjectMocks
    private ShipmentController shipmentController;

    @Test
    void testAssignShipment_Success() {
        AssignPartnerRequest request = new AssignPartnerRequest(40L);
        Shipment shipment = new Shipment(900L, 40L, "TRK123", "ASSIGNED");
        shipment.setId(555L);

        when(shipmentService.assignShipment(555L, 40L)).thenReturn(shipment);

        ResponseEntity<?> response = shipmentController.assignShipment(555L, request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        Shipment body = (Shipment) response.getBody();
        assertEquals(555L, body.getId());
        assertEquals("ASSIGNED", body.getStatus());
    }

    @Test
    void testPickupShipment_Success() {
        Shipment shipment = new Shipment(900L, 40L, "TRK123", "PICKED_UP");
        shipment.setId(555L);

        when(shipmentService.pickupShipment(555L)).thenReturn(shipment);

        ResponseEntity<?> response = shipmentController.pickupShipment(555L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        Shipment body = (Shipment) response.getBody();
        assertEquals(555L, body.getId());
        assertEquals("PICKED_UP", body.getStatus());
    }

    @Test
    void testDeliverShipment_Success() {
        Shipment shipment = new Shipment(900L, 40L, "TRK123", "DELIVERED");
        shipment.setId(555L);

        when(shipmentService.deliverShipment(555L)).thenReturn(shipment);

        ResponseEntity<?> response = shipmentController.deliverShipment(555L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        Shipment body = (Shipment) response.getBody();
        assertEquals(555L, body.getId());
        assertEquals("DELIVERED", body.getStatus());
    }
}
