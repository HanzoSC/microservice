package com.example.userservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "api-gateway", fallback = CompanyFeignClientFallback.class)
public interface CompanyFeignClient {
    @GetMapping("/company/api/companies/{id}/exists")
    ResponseEntity<Void> exists(@PathVariable("id") Long id);

    @GetMapping("/company/api/companies/{id}/name")
    String getName(@PathVariable("id") Long id);
}


