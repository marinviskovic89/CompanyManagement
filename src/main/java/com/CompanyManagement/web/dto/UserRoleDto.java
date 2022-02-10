package com.CompanyManagement.web.dto;

import com.CompanyManagement.persistence.entities.Employee;
import com.CompanyManagement.persistence.entities.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class UserRoleDto {
    private UUID id;
    private String roleName;
    private Collection<Employee> employees;

    public UserRole ConvertDtoToEntity(UserRoleDto userRoleDto) {
        UserRole userRole = new UserRole();
        userRole.setId(userRoleDto.getId());
        userRole.setRoleName(userRoleDto.getRoleName());
        userRole.setEmployees(userRoleDto.getEmployees());
        return  userRole;
    }
    public UserRoleDto ConvertEntityToDto(UserRole userRole) {
        UserRoleDto userRoleDto = new UserRoleDto();
        userRoleDto.setId(userRole.getId());
        userRoleDto.setRoleName(userRole.getRoleName());
        userRoleDto.setEmployees(userRole.getEmployees());
        return  userRoleDto;
    }

    public List<UserRoleDto> ConvertEntityToDto(List<UserRole> userRoles) {
        return userRoles.stream().map(x -> ConvertEntityToDto(x)).collect(Collectors.toList());
    }
    public List<UserRole> ConvertDtoToEntity(List<UserRoleDto> userRoleDtos) {
        return userRoleDtos.stream().map(x -> ConvertDtoToEntity(x)).collect(Collectors.toList());
    }
}
