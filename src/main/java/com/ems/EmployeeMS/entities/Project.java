package com.ems.EmployeeMS.entities;

import java.util.Date;

public class Project {
    private long project_id;
    private String name;
    private Date start_date;
    private Date end_date;
    private Employee employee;
    private Department department;

    public Project(long project_id, String name, Date start_date, Date end_date, Employee employee, Department department) {
        this.project_id = project_id;
        this.name = name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.employee = employee;
        this.department = department;
    }

    public Project() {
    }

    public long getProject_id() {
        return project_id;
    }

    public void setProject_id(long project_id) {
        this.project_id = project_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}
