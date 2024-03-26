package com.ems.EmployeeMS.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
public class Employee {

    private long employee_id;
    private String name;
    private String email;

    private String address;
    private String phone;
    private String password;
    private Role role = Role.ADMIN;

    public Employee(long employee_id, String name, String email, String address, String phone, String password, Role role) {
        this.employee_id = employee_id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.password = password;
        this.role = role;
    }

    public Employee() {
    }

    public long getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(long employee_id) {
        this.employee_id = employee_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Collection<? extends GrantedAuthority> getRoles() {
        return new ArrayList<>() {
            {
                add(new GrantedAuthority() {
                    @Override
                    public String getAuthority() {
                        return role.name();
                    }
                });
            }
        };
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employee_id=" + employee_id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
