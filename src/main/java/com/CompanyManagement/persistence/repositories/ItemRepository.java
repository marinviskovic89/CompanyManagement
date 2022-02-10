package com.CompanyManagement.persistence.repositories;

import com.CompanyManagement.persistence.entities.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ItemRepository extends JpaRepository<Item, UUID> {

    List<Item> findByItemNameContainingIgnoreCase(String itemName);

    Page<Item> findAll(Pageable pageable);
    List<Item> findAll(Sort sort);
}
