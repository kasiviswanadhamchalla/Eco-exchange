package com.industry_connect.order_service.controller;

import com.industry_connect.order_service.config.UserPrincipal;
import com.industry_connect.order_service.dto.OrderRequest;
import com.industry_connect.order_service.dto.ShipmentRequest;
import com.industry_connect.order_service.entity.Order;
import com.industry_connect.order_service.entity.Shipment;
import com.industry_connect.order_service.service.IdempotencyService;
import com.industry_connect.order_service.service.OrderService;
import com.industry_connect.order_service.service.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private IdempotencyService idempotencyService;

    private Long resolveOrgId(Long xOrgId, UserPrincipal principal) {
        if (xOrgId != null) {
            return xOrgId;
        }
        if (principal != null) {
            return principal.getOrganizationId();
        }
        return null;
    }

    @PostMapping
    public ResponseEntity<?> createOrder(
            @RequestBody OrderRequest request,
            @RequestHeader(value = "X-Idempotency-Key", required = false) String idempotencyKey,
            @RequestHeader(value = "X-Organization-Id", required = false) Long xOrgId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        Long userOrgId = resolveOrgId(xOrgId, principal);

        if (idempotencyKey != null && !idempotencyKey.trim().isEmpty()) {
            var existing = idempotencyService.get(idempotencyKey);
            if (existing.isPresent()) {
                return idempotencyService.toResponse(existing.get(), Order.class);
            }
        }

        try {
            Order order = orderService.createOrder(request, userOrgId);

            if (idempotencyKey != null && !idempotencyKey.trim().isEmpty()) {
                idempotencyService.save(idempotencyKey, request, order, HttpStatus.CREATED);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(order);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(java.util.Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        return orderService.getOrder(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyOrders(
            @RequestHeader(value = "X-Organization-Id", required = false) Long xOrgId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        Long orgId = resolveOrgId(xOrgId, principal);
        if (orgId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(java.util.Map.of("error", "Unauthorized access. Organization context missing."));
        }
        try {
            List<Order> orders = orderService.getMyOrders(orgId);
            return ResponseEntity.ok(orders);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(java.util.Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/shipment")
    public ResponseEntity<?> createShipment(
            @PathVariable Long id,
            @RequestBody ShipmentRequest request,
            @RequestHeader(value = "X-Idempotency-Key", required = false) String idempotencyKey
    ) {
        if (idempotencyKey != null && !idempotencyKey.trim().isEmpty()) {
            var existing = idempotencyService.get(idempotencyKey);
            if (existing.isPresent()) {
                return idempotencyService.toResponse(existing.get(), Shipment.class);
            }
        }

        try {
            Shipment shipment = shipmentService.createShipment(id, request);

            if (idempotencyKey != null && !idempotencyKey.trim().isEmpty()) {
                idempotencyService.save(idempotencyKey, request, shipment, HttpStatus.CREATED);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(shipment);
        } catch (RuntimeException e) {
            if (e.getMessage() != null && e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(java.util.Map.of("error", e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(java.util.Map.of("error", e.getMessage()));
        }
    }
}
