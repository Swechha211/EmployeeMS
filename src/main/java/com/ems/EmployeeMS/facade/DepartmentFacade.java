package com.ems.EmployeeMS.facade;

import com.ems.EmployeeMS.entities.Department;
import com.ems.EmployeeMS.entities.Employee;
import com.ems.EmployeeMS.services.DepartmentService;
import com.grpc.DepartmentOuterClass;
import com.grpc.EmployeeOuterClass;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentFacade {
    private final DepartmentService departmentService;

    public DepartmentFacade(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    public DepartmentOuterClass.Department  saveDepartment(DepartmentOuterClass.Department department){
        System.out.println("Department from input = "+department);
        Department department1 = mapToEntity(department);
        System.out.println("Department after mapping to Department entity  = "+department1);
        DepartmentOuterClass.Department department2= mapToOuterClass(departmentService.saveDepartment(department1));
        System.out.println(" Department after mapping to outerclass  = "+department2);
        return department2;
    }

//    @PreAuthorize("hasRole('USER')")
    public DepartmentOuterClass.Department getDepartmentById(Long department_id) {
        System.out.println("Department id= "+department_id);
        return mapToOuterClass(departmentService.getDepartmentByID(department_id));
    }

    public void deleteDepartment(Long departmentId) {
        System.out.println("Department id to delete = " + departmentId);
        departmentService.deleteDepartment(departmentId);
    }



    public List<DepartmentOuterClass.Department> getAllDepartments() {
//        List<Department> departments = departmentService.getAllDepartment();
//        return departments.stream()
//                .map(this::mapToOuterClass)
//                .collect(Collectors.toList());

        List<Department> departments = departmentService.getAllDepartment();
        List<DepartmentOuterClass.Department> departmentOuterList = new ArrayList<>();


        if (departments != null) {
            for (Department department : departments) {
                DepartmentOuterClass.Department departmentOuter = mapToOuterClass(department);
                departmentOuterList.add(departmentOuter);
            }
        }

        return departmentOuterList;
    }

    public DepartmentOuterClass.Department updateDepartment(Long department_id,DepartmentOuterClass.Department department) {
        Department updatedDepartment = departmentService.updateDepartment(department_id, mapToEntity(department));
        return mapToOuterClass(updatedDepartment);
    }

    private Department mapToEntity(DepartmentOuterClass.Department department){
        Department department1 = new Department();
        department1.setDepartment_id(department.getDepartmentId());
        department1.setName(department.getName());

        return department1;
    }

    private DepartmentOuterClass.Department mapToOuterClass(Department department){
        DepartmentOuterClass.Department.Builder builder = DepartmentOuterClass.Department.newBuilder();
        builder.setDepartmentId(department.getDepartment_id())
                .setName(department.getName());

        return builder.build();
    }
}
