package com.example.companyservice.service;

import com.example.companyservice.dto.CompanyHardDeleteEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class CompanyDeleteConsumer {

    private final CompanyService companyService;

    public CompanyDeleteConsumer(CompanyService companyService) {
        this.companyService = companyService;
    }

    @KafkaListener(topics = "company-hard-delete", groupId = "company-service-group")
    public void consumeHardDelete(CompanyHardDeleteEvent event) {
        try {
            companyService.hardDelete(event.getCompanyId());
        } catch (Exception e) {
            // Игнорируем ошибки
        }
    }
}

