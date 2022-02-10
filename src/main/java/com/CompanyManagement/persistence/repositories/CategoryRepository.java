package com.CompanyManagement.persistence.repositories;

import com.CompanyManagement.persistence.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    Category findByCategoryName(String categoryName);

    @Override
    List<Category> findAll();

    List<Category> findAllByOrderByCategoryNameAsc();

}
