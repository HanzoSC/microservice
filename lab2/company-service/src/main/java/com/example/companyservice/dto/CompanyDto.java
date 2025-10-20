package com.example.companyservice.dto;

public class CompanyDto {
    private Long id;
    private String name;
    private String ogrn;
    private String description;
    private Long directorId;
    private String directorFullName; // from user-service

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getOgrn() { return ogrn; }
    public void setOgrn(String ogrn) { this.ogrn = ogrn; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Long getDirectorId() { return directorId; }
    public void setDirectorId(Long directorId) { this.directorId = directorId; }
    public String getDirectorFullName() { return directorFullName; }
    public void setDirectorFullName(String directorFullName) { this.directorFullName = directorFullName; }
}


