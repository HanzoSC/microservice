package com.example.userservice.dto;

public class CompanyHardDeleteEvent {
    private Long companyId;

    public CompanyHardDeleteEvent() {
    }

    public CompanyHardDeleteEvent(Long companyId) {
        this.companyId = companyId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }
}

