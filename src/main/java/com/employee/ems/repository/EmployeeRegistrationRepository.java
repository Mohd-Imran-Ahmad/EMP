package com.employee.ems.repository;


import com.employee.ems.entity.EmployeeRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
public interface EmployeeRegistrationRepository extends JpaRepository<EmployeeRegistration, Long> {
    EmployeeRegistration findByUsername(String username);
}
