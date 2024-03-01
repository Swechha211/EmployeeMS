package com.ems.EmployeeMS.facade;

import com.ems.EmployeeMS.entities.Employee;
import com.ems.EmployeeMS.services.EmployeeService;
import com.grpc.EmployeeOuterClass;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

public class EmployeeFacade {

    private final EmployeeService employeeService;
    private final ModelMapper modelMapper;

    public EmployeeFacade(EmployeeService employeeService, ModelMapper modelMapper) {
        this.employeeService = employeeService;
        this.modelMapper = modelMapper;
    }


}
