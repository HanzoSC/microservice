package com.example.companyservice.repository;

import com.example.companyservice.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    
    @Query("SELECT c FROM Company c WHERE c.deleted = false")
    List<Company> findAllNotDeleted();
    
    @Query("SELECT c FROM Company c WHERE c.id = :id AND c.deleted = false")
    Optional<Company> findByIdNotDeleted(Long id);
    
    boolean existsByIdAndDeletedFalse(Long id);
}


