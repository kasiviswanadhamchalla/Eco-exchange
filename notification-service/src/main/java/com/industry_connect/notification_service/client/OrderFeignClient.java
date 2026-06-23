package com.industry_connect.notification_service.client;

import com.industry_connect.notification_service.dto.OrderDto;
import com.industry_connect.notification_service.dto.ShipmentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order-service", url = "${order-service.url:http://localhost:8085}")
public interface OrderFeignClient {

    @GetMapping("/api/orders/{id}")
    OrderDto getOrder(@PathVariable("id") Long id);

    @GetMapping("/api/shipments/{id}")
    ShipmentDto getShipment(@PathVariable("id") Long id);
}
