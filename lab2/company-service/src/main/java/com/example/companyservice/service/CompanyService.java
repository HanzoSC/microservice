package com.example.companyservice.service;

import com.example.companyservice.dto.CompanyDto;
import com.example.companyservice.entity.Company;
import com.example.companyservice.repository.CompanyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UserFeignClient userClient;

    public CompanyService(CompanyRepository companyRepository, UserFeignClient userClient) {
        this.companyRepository = companyRepository;
        this.userClient = userClient;
    }

    public boolean exists(Long id) {
        return companyRepository.existsById(id);
    }

    public String getCompanyName(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
        return company.getName();
    }

    public List<CompanyDto> getAll() {
        return companyRepository.findAll().stream().map(this::toDtoWithDirector).toList();
    }

    public CompanyDto create(CompanyDto dto) {
        try {
            ResponseEntity<Void> response = userClient.exists(dto.getDirectorId());
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Director not found or inactive");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Director not found or inactive");
        }
        Company company = new Company();
        company.setName(dto.getName());
        company.setOgrn(dto.getOgrn());
        company.setDescription(dto.getDescription());
        company.setDirectorId(dto.getDirectorId());
        Company saved = companyRepository.save(company);
        return toDtoWithDirector(saved);
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
}


