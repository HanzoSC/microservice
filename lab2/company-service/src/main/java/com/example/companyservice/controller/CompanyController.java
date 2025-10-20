package com.example.companyservice.controller;

import com.example.companyservice.dto.CompanyDto;
import com.example.companyservice.service.CompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Void> exists(@PathVariable Long id) {
        return companyService.exists(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @GetMapping
    public List<CompanyDto> getAll() {
        return companyService.getAll();
    }

    @PostMapping
    public ResponseEntity<CompanyDto> create(@RequestBody CompanyDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(companyService.create(dto));
    }

    @GetMapping("/{id}/name")
    public String getName(@PathVariable Long id) {
        return companyService.getCompanyName(id);
    }
}


