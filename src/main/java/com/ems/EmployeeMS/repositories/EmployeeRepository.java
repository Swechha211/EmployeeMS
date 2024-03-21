package com.ems.EmployeeMS.repositories;

import com.ems.EmployeeMS.entities.Employee;

import java.util.List;

public interface EmployeeRepository {
    public Employee saveEmployee(Employee employee);
    public Employee getEmployeeByID(Long employee_id);
    public List<Employee> getAllEmployee();
    public void updateEmployee (Long employee_id, Employee employee);
    public void deleteEmployee(Long employee_id);

    Employee getEmployeeByname(String name);


}
