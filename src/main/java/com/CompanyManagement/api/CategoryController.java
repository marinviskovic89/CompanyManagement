package com.CompanyManagement.api;

import com.CompanyManagement.persistence.entities.Category;
import com.CompanyManagement.persistence.repositories.CategoryRepository;
import com.CompanyManagement.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;


@AllArgsConstructor
@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;

    @PostMapping("/addCategory")
    public RedirectView createCategories( Category category) {
        categoryService.createCategories(category);
        return new RedirectView("/categories/addCategory");
    }

    @GetMapping("/addCategory")
    public String openCategoryForm (Model model) {
        model.addAttribute("category",new Category());
        return "category-add";
    }

    @GetMapping("/{categoryName}")
    public Category findCategoryByName(@PathVariable String categoryName) {
        return categoryService.findByCategoryName(categoryName);
    }
}
