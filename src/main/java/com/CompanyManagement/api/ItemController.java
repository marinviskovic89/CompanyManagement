package com.CompanyManagement.api;

import com.CompanyManagement.persistence.entities.Category;
import com.CompanyManagement.persistence.entities.Item;
import com.CompanyManagement.persistence.repositories.CategoryRepository;
import com.CompanyManagement.persistence.repositories.ItemRepository;
import com.CompanyManagement.service.CategoryService;
import com.CompanyManagement.service.ItemService;
import com.CompanyManagement.util.MapperUtils;
import com.CompanyManagement.web.SearchParams;
import com.CompanyManagement.web.dto.ItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Controller
@SessionAttributes({"categories"})
@RequestMapping("/items/")
public class ItemController {


    private final ItemRepository itemRepository;
    private final CategoryController categoryController;
    private final CategoryRepository categoryRepository;
    private final ItemService itemService;
    private final CategoryService categoryService;

    @Autowired
    public ItemController(ItemRepository itemRepository, CategoryController categoryController, CategoryRepository categoryRepository, ItemService itemService, CategoryService categoryService) {
        this.itemRepository = itemRepository;
        this.categoryController = categoryController;
        this.categoryRepository = categoryRepository;
        this.itemService = itemService;
        this.categoryService = categoryService;
    }

    @PostMapping
    public void createItem(@RequestBody Item item) {
        itemService.createItem(item);
    }

    @GetMapping
    public List<Item> getItems() {
        return itemService.getItems();
    }

    @GetMapping("listOfItems")
    public List<Item> getItems(Category category, Model model) {
        List<Category> listCategories = categoryService.getCategories();
        model.addAttribute("listCategories", listCategories);
        model.addAttribute("items", itemRepository.findAll());
        return itemService.getItems();
    }

    @GetMapping("addItem")
    public String addItem(Model model) {
        model.addAttribute("item", new Item());
        model.addAttribute("categories", categoryRepository.findAllByOrderByCategoryNameAsc());
        return "item-add";
    }

    @PostMapping("addItem")
    public String addItem(@Valid Item item, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "item-add";
        }
        itemRepository.save(item);
        return "redirect:viewPage";
    }

    @DeleteMapping("/{id}")
    public void deleteItemById(@PathVariable UUID id) {
        itemService.deleteItemById(id);
    }

    @GetMapping("delete/{id}")
    public String deleteItem(@PathVariable("id") UUID id, Model model) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid item Id:" + id));
        itemRepository.delete(item);
        model.addAttribute("items", itemRepository.findAll());
        return "redirect:/items/viewPage";
    }

    @PutMapping("/{id}")
    public void updateItem(@PathVariable UUID id, @RequestBody Item item) {
        itemService.updateItem(item, id);
    }

    @GetMapping("edit/{id}")
    public String showUpdateForm(@PathVariable("id") UUID id, Model model) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid item Id:" + id));
        model.addAttribute("item", item);
        model.addAttribute("category", categoryRepository.findAll());
        return "update-item";
    }

    @PostMapping("update/{id}")
    public String updateItem(@PathVariable("id") UUID id, @Valid Item item, BindingResult result,
                             Model model) {
        if (result.hasErrors()) {
            item.setId(id);
            return "redirect:viewPage";
        }
        itemRepository.save(item);
        model.addAttribute("items", itemRepository.findAll());
        model.addAttribute("category", categoryRepository.findAll());
        return "redirect:/items/viewPage";
    }

    @PostMapping("/assign/{itemId}/{categoryId}")
    public void assignCategoryToItem(@PathVariable UUID itemId, @PathVariable UUID categoryId) {
        itemService.assignCategoryToItem(itemId, categoryId);
    }

    //SEARCH
    @RequestMapping("/search")
    public String findByItemNameIgnoreCase(Model model, @Param("keyword") String keyword) {
        List<Item> listItems = itemService.findByItemNameContainingIgnoreCase(keyword);
        model.addAttribute("listItems", listItems);
        model.addAttribute("keyword", keyword);

        return "item-search";
    }

    //PAGING
    @RequestMapping("viewPage")
    public String viewPage(Model model) {
        return listByPage(model, 1, "price", "asc");
    }

    @GetMapping("/page/{pageNumber}")
    public String listByPage(Model model,
                             @PathVariable("pageNumber") int currentPage,
                             @Param("sortField") String sortField,
                             @Param("sortDir") String sortDir) {

        Page<Item> page = itemService.listAll(currentPage, sortField, sortDir);
        int totalElements = (int) page.getTotalElements();
        int totalPages = page.getTotalPages();

        List<Item> listItem = page.getContent();

        model.addAttribute("items", itemRepository.findAll());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("listItem", listItem);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);

        String reverseSortDir =sortDir.equals("asc") ? "desc" : "asc";
        model.addAttribute("reverseSortDir", reverseSortDir);

        model.addAttribute("categories", categoryRepository.findAll());

        return "item-list";
    }


}