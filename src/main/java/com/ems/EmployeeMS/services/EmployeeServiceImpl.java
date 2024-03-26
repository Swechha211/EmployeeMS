package com.ems.EmployeeMS.services;

import com.ems.EmployeeMS.entities.Employee;
import com.ems.EmployeeMS.entities.LoginModel;
import com.ems.EmployeeMS.jwt.JwtAuthProvider;
import com.ems.EmployeeMS.jwt.JwtTokenResponse;
import com.ems.EmployeeMS.jwt.JwtTokenUtil;
import com.ems.EmployeeMS.repositories.EmployeeRepository;
import com.grpc.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService{
    private final EmployeeRepository employeeRepository;
    private final JwtAuthProvider jwtAuthProvider;
    private final JwtTokenUtil jwtTokenUtil;
    private final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, JwtAuthProvider jwtAuthProvider, JwtTokenUtil jwtTokenUtil) {
        this.employeeRepository = employeeRepository;
        this.jwtAuthProvider = jwtAuthProvider;
        this.jwtTokenUtil = jwtTokenUtil;
    }


    @Override
    public Employee saveEmployee(Employee employee) {
       return employeeRepository.saveEmployee(employee);
    }

    @Override
    public Employee getEmployeeByID(Long employee_id) {
        return employeeRepository.getEmployeeByID(employee_id);
    }

    @Override
    public List<Employee> getAllEmployee() {
        return employeeRepository.getAllEmployee();
    }

    @Override
    public Employee updateEmployee(Long employee_id, Employee employee) {
        employeeRepository.updateEmployee(employee_id, employee);

        return employee;
    }

    @Override
    public void deleteEmployee(Long employee_id) {
        employeeRepository.deleteEmployee(employee_id);

    }

    @Override
    public JwtTokenResponse login(LoginModel loginModel) {
        UserDetails userDetails = jwtAuthProvider.loadUserByUsername(loginModel.getUsername());
        if (userDetails == null) {
            throw new UsernameNotFoundException("User not found with username: " + loginModel.getUsername());
        }

        logger.info("User logged in successfully!!");
        Authentication authentication = jwtAuthProvider.authenticate(new UsernamePasswordAuthenticationToken(loginModel.getUsername(), loginModel.getPassword()));
        String token = jwtTokenUtil.generateToken(authentication);
        return new JwtTokenResponse(token);
//        return null;
    }

//    @Override
//    public Schema.JwToken login(Schema.JwtRequest jwtRequest) {
////        UserDetails userDetails = jwtAuthProvider.loadUserByUsername(user.getUsername());
//        return null;
//    }


}
