package com.CompanyManagement.web.dto;

import com.CompanyManagement.persistence.entities.Customer;
import com.CompanyManagement.persistence.entities.Employee;
import com.CompanyManagement.persistence.entities.Invoice;
import com.CompanyManagement.persistence.entities.ItemInvoice;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class InvoiceDto {

    private UUID id;

    private int invoiceNumber;

    private float totalAmount;

    private String dateOfIssue;

    private String dueDate;

    private float vat;

    @Max(value = 40)
    private float discount;

    private String paymentStatus;

    private String paymentMethod;

    @NotNull(message = "Customer must be selected.")
    private UUID customerId;
    private Customer customer;

    private Employee employee;

    private UUID itemId;
    private List<ItemInvoice> items;

    public void addItemInvoice(ItemInvoice item) {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }
        this.items.add(item);
    }

    public void calculateTotal() {
        float total = 0.0f;
        for (ItemInvoice item : items) {
            total += item.calculateImport();
        }

        this.totalAmount = total;
    }

    public Invoice toEntity() {
        Invoice entity = new Invoice();
        entity.setId(this.id);
        entity.setInvoiceNumber(this.invoiceNumber);
        entity.setTotalAmount(this.totalAmount);
        entity.setDateOfIssue(this.dateOfIssue);
        entity.setDueDate(this.dueDate);
        entity.setVat(this.vat);
        entity.setDiscount(this.discount);
        entity.setPaymentStatus(this.paymentStatus);
        entity.setPaymentMethod(this.getPaymentMethod());
        return entity;
    }

}
