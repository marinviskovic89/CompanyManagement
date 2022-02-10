package com.CompanyManagement.service;

import com.CompanyManagement.persistence.entities.Category;
import com.CompanyManagement.persistence.entities.Customer;
import com.CompanyManagement.persistence.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Component
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category createCategories(Category category){
        return categoryRepository.save(category);
    }

    public List<Category> getCategories() {
        return (List<Category>) categoryRepository.findAll();
    }

    public Category findByCategoryName(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName);
    }

    public Optional<Category> findCategoryById(UUID categoryId) {
        return categoryRepository.findById(categoryId);
    }

}
