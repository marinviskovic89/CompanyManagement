package com.CompanyManagement.web.dto;

import com.CompanyManagement.persistence.entities.Customer;
import com.CompanyManagement.persistence.entities.Invoice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class CustomerDto {
    private UUID id;
    private String customerName;
    private String surname;
    private long oib;
    private String address;
    private String telephone;
    private String city;
    private List<Invoice> invoices;

    public Customer ConvertDtoToEntity(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setId(customerDto.getId());
        customer.setCustomerName(customerDto.getCustomerName());
        customer.setSurname(customerDto.getSurname());
        customer.setOib(customerDto.getOib());
        customer.setAddress(customerDto.getAddress());
        customer.setTelephone(customerDto.getTelephone());
        customer.setCity(customerDto.getCity());
        customer.setInvoices(customerDto.getInvoices());
        return customer;
    }
    public List<CustomerDto> ConvertEntityToDto(List<Customer> customers) {
        return customers.stream().map(x -> ConvertEntityToDto(x)).collect(Collectors.toList());
    }
    public List<Customer> ConvertDtoToEntity(List<CustomerDto> customerDtos) {
        return customerDtos.stream().map(x -> ConvertDtoToEntity(x)).collect(Collectors.toList());
    }
    public CustomerDto ConvertEntityToDto(Customer customer) {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(customer.getId());
        customerDto.setCustomerName(customer.getCustomerName());
        customerDto.setSurname(customer.getSurname());
        customerDto.setOib(customer.getOib());
        customerDto.setAddress(customer.getAddress());
        customerDto.setCity(customer.getCity());
        customerDto.setTelephone(customer.getTelephone());
        customerDto.setInvoices(customer.getInvoices());
        return customerDto;
    }
}
