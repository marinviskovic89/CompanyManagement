package com.CompanyManagement.web;


import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class SearchParams {

    private String searchText;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
}
