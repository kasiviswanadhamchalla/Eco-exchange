package com.industry_connect.notification_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "identity-service", url = "${identity-service.url:http://localhost:8081}")
public interface IdentityFeignClient {

    @GetMapping("/api/organizations/{id}/admin-email")
    String getOrganizationAdminEmail(@PathVariable("id") Long id);
}
