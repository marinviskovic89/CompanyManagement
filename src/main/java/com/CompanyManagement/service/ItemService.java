package com.CompanyManagement.service;

import com.CompanyManagement.persistence.entities.Item;
import com.CompanyManagement.persistence.repositories.CategoryRepository;
import com.CompanyManagement.persistence.repositories.ItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
@Service
public class ItemService {


        private final ItemRepository itemRepository;
        private final CategoryRepository categoryRepository;

        public Item createItem(Item item) {
                return itemRepository.save(item);
        }

        public List<Item> getItems() {
                return (List<Item>) itemRepository.findAll();
        }

        public List<Item> getItems(String searchText) {
                List<Item> items = itemRepository.findAll();
                return items == null ? new ArrayList<>() : items.stream().filter(item -> item.getItemName().contains(searchText)).collect(Collectors.toList());
        }

        public Item getItemById(UUID id) {
                return itemRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid item Id:" + id));
        }

        public void deleteItemById(UUID id) {
                itemRepository.deleteById(id);
        }

        public void updateItem(Item newItem, UUID id) {
                var i = itemRepository.findById(id).orElse(null);

                i.setPrice(newItem.getPrice());
                i.setItemName(newItem.getItemName());

                itemRepository.save(i);
        }

        public void assignCategoryToItem(UUID itemId, UUID categoryId) {
                var item = itemRepository.findById(itemId).orElse(null);
                var category = categoryRepository.findById(categoryId).orElse(null);

                item.setCategory(category);

                itemRepository.save(item);
        }

        //SEARCH
        public List<Item> findByItemNameContainingIgnoreCase(@Pattern(regexp = "[A-Za-z]+") String keyword) {
                var items = itemRepository.findAll();
                var itemList = new ArrayList<Item>();

                items.forEach(i -> {
                        if(i.getItemName().toLowerCase().contains(keyword.toLowerCase())) {
                                itemList.add(i);
                        }
                });
                return itemList;
        }


        //PAGING&SORTING
        public Page<Item> listAll(int pageNumber, String sortField, String sortDir) {
                Sort sort = Sort.by("price");
                sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
                Pageable pageable = PageRequest.of(pageNumber - 1, 3, sort);
                return itemRepository.findAll(pageable);
        }



}
