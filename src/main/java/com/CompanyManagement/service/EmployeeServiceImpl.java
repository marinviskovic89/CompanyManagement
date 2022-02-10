package com.CompanyManagement.service;

import com.CompanyManagement.persistence.entities.Employee;
import com.CompanyManagement.persistence.entities.UserRole;

import java.util.*;
import java.util.stream.Collectors;

import com.CompanyManagement.persistence.repositories.EmployeeRepository;
import com.CompanyManagement.persistence.repositories.UserRoleRepository;
import com.CompanyManagement.web.EmployeeRegistrationDto;
import com.CompanyManagement.web.dto.EmployeeDto;
import com.CompanyManagement.web.dto.UserRoleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Pattern;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private EmployeeDto employeeDto;
    @Autowired
    private UserRoleDto userRoleDto;


    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<UserRoleDto> listRoles() {
        return userRoleDto.ConvertEntityToDto(userRoleRepository.findAll());
    }

    public List<EmployeeDto> findBySurnameIgnoreCase(@Pattern(regexp = "[A-Za-z]") String keyword) {
        var employees = employeeDto.ConvertEntityToDto(employeeRepository.findAll());
        var employeeList = new ArrayList<EmployeeDto>();

        employees.forEach(e -> {
            if(e.getSurname().toLowerCase().contains(keyword.toLowerCase())) {
                employeeList.add(e);
            }
        });
        return employeeList;
    }

    public Page<Employee> listAll(int pageNumber, String sortField, String sortDir) {
        Sort sort = Sort.by("surname");
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNumber - 1, 2, sort);
        return employeeRepository.findAll(pageable);
    }

    @Override
    public Employee save(EmployeeRegistrationDto registrationDto) {
        Employee employee = new Employee(registrationDto.getFirstName(), registrationDto.getLastName(), registrationDto.getOib(), registrationDto.getAddress(), registrationDto.getEmail(), passwordEncoder.encode(registrationDto.getPasswd()), null);
        return employeeRepository.save(employee);                                           /*Arrays.asList(new UserRole("USER")));*/
    }

        @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        EmployeeDto employee = employeeDto.ConvertEntityToDto(employeeRepository.findByEmail(s));
        if(employee == null) {
            throw new UsernameNotFoundException("Invalid username or password");
        }
        return new org.springframework.security.core.userdetails.User(employee.getEmail(), employee.getPasswd(),mapRolesToAuthorities(employee.getRoles()));
    }
    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<UserRole> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toList());
    }

    public Employee createEmployee(EmployeeDto employee) {
        return employeeRepository.save(employeeDto.ConvertDtoToEntity(employee));
    }

    public List<EmployeeDto> getEmployees() {
        return employeeDto.ConvertEntityToDto(employeeRepository.findAll());
    }

    public Employee findByOib(long oib) {
        return employeeRepository.findByOib(oib);
    }

    @Override
    public String deleteEmployee(UUID id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            employee.get().removeRoles();
            employeeRepository.deleteById(employee.get().getId());
            return "Employee with id: " + id + " deleted successfully!";
        }
        return null;
    }

    public Employee get(UUID id) {
        return employeeRepository.findById(id).get();
    }

    public void updateEmployee(EmployeeDto newEmployee, UUID id) {
        var e = employeeDto.ConvertEntityToDto(employeeRepository.findById(id).orElse(null));


        e.setEmployeeName(newEmployee.getEmployeeName());
        e.setSurname(newEmployee.getSurname());
        e.setOib(newEmployee.getOib());
        e.setAddress(newEmployee.getAddress());
        e.setEmail(newEmployee.getEmail());
        e.setPasswd(newEmployee.getPasswd());

        employeeRepository.save(employeeDto.ConvertDtoToEntity(e));
    }

    @Override
    public Employee findByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }

    public boolean emailExists(String email) {
        var employees  = employeeDto.ConvertEntityToDto(employeeRepository.findAll());

        for(EmployeeDto e : employees) {
            if(e.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

}
