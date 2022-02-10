package com.CompanyManagement.service;

import com.CompanyManagement.persistence.entities.UserRole;
import com.CompanyManagement.persistence.repositories.UserRoleRepository;
import com.CompanyManagement.web.dto.UserRoleDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Component
@Service
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;
    private final UserRoleDto userRoleDto;
    public UserRole createUserRole(UserRoleDto userRole){
        return (userRoleRepository.save(userRoleDto.ConvertDtoToEntity(userRole)));
    }

    public List<UserRoleDto> getUserRoles() {
        return  userRoleDto.ConvertEntityToDto(userRoleRepository.findAll());
    }

    public void deleteUserRoleById(UUID id) {
        userRoleRepository.deleteById(id);
    }

    public void updateUserRole(UserRoleDto newUserRole, UUID id) {
        var role = userRoleDto.ConvertEntityToDto(userRoleRepository.findById(id).orElse(null));

        role.setRoleName(newUserRole.getRoleName());

        userRoleRepository.save(userRoleDto.ConvertDtoToEntity(role));
    }
}