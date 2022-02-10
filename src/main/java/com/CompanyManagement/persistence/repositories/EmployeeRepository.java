package com.CompanyManagement.persistence.repositories;

import com.CompanyManagement.persistence.entities.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    Employee findByEmail(String email);
    Employee findByOib(long oib);
    Page<Employee> findAll(Pageable pageable);
    List<Employee> findAll(Sort sort);

}