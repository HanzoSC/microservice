package com.example.companyservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${user-service.base-url:http://localhost:8081}", fallback = UserFeignClientFallback.class)
public interface UserFeignClient {
    @GetMapping("/api/users/{id}/exists")
    ResponseEntity<Void> exists(@PathVariable("id") Long id);

    @GetMapping("/api/users/{id}/name")
    String getName(@PathVariable("id") Long id);
}


