package com.CompanyManagement.persistence.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Collection;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "roles")
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "uuid-char")
    private UUID id;

    @Column(length = 20, nullable = false, unique = true)
    String roleName;

    @ManyToMany(mappedBy = "roles")
    private Collection<Employee> employees;

    public UserRole() {
    }

    public UserRole(UUID id) {
        this.id = id;
    }

    public UserRole(String roleName) {
        this.roleName = roleName;
    }

    public UserRole(UUID id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return this.roleName;
    }
}