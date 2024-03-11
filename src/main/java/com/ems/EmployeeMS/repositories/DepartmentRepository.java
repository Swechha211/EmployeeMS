package com.ems.EmployeeMS.repositories;

import com.ems.EmployeeMS.entities.Department;

import java.util.List;

public interface DepartmentRepository {
    public Department saveDepartment(Department department);
    public Department getDepartmentByID(Long department_id);
    public List<Department> getAllDepartment();
    public void updateDepartment(Long department_id, Department department);
    public void deleteDepartment(Long department_id);
}
