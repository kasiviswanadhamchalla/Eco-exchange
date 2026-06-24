package com.industry_connect.order_service.service;

import com.industry_connect.order_service.dto.ShipmentRequest;
import com.industry_connect.order_service.entity.Order;
import com.industry_connect.order_service.entity.Shipment;
import com.industry_connect.order_service.repository.OrderRepository;
import com.industry_connect.order_service.repository.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ShipmentService {

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EventPublisher eventPublisher;

    @Transactional
    public Shipment createShipment(Long orderId, ShipmentRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        // Check if shipment already exists for this order to prevent duplicate shipments
        Optional<Shipment> existingShipment = shipmentRepository.findByOrderId(orderId);
        if (existingShipment.isPresent()) {
            return existingShipment.get();
        }

        String initialStatus = "REQUESTED";
        if (request.getPartnerId() != null) {
            initialStatus = "ASSIGNED";
        }

        Shipment shipment = new Shipment(
                orderId,
                request.getPartnerId(),
                request.getTrackingNumber(),
                initialStatus
        );

        Shipment savedShipment = shipmentRepository.save(shipment);

        // Update Order status
        order.setStatus("SHIPPED");
        orderRepository.save(order);

        // Publish event
        eventPublisher.publishShipmentRequested(orderId);

        return savedShipment;
    }

    @Transactional
    public Shipment assignShipment(Long id, Long partnerId) {
        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shipment not found with id: " + id));

        if (partnerId == null) {
            throw new IllegalArgumentException("Partner ID is required");
        }

        shipment.setPartnerId(partnerId);
        shipment.setStatus("ASSIGNED");
        return shipmentRepository.save(shipment);
    }

    @Transactional
    public Shipment pickupShipment(Long id) {
        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shipment not found with id: " + id));

        shipment.setStatus("PICKED_UP");
        return shipmentRepository.save(shipment);
    }

    @Transactional
    public Shipment deliverShipment(Long id) {
        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shipment not found with id: " + id));

        shipment.setStatus("DELIVERED");
        Shipment savedShipment = shipmentRepository.save(shipment);

        // Update corresponding Order status to DELIVERED
        orderRepository.findById(shipment.getOrderId()).ifPresent(order -> {
            order.setStatus("DELIVERED");
            orderRepository.save(order);
        });

        // Publish event
        eventPublisher.publishShipmentDelivered(id);

        return savedShipment;
    }

    public Optional<Shipment> getShipment(Long id) {
        return shipmentRepository.findById(id);
    }
    
    public Optional<Shipment> getShipmentByOrderId(Long orderId) {
        return shipmentRepository.findByOrderId(orderId);
    }

    public java.util.List<Shipment> getAllShipments() {
        return shipmentRepository.findAll();
    }
}
