package com.CompanyManagement.api;

import com.CompanyManagement.service.EmployeeService;
import com.CompanyManagement.service.EmployeeServiceImpl;
import com.CompanyManagement.web.EmployeeRegistrationDto;
import com.CompanyManagement.web.dto.EmployeeDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registration")
public class EmployeeRegistrationController {

    private EmployeeService employeeService;

    public EmployeeRegistrationController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @ModelAttribute("employee")
    public EmployeeRegistrationDto registrationDto(){
        return new EmployeeRegistrationDto();
    }

    @GetMapping
    public String showRegistrationForm(){
        return "registration";
    }

    @PostMapping
    public String registerEmployeeAccount(@ModelAttribute("employee") EmployeeRegistrationDto registration, String email){
        var checkEmail = employeeService.emailExists(email);
        if(checkEmail) {
            return "redirect:/registration?failure";
        }
        employeeService.save(registration);
        return "redirect:/registration?success";
    }
}