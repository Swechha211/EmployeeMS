package com.ems.EmployeeMS.services;

import com.ems.EmployeeMS.entities.Employee;
import com.ems.EmployeeMS.repositories.EmployeeRepository;
import org.springframework.stereotype.Service;

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
    public Employee updateEmployee(Long employee_id, Employee employee) {
        employeeRepository.updateEmployee(employee_id, employee);

        return employee;
    }

    @Override
    public void deleteEmployee(Long employee_id) {
        employeeRepository.deleteEmployee(employee_id);

    }


}
