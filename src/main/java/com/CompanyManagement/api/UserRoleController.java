package com.CompanyManagement.api;

import com.CompanyManagement.persistence.entities.UserRole;
import com.CompanyManagement.service.UserRoleService;
import com.CompanyManagement.web.dto.UserRoleDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/role")
public class UserRoleController {

    private final UserRoleService roleService;

    private final UserRoleDto userRoleDto;

    @PostMapping
    public void createUserRole(@RequestBody UserRoleDto userRole) {
        roleService.createUserRole(userRole);
    }

    @GetMapping
    public List<UserRoleDto> getUserRoles() {
        return roleService.getUserRoles();
    }

    @DeleteMapping("/{id}")
    public void deleteItemById(@PathVariable UUID id) {
        roleService.deleteUserRoleById(id);
    }

    @PutMapping("/{id}")
    public void updateUserRole(@PathVariable UUID id, @RequestBody UserRoleDto userRole) {
        roleService.updateUserRole(userRole, id);
    }

}