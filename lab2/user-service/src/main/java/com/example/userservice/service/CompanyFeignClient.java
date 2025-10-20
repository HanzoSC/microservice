package com.example.userservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "company-service", url = "${company-service.base-url:http://localhost:8083}", fallback = CompanyFeignClientFallback.class)
public interface CompanyFeignClient {
    @GetMapping("/api/companies/{id}/exists")
    ResponseEntity<Void> exists(@PathVariable("id") Long id);

    @GetMapping("/api/companies/{id}/name")
    String getName(@PathVariable("id") Long id);
}


