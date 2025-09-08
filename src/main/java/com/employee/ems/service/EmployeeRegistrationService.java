package com.employee.ems.service;


import com.employee.ems.dto.EmployeeRegistrationDto;
import com.employee.ems.entity.EmployeeRegistration;
import com.employee.ems.repository.EmployeeRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EmployeeRegistrationService {

    @Autowired
    private EmployeeRegistrationRepository employeeRegistrationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public EmployeeRegistrationDto createUser(EmployeeRegistrationDto employeeRegistrationDto) {
        EmployeeRegistration empl = new EmployeeRegistration();
        empl.setUsername(employeeRegistrationDto.getUsername());
        empl.setPassword(passwordEncoder.encode(employeeRegistrationDto.getPassword()));
        empl.setEmail(employeeRegistrationDto.getEmail());
        empl.setMobileNumber((employeeRegistrationDto.getMobileNumber()));
        empl.setRole("ROLE_ADMIN");

        EmployeeRegistration savedUser = employeeRegistrationRepository.save(empl);


        EmployeeRegistrationDto  responseDto = new EmployeeRegistrationDto ();
        responseDto.setUsername(savedUser.getUsername());
        responseDto.setEmail(savedUser.getEmail());
        responseDto.setMobileNumber(savedUser.getMobileNumber());

        return responseDto;
    }
}
