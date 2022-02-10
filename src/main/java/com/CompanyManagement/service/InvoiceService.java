package com.CompanyManagement.service;

import com.CompanyManagement.exception.NotEnoughQuantityException;
import com.CompanyManagement.persistence.entities.Customer;
import com.CompanyManagement.persistence.entities.Invoice;
import com.CompanyManagement.persistence.entities.Item;
import com.CompanyManagement.persistence.entities.ItemInvoice;
import com.CompanyManagement.persistence.repositories.InvoiceRepository;
import com.CompanyManagement.util.MapperUtils;
import com.CompanyManagement.util.PaymentStatus;
import com.CompanyManagement.util.UserUtils;
import com.CompanyManagement.web.SearchParams;
import com.CompanyManagement.web.dto.InvoiceDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final ItemService itemService;
    private final CustomerService customerService;
    private final EmployeeService employeeService;

    public InvoiceService(InvoiceRepository invoiceRepository, ItemService itemService, CustomerService customerService, EmployeeService employeeService) {
        this.invoiceRepository = invoiceRepository;
        this.itemService = itemService;
        this.customerService = customerService;
        this.employeeService = employeeService;
    }

    public Invoice createInvoice(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    public List<Invoice> getInvoices() {
        return invoiceRepository.findAll();
    }

    public Invoice getById(UUID id) {
        return invoiceRepository.getById(id);
    }

    @Transactional
    public void deleteInvoice(UUID id) {
        Invoice invoice = invoiceRepository.getById(id);
        if (invoice == null) {
            throw new IllegalArgumentException("Invalid UUID");
        }

        for (ItemInvoice itemInvoice : invoice.getItems()) {
            Item item = itemInvoice.getItem();
            item.setQuantity(item.getQuantity() + itemInvoice.getAmount());
        }
        invoiceRepository.delete(invoice);
    }

    @Transactional
    public void createInvoice(InvoiceDto invoice, UUID[] itemId, Integer[] amount) {
        if (itemId != null) {
            for (int i = 0; i < itemId.length; i++) {
                Item product = itemService.getItemById(itemId[i]);
                ItemInvoice itemInvoice = new ItemInvoice();
                itemInvoice.setAmount(amount[i]);
                itemInvoice.setItem(product);
                invoice.addItemInvoice(itemInvoice);

                if (product.getQuantity() < (itemInvoice.getAmount())) {
                    throw new NotEnoughQuantityException(String.format("Not enough item in stock %s - %s", product.getItemName(), product.getQuantity()));
                }

                log.info("ID: " + itemId[i].toString() + ", amount: " + amount[i].toString());
            }
        }

        for (ItemInvoice itemInvoice : invoice.getItems()) {
            Item item = itemInvoice.getItem();
            item.setQuantity(item.getQuantity() - itemInvoice.getAmount());
            itemService.createItem(item);
        }

        Optional<Customer> customer = customerService.getCustomerById(invoice.getCustomerId());
        if (customer.isEmpty()) {
            throw new IllegalArgumentException("Customer not present");
        }
        invoice.setCustomer(customer.get());
        invoice.calculateTotal();
        /*invoice.getPaymentMethod(PaymentMethods.CASH.name());*/
        invoice.setPaymentStatus(PaymentStatus.CREATED.name());
        invoice.setEmployee(employeeService.findByEmail(UserUtils.getUsername()));


        long numberOfInvoices = invoiceRepository.count();
        invoice.setInvoiceNumber((int) ((Math.random() * (99 - 10)) + 10));

        createInvoice(MapperUtils.mapObject(invoice, Invoice.class));
    }

    public Page<Invoice> listAll(int pageNumber, String sortField, String sortDir, SearchParams searchParams) {
        Sort sort = Sort.by("dateOfIssue");
        sort = sortDir.equals("asc") ? sort.descending() : sort.ascending();
        Pageable pageable = PageRequest.of(pageNumber - 1, 3, sort);

        if (searchParams == null) {
            return invoiceRepository.findAll(pageable);
        }

        String date = "";
        if (searchParams.getDate() != null) {
             date = searchParams.getDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        }
        return invoiceRepository.findAllByDateOfIssueAndCustomer_customerNameContainingIgnoreCaseOrCustomer_surnameContainingIgnoreCase(date, searchParams.getSearchText(),searchParams.getSearchText(), pageable);

    }

}