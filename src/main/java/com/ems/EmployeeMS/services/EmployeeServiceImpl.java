package com.ems.EmployeeMS.services;

import com.ems.EmployeeMS.entities.Employee;
import com.ems.EmployeeMS.repositories.EmployeeRepository;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService{
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }


    @Override
    public Employee saveEmployee(Employee employee) {
       return employeeRepository.saveEmployee(employee);
    }

    @Override
    public Employee getEmployeeByID(Long employee_id) {
        return employeeRepository.getEmployeeByID(employee_id);
    }

    @Override
    public List<Employee> getAllEmployee() {
        return employeeRepository.getAllEmployee();
    }

    @Override
    public void updateEmployee(Long employee_id, Employee employee) {
        employeeRepository.updateEmployee(employee_id,employee);

    }

    @Override
    public void deleteEmployee(Long employee_id) {
        employeeRepository.deleteEmployee(employee_id);

    }
}
