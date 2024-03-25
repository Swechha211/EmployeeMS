package com.ems.EmployeeMS.config;

import com.ems.EmployeeMS.entities.CustomEmployeeDetails;
import com.ems.EmployeeMS.entities.Role;
import com.ems.EmployeeMS.jwt.JwtAuthProvider;
import com.ems.EmployeeMS.jwt.JwtTokenUtil;
import com.grpc.AuthServiceGrpc;
//import com.grpc.EmployeeJwtServiceGrpc;
import com.grpc.EmployeeServiceGrpc;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import net.devh.boot.grpc.server.security.authentication.BearerAuthenticationReader;
import net.devh.boot.grpc.server.security.authentication.GrpcAuthenticationReader;
import net.devh.boot.grpc.server.security.check.AccessPredicate;
import net.devh.boot.grpc.server.security.check.AccessPredicateVoter;
import net.devh.boot.grpc.server.security.check.GrpcSecurityMetadataSource;
import net.devh.boot.grpc.server.security.check.ManualGrpcSecurityMetadataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class SecurityConfig {

    private final JwtAuthProvider jwtAuthProvider;
    private final JwtTokenUtil jwtTokenUtil;


    private final UserDetailsService userDetailsService;

    public SecurityConfig(JwtAuthProvider jwtAuthProvider, JwtTokenUtil jwtTokenUtil, UserDetailsService userDetailsService) {
        this.jwtAuthProvider = jwtAuthProvider;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    AuthenticationManager authenticationManager() {
        return new ProviderManager(jwtAuthProvider);
    }

    @Bean
    GrpcAuthenticationReader grpcAuthenticationReader() {
        return new BearerAuthenticationReader(token ->
        {
            String username = null;
            if (token != null) {
                try {
                    username = jwtTokenUtil.extractUsername(token);
                } catch (SignatureException e) {
                    throw new SignatureException("Invalid JWT token");
                }
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (!jwtTokenUtil.isTokenExpired(token) && jwtTokenUtil.validateToken(token, userDetails)) {
                    Claims claims = jwtTokenUtil.extractAllClaims(token);
                    List<SimpleGrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(","))
                            .map(SimpleGrantedAuthority::new)
                            .toList();
                    CustomEmployeeDetails user = new CustomEmployeeDetails(claims.getSubject(), userDetails.getPassword(), authorities);
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, user.getPassword(), authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    return authenticationToken;
                } else {
                    throw new SignatureException("Invalid JWT token");
                }
            }
            return null;
        });

    }


    @Bean
    GrpcSecurityMetadataSource grpcSecurityMetadataSource() {
        ManualGrpcSecurityMetadataSource manualGrpcSecurityMetadataSource = new ManualGrpcSecurityMetadataSource();
        manualGrpcSecurityMetadataSource.setDefault(AccessPredicate.permitAll());

        manualGrpcSecurityMetadataSource.set(AuthServiceGrpc.getServiceDescriptor(), AccessPredicate.permitAll());

        manualGrpcSecurityMetadataSource.set(EmployeeServiceGrpc.getServiceDescriptor(), AccessPredicate.hasAnyRole(Role.SUPER_ADMIN.name(), Role.ADMIN.name()));


//        manualGrpcSecurityMetadataSource.set(EmployeeJwtServiceGrpc.getGetEmlployeeJwtInfoMethod(), AccessPredicate.hasRole("ROLE_ADMIN"));
        return manualGrpcSecurityMetadataSource;
    }

//    @Bean
//    AccessDecisionManager accessDecisionManager() {
//        List<AccessDecisionVoter<?>> accessDecisionVoters = new ArrayList<>();
//        accessDecisionVoters.add(new AccessPredicateVoter());
//        return new UnanimousBased(accessDecisionVoters);
//
//    }
}










//package com.ems.EmployeeMS.config;
//
//import com.ems.EmployeeMS.jwt.JwtAuthProvider;
//import com.grpc.EmployeeJwtServiceGrpc;
//import io.jsonwebtoken.JwtParserBuilder;
//import io.jsonwebtoken.Jwts;
//import net.devh.boot.grpc.server.security.authentication.BearerAuthenticationReader;
//import net.devh.boot.grpc.server.security.authentication.GrpcAuthenticationReader;
//import net.devh.boot.grpc.server.security.check.AccessPredicate;
//import net.devh.boot.grpc.server.security.check.AccessPredicateVoter;
//import net.devh.boot.grpc.server.security.check.GrpcSecurityMetadataSource;
//import net.devh.boot.grpc.server.security.check.ManualGrpcSecurityMetadataSource;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.security.SecurityProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.access.AccessDecisionManager;
//import org.springframework.security.access.AccessDecisionVoter;
//import org.springframework.security.access.vote.UnanimousBased;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.ProviderManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Configuration
//public class SecurityConfig {
//    @Value("${jwt.signing.key}")
//    String jwtSecretKey;
//    private final JwtAuthProvider jwtAuthProvider;
//
//    public SecurityConfig(JwtAuthProvider jwtAuthProvider) {
//        this.jwtAuthProvider = jwtAuthProvider;
//    }
//
//    @Bean
//    AuthenticationManager authenticationManager() {
//        return new ProviderManager(jwtAuthProvider);
//    }
//
//    @Bean
//    GrpcAuthenticationReader grpcAuthenticationReader(){
//        return  new BearerAuthenticationReader(token ->{
//            JwtParserBuilder claims = Jwts.parser().setSigningKey(jwtSecretKey).requireIssuer(token);
//            List<SimpleGrantedAuthority> auth =Arrays.stream(claims.requireIssuer("auth").toString().split(","))
//                    .map(SimpleGrantedAuthority::new)
//                    .collect(Collectors.toList());
//        User user = new User(claims.toString(), "", auth);
//        return new UsernamePasswordAuthenticationToken(user,token, auth);
//
//        });
//    }
//
//
//    @Bean
//    GrpcSecurityMetadataSource grpcSecurityMetadataSource() {
//        ManualGrpcSecurityMetadataSource manualGrpcSecurityMetadataSource = new ManualGrpcSecurityMetadataSource();
//        manualGrpcSecurityMetadataSource.setDefault(AccessPredicate.permitAll());
//
//        manualGrpcSecurityMetadataSource.set(EmployeeJwtServiceGrpc.getGetEmlployeeJwtInfoMethod(), AccessPredicate.hasRole("ROLE_ADMIN"));
//        return manualGrpcSecurityMetadataSource;
//    }
//
////    @Bean
////    AccessDecisionManager accessDecisionManager() {
////        List<AccessDecisionVoter<?>> accessDecisionVoters = new ArrayList<>();
////        accessDecisionVoters.add(new AccessPredicateVoter());
////        return new UnanimousBased(accessDecisionVoters);
////
////    }
//}
