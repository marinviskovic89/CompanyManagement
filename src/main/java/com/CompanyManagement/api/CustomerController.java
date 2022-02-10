package com.CompanyManagement.api;

import com.CompanyManagement.persistence.entities.Customer;
import com.CompanyManagement.persistence.repositories.CustomerRepository;
import com.CompanyManagement.service.CustomerService;
import com.CompanyManagement.web.dto.CustomerDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Controller
@RequestMapping("/customers/")
public class CustomerController {

    private final CustomerRepository customerRepository;

    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerDto customerDto;

    @Autowired
    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    @GetMapping("signup")
    public String showSignUpForm(Customer customer) {
        return "customer-add";
    }

    @PostMapping("addCustomer")
    public String addCustomer(@Valid CustomerDto customer, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "customer-add";
        }
        customerRepository.save(customerDto.ConvertDtoToEntity(customer));
        return "redirect:viewPage";
    }

    @GetMapping("edit/{id}")
    public String showUpdateForm(@PathVariable("id") UUID id, Model model) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));
        model.addAttribute("customer", customerDto.ConvertEntityToDto(customer));
        return "update-customer";
    }

    @PostMapping("update/{id}")
    public String updateCustomer(@PathVariable("id") UUID id, @Valid Customer customer, BindingResult result,
                                 Model model) {
        if (result.hasErrors()) {
            customer.setId(id);
            return "redirect:viewPage";
        }
        customerRepository.save(customer);
        model.addAttribute("customers", customerRepository.findAll());
        return "redirect:/customers/viewPage";
    }

    @GetMapping("delete/{id}")
    public String deleteCustomer(@PathVariable("id") UUID id, Model model) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));
        customerService.deleteCustomerById(id);
        model.addAttribute("customers", customerRepository.findAll());
        return "redirect:/customers/viewPage";
    }

    @RequestMapping("/viewPPage")
    public String viewPPage(Model model, @Param("keyword")String keyword) {
        return findBySurnameIgnoreCase(model, keyword,1, "surname", "asc");
    }

    @RequestMapping("search")
    public String findBySurnameIgnoreCase(Model model, @Param("keyword") String keyword,
                                             @PathVariable("pageNumber") int currentPage,
                                             @Param("sortField") String sortField,
                                             @Param("sortDir") String sortDir) {

        List<Customer> listCustomers = customerService.findBySurnameIgnoreCase(keyword);
        model.addAttribute("listCustomers", listCustomers);
        model.addAttribute("keyword", keyword);

        Page<Customer> page = customerService.listAll(currentPage, sortField, sortDir);
        int totalElements = (int) page.getTotalElements();
        int totalPages = page.getTotalPages();

        listCustomers = page.getContent();

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("sortDir", sortDir);

        String reverseSortDir =sortDir.equals("asc") ? "desc" : "asc";
        model.addAttribute("reverseSortDir", reverseSortDir);

        return "customer-search";
    }

    @GetMapping("listOfCustomers")
    public List<CustomerDto> getCustomers(Model model) {
        model.addAttribute("customers", customerRepository.findAll());

        return customerService.getCustomers();
    }

    //NOVI PAGING&SORTING
    @RequestMapping("/viewPage")
    public String viewPage(Model model) {
        return listByPage(model, 1, "surname", "asc");
    }

    @GetMapping("/page/{pageNumber}")
    public String listByPage(Model model,
                             @PathVariable("pageNumber") int currentPage,
                             @Param("sortField") String sortField,
                             @Param("sortDir") String sortDir) {

        Page<Customer> page = customerService.listAll(currentPage, sortField, sortDir);
        int totalElements = (int) page.getTotalElements();
        int totalPages = page.getTotalPages();

        List<Customer> listCustomer = page.getContent();

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("listCustomer", listCustomer);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("sortDir", sortDir);

        String reverseSortDir =sortDir.equals("asc") ? "desc" : "asc";
        model.addAttribute("reverseSortDir", reverseSortDir);


        return "customer-list";
    }

}
