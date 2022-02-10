package com.CompanyManagement.api;

import com.CompanyManagement.persistence.entities.Employee;
import com.CompanyManagement.persistence.entities.UserRole;
import com.CompanyManagement.persistence.repositories.EmployeeRepository;
import com.CompanyManagement.service.EmployeeService;
import com.CompanyManagement.service.EmployeeServiceImpl;
import com.CompanyManagement.web.dto.EmployeeDto;
import com.CompanyManagement.web.dto.UserRoleDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@AllArgsConstructor
@Controller
@RequestMapping("/employees/")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmployeeServiceImpl employeeServiceimpl;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private EmployeeDto employeeDto;
    @Autowired
    private UserRoleDto userRoleDto;

    @GetMapping("listOfEmployees")
    public String viewHomePage(UserRole userRole, Model model){
        Collection<UserRoleDto> listRoles = employeeServiceimpl.listRoles();
        model.addAttribute("listRoles", listRoles);
        model.addAttribute("employees", employeeDto.ConvertEntityToDto(employeeRepository.findAll()));
        return  "employee-list";
    }

    @GetMapping("ShowNewEmployeeForm")
    public String showNewEmployeeForm(Employee employee) {
        return "new_employee";
    }

    @PostMapping("add")
    public String addEmployee(@Valid EmployeeDto employee, BindingResult result, Model model) {
        if(result.hasErrors()) {
            return "new_employee";
        }
        Employee employee1 = new Employee(employee.getEmployeeName(), employee.getSurname(), employee.getOib(), employee.getAddress(), employee.getEmail(), passwordEncoder.encode(employee.getPasswd()), null);
        employeeRepository.save(employee1);
        return "redirect:viewPage";
    }

    @PostMapping("update/{id}")
    public String updateEmployee(UserRole userRole, @PathVariable("id") UUID id, @Valid EmployeeDto employee, BindingResult result,
                                 Model model) {
        Collection<UserRoleDto> listRoles = employeeService.listRoles();
        if (result.hasErrors()) {
            employee.setId(id);
            return "redirect:viewPage";
        }

        employeeRepository.save(employeeDto.ConvertDtoToEntity(employee));
        model.addAttribute("employees", employeeDto.ConvertEntityToDto(employeeRepository.findAll()));
        model.addAttribute("listRoles", listRoles);
        return "redirect:/employees/viewPage";
    }

    @GetMapping("edit/{id}")
    public String showUpdateForm(UserRole userRole, @PathVariable("id") UUID id, Model model) {
        Collection<UserRoleDto> listRoles = employeeServiceimpl.listRoles();
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee Id:" + id));
        model.addAttribute("employee", employeeDto.ConvertEntityToDto(employee));
        model.addAttribute("listRoles", listRoles);
        return "update-employee";
    }

    @GetMapping("delete/{id}")
    public String deleteEmployee(@PathVariable("id") UUID id, Model model) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student Id:" + id));
        employeeService.deleteEmployee(id);
        model.addAttribute("employees", employeeDto.ConvertEntityToDto(employeeRepository.findAll()));
        return "redirect:/employees/viewPage";
    }

    @RequestMapping("search")
    public String viewHomePage(Model model, @Param("keyword") String keyword) {
        List<EmployeeDto> listEmployees = employeeServiceimpl.findBySurnameIgnoreCase(keyword);
        model.addAttribute("listEmployees", listEmployees);
        model.addAttribute("keyword", keyword);

        return "employee-search";
    }

    @RequestMapping("viewPage")
    public String viewPage(Model model) {
        return listByPage(model, 1, "surname", "asc");
    }

    @GetMapping("/page/{pageNumber}")
    public String listByPage(Model model,
                             @PathVariable("pageNumber") int currentPage,
                             @Param("sortField") String sortField,
                             @Param("sortDir") String sortDir) {

        Page<Employee> page = employeeServiceimpl.listAll(currentPage, sortField, sortDir);
        int totalElements = (int) page.getTotalElements();
        int totalPages = page.getTotalPages();

        List<Employee> listEmployee = page.getContent();

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("listEmployee", listEmployee);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("sortDir", sortDir);

        String reverseSortDir =sortDir.equals("asc") ? "desc" : "asc";
        model.addAttribute("reverseSortDir", reverseSortDir);


        return "employee-list";
    }
}