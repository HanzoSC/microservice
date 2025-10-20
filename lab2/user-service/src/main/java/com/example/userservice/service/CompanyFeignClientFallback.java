package com.example.userservice.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CompanyFeignClientFallback implements CompanyFeignClient {
    
    @Override
    public ResponseEntity<Void> exists(Long id) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    
    @Override
    public String getName(Long id) {
        return null;
    }
}
