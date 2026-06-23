package com.industry_connect.search_service.client;

import com.industry_connect.search_service.dto.ListingResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "marketplace-service", url = "${marketplace-service.url:http://localhost:8082}")
public interface MarketplaceFeignClient {

    @GetMapping("/api/listings/{id}")
    ListingResponse getListing(@PathVariable("id") Long id);
}
