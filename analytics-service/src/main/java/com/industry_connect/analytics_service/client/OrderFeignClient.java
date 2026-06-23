package com.industry_connect.analytics_service.client;

import com.industry_connect.analytics_service.dto.OrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order-service", url = "${order-service.url:http://localhost:8083}")
public interface OrderFeignClient {

    @GetMapping("/api/orders/{id}")
    OrderResponse getOrder(@PathVariable("id") Long id);
}
