package com.example.companyservice.service;

import com.example.companyservice.dto.CompanyDeletedEvent;
import com.example.companyservice.dto.CompanyDto;
import com.example.companyservice.entity.Company;
import com.example.companyservice.repository.CompanyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UserFeignClient userClient;
    private final KafkaTemplate<String, CompanyDeletedEvent> kafkaTemplate;
    private static final String COMPANY_DELETED_TOPIC = "company-deleted";

    public CompanyService(CompanyRepository companyRepository, UserFeignClient userClient,
                          KafkaTemplate<String, CompanyDeletedEvent> kafkaTemplate) {
        this.companyRepository = companyRepository;
        this.userClient = userClient;
        this.kafkaTemplate = kafkaTemplate;
    }

    public boolean exists(Long id) {
        return companyRepository.existsByIdAndDeletedFalse(id);
    }

    public String getCompanyName(Long id) {
        Company company = companyRepository.findByIdNotDeleted(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
        return company.getName();
    }

    public List<CompanyDto> getAll() {
        return companyRepository.findAllNotDeleted().stream().map(this::toDtoWithDirector).toList();
    }

    public CompanyDto create(CompanyDto dto) {
        // Валидация входных данных
        if (dto.getDirectorId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Director ID is required");
        }
        
        // Проверка существования директора
        try {
            ResponseEntity<Void> response = userClient.exists(dto.getDirectorId());
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Director not found or inactive");
            }
        } catch (ResponseStatusException e) {
            throw e; // Пробрасываем статусные исключения
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "User service unavailable");
        }
        
        // Создание компании
        try {
            Company company = new Company();
            company.setName(dto.getName());
            company.setOgrn(dto.getOgrn());
            company.setDescription(dto.getDescription());
            company.setDirectorId(dto.getDirectorId());
            company.setDeleted(false);
            
            Company saved = companyRepository.save(company);
            return toDtoWithDirector(saved);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage() != null && e.getMessage().contains("ogrn")) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Company with this OGRN already exists");
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid company data");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create company");
        }
    }

    private CompanyDto toDtoWithDirector(Company company) {
        CompanyDto dto = new CompanyDto();
        dto.setId(company.getId());
        dto.setName(company.getName());
        dto.setOgrn(company.getOgrn());
        dto.setDescription(company.getDescription());
        dto.setDirectorId(company.getDirectorId());
        try {
            dto.setDirectorFullName(userClient.getName(company.getDirectorId()));
        } catch (Exception e) {
            dto.setDirectorFullName("Unknown");
        }
        return dto;
    }

    @Transactional
    public void delete(Long id) {
        Company company = companyRepository.findByIdNotDeleted(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
        
        company.setDeleted(true);
        companyRepository.save(company);
        
        // Отправляем сообщение в Kafka для user-service
        if (kafkaTemplate != null) {
            try {
                CompanyDeletedEvent event = new CompanyDeletedEvent(id);
                kafkaTemplate.send(COMPANY_DELETED_TOPIC, event).get(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (ExecutionException | TimeoutException e) {
                // Игнорируем ошибки Kafka, компания уже помечена как удаленная
            } catch (Exception e) {
                // Игнорируем ошибки Kafka, компания уже помечена как удаленная
            }
        }
    }

    @Transactional
    public void hardDelete(Long companyId) {
        companyRepository.deleteById(companyId);
    }
}


