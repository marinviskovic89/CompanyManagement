package com.CompanyManagement.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRegistrationDto {

    private String firstName;
    private String lastName;
    private long oib;
    private String address;
    private String email;
    private String passwd;
}
