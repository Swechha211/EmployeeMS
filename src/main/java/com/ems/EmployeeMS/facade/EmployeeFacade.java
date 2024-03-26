package com.ems.EmployeeMS.facade;

import com.ems.EmployeeMS.entities.Employee;
import com.ems.EmployeeMS.entities.LoginModel;
import com.ems.EmployeeMS.jwt.JwtTokenResponse;
import com.ems.EmployeeMS.services.EmployeeService;
import com.grpc.EmployeeOuterClass;
import com.grpc.Schema;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeFacade {

    private final EmployeeService employeeService;

    public EmployeeFacade(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    public EmployeeOuterClass.Employee saveEmployee(EmployeeOuterClass.Employee employee){
        System.out.println("Employee from input = "+employee);
        Employee employee1 = mapToEntity(employee);
        System.out.println("Employee after mapping to employee entity  = "+employee1);
        EmployeeOuterClass.Employee employee2= mapToOuterClass(employeeService.saveEmployee(employee1));
        System.out.println(" Employee after mapping to outerclass  = "+employee2);
        return employee2;
    }

    public EmployeeOuterClass.Employee getEmployeetById(Long employee_id) {
        System.out.println("Employee id= "+employee_id);
        return mapToOuterClass(employeeService.getEmployeeByID(employee_id));
    }

    public List<EmployeeOuterClass.Employee> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployee();
        return employees.stream()
                .map(this::mapToOuterClass)
                .collect(Collectors.toList());
    }

    public void deleteEmployee(Long employeeId) {
        System.out.println("Employee id to delete = " + employeeId);
        employeeService.deleteEmployee(employeeId);
    }

    public EmployeeOuterClass.Employee updateEmployee(Long employee_id, EmployeeOuterClass.Employee employee) {
        Employee updatedEmployee = employeeService.updateEmployee(employee_id, mapToEntity(employee));
        return mapToOuterClass(updatedEmployee);
    }

//    public Schema.JwToken login(Schema.JwtRequest jwtRequest){
//        Schema.JwToken employee = employeeService.login(jwtRequest);
//        return employee;
//    }

    public Schema.LoginResponse login(Schema.LoginRequest loginRequest){
//        LoginModel loginModel = mapToLoginModel(loginRequest);
//        JwtTokenResponse loginModel1 = employeeService.login(loginModel);
//        return  Schema.LoginResponse.newBuilder()
//                .setJwtToken(employeeService.login(loginModel).getToken())
//                .build();

        LoginModel loginModel = mapToLoginModel(loginRequest);
        JwtTokenResponse jwtTokenResponse = employeeService.login(loginModel);

        if (jwtTokenResponse != null) {
            String jwtToken = jwtTokenResponse.getToken();
            return Schema.LoginResponse.newBuilder()
                    .setJwtToken(jwtToken)
                    .build();
        } else {
            // Handle the case where login fails and returns null
            // You can throw an exception, return a default response, or handle it based on your application's logic
            // For example:
            throw new RuntimeException("Login failed. Token not received.");
        }
    }




    private Employee mapToEntity(EmployeeOuterClass.Employee employee){
        Employee employee1 = new Employee();
        employee1.setEmployee_id(employee.getEmployeeId());
        employee1.setName(employee.getName());
        employee1.setEmail(employee.getEmail());
        employee1.setAddress(employee.getAddress());
        employee1.setPhone(employee.getPhone());
        employee1.setPassword(employee.getPassword());
        return employee1;
    }

    private EmployeeOuterClass.Employee mapToOuterClass(Employee employee){
        EmployeeOuterClass.Employee.Builder employeeBuilder = EmployeeOuterClass.Employee.newBuilder();
        employeeBuilder.setEmployeeId(employee.getEmployee_id())
                .setName(employee.getName())
                .setEmail(employee.getEmail())
                .setAddress(employee.getAddress())
                .setPhone(employee.getPhone())
                .setPassword(employee.getPassword());

        return employeeBuilder.build();
    }

    private LoginModel mapToLoginModel(Schema.LoginRequest loginRequest) {
        return new LoginModel(loginRequest.getUsername(), loginRequest.getPassword());
    }


}
