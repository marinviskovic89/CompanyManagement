package com.CompanyManagement.web.dto;

import com.CompanyManagement.persistence.entities.Category;
import lombok.Data;

import java.util.UUID;

@Data
public class ItemDto {
    String name;
    float price;
    int quantity;
    private UUID id;
    private UUID categoryId;
    private Category category;
}
