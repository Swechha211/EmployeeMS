package com.ems.EmployeeMS.services;

import com.ems.EmployeeMS.entities.Employee;

import java.util.List;

public interface EmployeeService {
    public Employee saveEmployee(Employee employee);
    public Employee getEmployeeByID(Long employee_id);
    public List<Employee> getAllEmployee();
    public void updateEmployee (Long employee_id, Employee employee);
    public void deleteEmployee(Long employee_id);
}
