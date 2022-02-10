package com.CompanyManagement.persistence.repositories;

import com.CompanyManagement.persistence.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {


}