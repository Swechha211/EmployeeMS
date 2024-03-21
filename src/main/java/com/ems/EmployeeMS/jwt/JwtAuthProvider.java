package com.ems.EmployeeMS.jwt;

import com.ems.EmployeeMS.entities.CustomEmployeeDetails;
import com.ems.EmployeeMS.entities.Employee;
import com.ems.EmployeeMS.repositories.EmployeeRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class JwtAuthProvider implements AuthenticationProvider {

    private final EmployeeRepository employeeRepository;

    public JwtAuthProvider(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if(!authentication.isAuthenticated()){
            UserDetails userDetails = loadUserByUsername(authentication.getName());
            return new UsernamePasswordAuthenticationToken(userDetails.getUsername(),userDetails.getPassword(), userDetails.getAuthorities());
        }
        return  authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }



        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            Employee employee = employeeRepository.getEmployeeByname(username);
            if(employee == null){
                throw  new UsernameNotFoundException("Employee is not found");
            }
            return new CustomEmployeeDetails(employee.getName(), "", employee.getRoles());
        }


//    static  class DomainUserDetailService implements UserDetailsService{
//        @Override
//        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//            String role = username.equals("swechha") ? "ROLE_ADMIN" : "ROLE_USER";
//            List<SimpleGrantedAuthority> simpleGrantedAuthorities = Collections.singletonList(new SimpleGrantedAuthority(role));
//            return new User(username, "", simpleGrantedAuthorities);
//        }
//    }
}
