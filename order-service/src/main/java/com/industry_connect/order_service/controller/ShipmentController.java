package com.industry_connect.order_service.controller;

import com.industry_connect.order_service.dto.AssignPartnerRequest;
import com.industry_connect.order_service.entity.Shipment;
import com.industry_connect.order_service.service.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {

    @Autowired
    private ShipmentService shipmentService;

    @PutMapping("/{id}/assign")
    @PreAuthorize("hasAnyRole('PLATFORM_ADMIN', 'ORG_ADMIN', 'ADMIN')")
    public ResponseEntity<?> assignShipment(@PathVariable Long id, @RequestBody AssignPartnerRequest request) {
        try {
            Shipment shipment = shipmentService.assignShipment(id, request.getPartnerId());
            return ResponseEntity.ok(shipment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/pickup")
    @PreAuthorize("hasAnyRole('PLATFORM_ADMIN', 'ORG_ADMIN', 'ADMIN', 'LOGISTICS_PARTNER')")
    public ResponseEntity<?> pickupShipment(@PathVariable Long id) {
        try {
            Shipment shipment = shipmentService.pickupShipment(id);
            return ResponseEntity.ok(shipment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/deliver")
    @PreAuthorize("hasAnyRole('PLATFORM_ADMIN', 'ORG_ADMIN', 'ADMIN', 'LOGISTICS_PARTNER')")
    public ResponseEntity<?> deliverShipment(@PathVariable Long id) {
        try {
            Shipment shipment = shipmentService.deliverShipment(id);
            return ResponseEntity.ok(shipment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Shipment> getShipment(@PathVariable Long id) {
        return shipmentService.getShipment(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
