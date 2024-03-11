package com.ems.EmployeeMS.config;

import com.ems.EmployeeMS.endpoints.DepartmentServiceGrpcImpl;
import com.ems.EmployeeMS.endpoints.EmployeeServiceGrpcImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;


@Configuration
public class ServerConfig {

    @Bean
    public Server grpcServer(EmployeeServiceGrpcImpl employeeServiceGrpc,
                             DepartmentServiceGrpcImpl departmentServiceGrpc) throws IOException {

        Server server = ServerBuilder.forPort(9091)
                .addService(employeeServiceGrpc)
                .addService(departmentServiceGrpc)
                .build();
        server.start();
        System.out.println("Server started at " + server.getPort());
        return server;
    }
}