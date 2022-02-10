package com.CompanyManagement.persistence.repositories;

import com.CompanyManagement.persistence.entities.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {

    Page<Invoice> findAll(Pageable pageable);

    List<Invoice> findAll(Sort sort);

    List<Invoice> findAll();

    Page<Invoice> findAllByDateOfIssueAndCustomer_customerNameContainingIgnoreCaseOrCustomer_surnameContainingIgnoreCase(String date, String searchText, String searchText1, Pageable pageable);
}