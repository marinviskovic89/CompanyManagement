package com.CompanyManagement.persistence.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.*;

@Setter
@Getter
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "uuid-char")
    private UUID id;

    @Column(length = 35, nullable = false)
    String employeeName;

    @Column(length = 35, nullable = false)
    String surname;

    @Column(length = 11, nullable = false, unique = true)
    long oib;

    @Column(length = 50)
    String address;

    @Column(unique = true)
    String email;

    @Column(nullable = false)
    String passwd;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))

    private Collection<UserRole> roles;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee")
    private List<Invoice> invoices;

    public Employee() {
    }

    public Employee(String employeeName, String surname, long oib, String address, String email, String passwd, Collection<UserRole> roles) {
        this.employeeName = employeeName;
        this.surname = surname;
        this.oib = oib;
        this.address = address;
        this.email = email;
        this.passwd = passwd;
        this.roles = roles;
    }

    public void removeRole(UserRole userRole) {
        this.getRoles().remove(userRole);
        userRole.getEmployees().remove(this);
    }
    public void removeRoles() {
        for (UserRole userRole : new HashSet<>(roles)) {
            removeRole(userRole);
        }
    }
}
