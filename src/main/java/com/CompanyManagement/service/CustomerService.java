package com.CompanyManagement.service;

import com.CompanyManagement.persistence.entities.Customer;
import com.CompanyManagement.persistence.repositories.CustomerRepository;
import com.CompanyManagement.web.dto.CustomerDto;
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
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Component
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerDto customerDto;

    public Customer createCustomer(CustomerDto customer){
        return customerRepository.save(customerDto.ConvertDtoToEntity(customer));
    }

    public List<CustomerDto> getCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customerDto.ConvertEntityToDto(customers);
    }

    public Optional<Customer> getCustomerById(UUID id) {
        return customerRepository.findById(id);
    }

    public Customer findCustomerByOib(long oib) {
        return customerRepository.findByOib(oib);
    }

    public void deleteCustomerById(UUID id) {
        customerRepository.deleteById(id);
    }

    public void updateCustomer(CustomerDto newCustomer, UUID id) {
        var c = customerRepository.findById(id).orElse(null);

        c.setCustomerName(newCustomer.getCustomerName());
        c.setSurname(newCustomer.getSurname());
        c.setOib(newCustomer.getOib());
        c.setAddress(newCustomer.getAddress());
        c.setTelephone(newCustomer.getTelephone());
        c.setCity(newCustomer.getCity());

        customerRepository.save(c);
    }


    public List<Customer> findBySurnameIgnoreCase(@Pattern(regexp = "[A-Za-z]") String keyword) {
        var customers = customerRepository.findAll();
        var customerList = new ArrayList<Customer>();

        customers.forEach(c -> {
            if(c.getSurname().toLowerCase().contains(keyword.toLowerCase())) {
                customerList.add(c);
            }
        });
        return customerList;
    }

    //NOVI PAGING&SORTING
    public Page<Customer> listAll(int pageNumber, String sortField, String sortDir) {
        Sort sort = Sort.by("surname");
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNumber - 1, 2, sort);
        return customerRepository.findAll(pageable);
    }

}
