package com.CompanyManagement.service;

import com.CompanyManagement.persistence.entities.Employee;
import com.CompanyManagement.web.EmployeeRegistrationDto;
import com.CompanyManagement.web.dto.EmployeeDto;
import com.CompanyManagement.web.dto.UserRoleDto;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.UUID;

public interface EmployeeService extends UserDetailsService {


    public List<UserRoleDto> listRoles();
    public Employee get(UUID id);
    Employee save(EmployeeRegistrationDto registrationDto);
    public String deleteEmployee(UUID id);
    Employee findByEmail(String email);
    public boolean emailExists(String email);
    public List<EmployeeDto> findBySurnameIgnoreCase(String keyword);
    Page<Employee> listAll(int currentPage, String sortField, String sortDir);
    public List<EmployeeDto> getEmployees();


}
