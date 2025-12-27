package com.example.userservice.repository;

import com.example.userservice.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByLogin(String login);
    List<UserAccount> findByCompanyId(Long companyId);
}


