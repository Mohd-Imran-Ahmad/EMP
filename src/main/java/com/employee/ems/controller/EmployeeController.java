package com.employee.ems.controller;


import com.employee.ems.entity.Employee;
import com.employee.ems.repository.EmployeeRepository;
import com.employee.ems.service.EmployeeService;
import com.employee.ems.service.ExcelGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
//@CrossOrigin
//        (
//        origins = "http://localhost:4200",
//        methods = {RequestMethod.GET,
//                RequestMethod.POST,
//                RequestMethod.PUT,
//                RequestMethod.DELETE,
//                RequestMethod.OPTIONS},
//        allowedHeaders = "*",
////        allowedHeaders = {"Authorization",
////                "Content-Type",
////                "X-Requested-With"},
//        allowCredentials = "true"
//)

public class EmployeeController {

    @Autowired
    private EmployeeService service;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ExcelGeneratorService excelGeneratorService;

    @PostMapping("/create")
    public Employee createEmployee(@RequestBody Employee employee) {

        return service.create(employee);
    }

    @GetMapping("/getAll")
    public List<Employee> getAllEmployees() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable Long id) {
        return service.getById(id).orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        return service.update(id, employee);
    }

    @DeleteMapping("/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        service.delete(id);
        return "Deleted successfully";
    }

//    @GetMapping("/passengers/excel")
//    public ResponseEntity<byte[]> generateExcel() {
//        try {
//            // Assuming you have a method to fetch passengers from database
//            List<Employee> employee = fetchEmployeesFromDatabase();
//            byte[] excelBytes = excelGeneratorService.generateExcel(employee);
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//            headers.setContentDispositionFormData("attachment", "employee_data.xlsx");
//
//            return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    // Dummy method to fetch passengers (replace with actual data retrieval logic)
//    private List<Employee> fetchEmployeesFromDatabase() {
//        // Implement your data retrieval logic here
//        return employeeRepository.findAll();
//    }

    // Both excel code work

    @GetMapping("/passengers/excel")
    public ResponseEntity<byte[]> generateExcel() {
        try {
            List<Employee> employees = fetchEmployeesFromDatabase();
            byte[] excelBytes = excelGeneratorService.generateExcel(employees);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", "employee_data.xlsx");
            headers.setContentLength(excelBytes.length);

            return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<Employee> fetchEmployeesFromDatabase() {
        return employeeRepository.findAll();
    }


}
