package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.entity.UserAccount;
import com.example.userservice.repository.UserAccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserAccountRepository userRepository;
    private final CompanyFeignClient companyClient;

    public UserService(UserAccountRepository userRepository, CompanyFeignClient companyClient) {
        this.userRepository = userRepository;
        this.companyClient = companyClient;
    }

    public List<UserDto> getAll() {
        return userRepository.findAll().stream().map(this::toDtoWithCompany).toList();
    }

    public void setActive(Long id, boolean active) {
        UserAccount user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        user.setActive(active);
        userRepository.save(user);
    }

    public UserDto create(UserDto dto) {
        if (dto.getCompanyId() != null) {
            try {
                ResponseEntity<Void> response = companyClient.exists(dto.getCompanyId());
                if (!response.getStatusCode().is2xxSuccessful()) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found");
                }
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found");
            }
        }
        UserAccount user = new UserAccount();
        user.setName(dto.getName());
        user.setLogin(dto.getLogin());
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());
        user.setCompanyId(dto.getCompanyId());
        user.setActive(true);
        UserAccount saved = userRepository.save(user);
        return toDtoWithCompany(saved);
    }

    public UserDto update(Long id, UserDto dto) {
        UserAccount user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (dto.getCompanyId() != null) {
            try {
                ResponseEntity<Void> response = companyClient.exists(dto.getCompanyId());
                if (!response.getStatusCode().is2xxSuccessful()) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found");
                }
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found");
            }
        }
        Optional.ofNullable(dto.getName()).ifPresent(user::setName);
        Optional.ofNullable(dto.getEmail()).ifPresent(user::setEmail);
        if (dto.getCompanyId() != null) user.setCompanyId(dto.getCompanyId());
        UserAccount saved = userRepository.save(user);
        return toDtoWithCompany(saved);
    }

    public void checkExistsAndActive(Long id) {
        UserAccount user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (!user.isActive()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User is inactive");
        }
    }

    public String getUserFullName(Long id) {
        UserAccount user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return user.getName();
    }

    private UserDto toDtoWithCompany(UserAccount user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setLogin(user.getLogin());
        dto.setEmail(user.getEmail());
        dto.setActive(user.isActive());
        dto.setCompanyId(user.getCompanyId());
        if (user.getCompanyId() != null) {
            try {
                dto.setCompanyName(companyClient.getName(user.getCompanyId()));
            } catch (Exception e) {
                dto.setCompanyName("Unknown");
            }
        }
        return dto;
    }
}


