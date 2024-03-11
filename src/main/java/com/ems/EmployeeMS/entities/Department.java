package com.ems.EmployeeMS.entities;

public class Department {
    private Long department_id;
    private String name;

    public Department(Long department_id, String name) {
        this.department_id = department_id;
        this.name = name;
    }

    public Department() {

    }

    public Long getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(Long department_id) {
        this.department_id = department_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
