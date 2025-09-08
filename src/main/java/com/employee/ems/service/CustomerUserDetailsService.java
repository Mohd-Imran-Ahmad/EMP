package com.employee.ems.service;


import com.employee.ems.entity.EmployeeRegistration;
import com.employee.ems.repository.EmployeeRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomerUserDetailsService implements UserDetailsService {

    @Autowired
    private EmployeeRegistrationRepository employeeRegistrationRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        EmployeeRegistration user = employeeRegistrationRepository.findByUsername(username);

        return new org.springframework.security.core.userdetails.User(user.getUsername() ,user.getPassword(), Collections.singleton(new
                SimpleGrantedAuthority(user.getRole())));
    }

}