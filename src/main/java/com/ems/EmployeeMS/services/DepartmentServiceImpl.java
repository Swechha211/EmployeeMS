package com.ems.EmployeeMS.services;

import com.ems.EmployeeMS.entities.Department;
import com.ems.EmployeeMS.repositories.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DepartmentServiceImpl implements DepartmentService{
    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }


    @Override
    public Department saveDepartment(Department department) {
        return departmentRepository.saveDepartment(department);
    }

    @Override
    public Department getDepartmentByID(Long department_id) {
        return departmentRepository.getDepartmentByID(department_id);
    }

    @Override
    public List<Department> getAllDepartment() {
        return departmentRepository.getAllDepartment();
    }

    @Override
    public Department updateDepartment(Long department_id, Department department) {
        departmentRepository.updateDepartment(department_id, department);

        return department;
    }

    @Override
    public void deleteDepartment(Long department_id) {
        departmentRepository.deleteDepartment(department_id);

    }
}
