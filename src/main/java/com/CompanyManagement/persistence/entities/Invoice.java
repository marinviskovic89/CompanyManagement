package com.CompanyManagement.persistence.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "uuid-char")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "employee_id")
    private Employee employee;
    /*fetch = FetchType.EAGER,*/

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "invoice_id")
    private List<ItemInvoice> items;

    @Column(nullable = false, unique = true)
    private int invoiceNumber;

    @Column(nullable = false)
    private float totalAmount;

    @Column(nullable = false)
    private String dateOfIssue;

    @Column(nullable = false)
    private String dueDate;

    @Column(nullable = false)
    private float vat;

    private float discount;

    @Column(nullable = true)
    private String paymentStatus;

    @Column(nullable = false)
    private String paymentMethod;

    @PrePersist
    protected void onCreate() {
        this.vat = 25;
        this.dateOfIssue = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        this.dueDate = LocalDateTime.now().plusDays(15).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        float vatAmount = this.totalAmount * this.vat / 100;
        float discountAmount = this.totalAmount * this.discount / 100;
        this.totalAmount = this.totalAmount + vatAmount - discountAmount;
    }

}