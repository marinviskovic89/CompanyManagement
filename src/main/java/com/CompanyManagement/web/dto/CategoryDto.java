package com.CompanyManagement.web.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CategoryDto {
    private UUID id;
    private String categoryName;
}
