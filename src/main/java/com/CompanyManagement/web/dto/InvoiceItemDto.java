package com.CompanyManagement.web.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class InvoiceItemDto {

    private UUID itemId;
    private int quantity;

}
