package com.employee.ems.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @NotNull
    @Column(name = "email", nullable = false, unique = true, length = 50)
    private String email;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "role")
    private String role;

}
