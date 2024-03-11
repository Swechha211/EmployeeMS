package com.ems.EmployeeMS.services;

import com.ems.EmployeeMS.entities.Department;

import java.util.List;

public interface DepartmentService {
    public Department saveDepartment(Department department);
    public Department getDepartmentByID(Long department_id);
    public List<Department> getAllDepartment();
    public Department updateDepartment(Long department_id, Department department);
    public void deleteDepartment(Long department_id);
}
