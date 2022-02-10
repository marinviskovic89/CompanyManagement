package com.CompanyManagement.web.dto;

import com.CompanyManagement.persistence.entities.Employee;
import com.CompanyManagement.persistence.entities.Invoice;
import com.CompanyManagement.persistence.entities.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class EmployeeDto {

    private UUID id;
    private String employeeName;
    private String surname;
    private long oib;
    private String address;
    private String email;
    private String passwd;
    private List<Invoice> invoices;
    private Collection<UserRole> roles;

    public EmployeeDto(String employeeName, String surname, long oib, String address, String email, String encode, Object o) {
    }

    public Employee ConvertDtoToEntity(EmployeeDto employeeDto) {
        Employee employee = new Employee();
        employee.setId(employeeDto.getId());
        employee.setEmployeeName(employeeDto.getEmployeeName());
        employee.setSurname(employeeDto.getSurname());
        employee.setOib(employeeDto.getOib());
        employee.setAddress(employeeDto.getAddress());
        employee.setEmail(employeeDto.getEmail());
        employee.setPasswd(employeeDto.getPasswd());
        employee.setRoles(employeeDto.getRoles());
        return employee;
    }
    public EmployeeDto ConvertEntityToDto(Employee employee) {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(employee.getId());
        employeeDto.setEmployeeName(employee.getEmployeeName());
        employeeDto.setSurname(employee.getSurname());
        employeeDto.setOib(employee.getOib());
        employeeDto.setAddress(employee.getAddress());
        employeeDto.setEmail(employee.getEmail());
        employeeDto.setPasswd(employee.getPasswd());
        employeeDto.setRoles(employee.getRoles());
        return employeeDto;
    }
    public List<EmployeeDto> ConvertEntityToDto(List<Employee> employees) {
        return employees.stream().map(x -> ConvertEntityToDto(x)).collect(Collectors.toList());
    }
    public List<Employee> ConvertDtoToEntity(List<EmployeeDto> employeeDtos) {
        return employeeDtos.stream().map(x -> ConvertDtoToEntity(x)).collect(Collectors.toList());
    }
}
