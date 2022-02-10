package com.CompanyManagement.persistence.repositories;

import com.CompanyManagement.persistence.entities.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@Component
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

   Customer findByOib(long oib);
   Customer findBySurnameIgnoreCase(String surname);

   /*//PAGING
   Page<Customer> findAll(Pageable pageable);

   //SORTING
   List<Customer> findAllByOrderBySurnameAsc(Sort sort);*/

   Page<Customer> findAll(Pageable pageable);
   List<Customer> findAll(Sort sort);

}
