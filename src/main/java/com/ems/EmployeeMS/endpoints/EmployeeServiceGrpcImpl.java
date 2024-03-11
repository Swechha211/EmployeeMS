package com.ems.EmployeeMS.endpoints;


import com.ems.EmployeeMS.facade.EmployeeFacade;
import com.grpc.EmployeeOuterClass;
import com.grpc.EmployeeServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import com.google.protobuf.Empty;

import java.util.List;


@GrpcService
public class EmployeeServiceGrpcImpl extends EmployeeServiceGrpc.EmployeeServiceImplBase {
    private  final EmployeeFacade employeeFacade;

    public EmployeeServiceGrpcImpl(EmployeeFacade employeeFacade) {
        this.employeeFacade = employeeFacade;
    }

    @Override
    public void getEmployee(EmployeeOuterClass.EmployeeRequest request, StreamObserver<EmployeeOuterClass.Employee> responseObserver) {
        EmployeeOuterClass.Employee response = employeeFacade.getEmployeetById(request.getEmployeeId());
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void addEmployee(EmployeeOuterClass.Employee request, StreamObserver<EmployeeOuterClass.Employee> responseObserver) {
        EmployeeOuterClass.Employee response = employeeFacade.saveEmployee(request);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

//    @Override
//    public void updateEmployee(EmployeeOuterClass.EmployeeRequest request, StreamObserver<EmployeeOuterClass.Employee> responseObserver) {
//        EmployeeOuterClass.Employee response = employeeFacade.updateEmployee(request.getEmployeeId(),request) ;
//        responseObserver.onNext(response);
//        responseObserver.onCompleted();
//    }

    @Override
    public void deleteEmployee(EmployeeOuterClass.EmployeeRequest request, StreamObserver<EmployeeOuterClass.EmptyResponse> responseObserver) {
        employeeFacade.deleteEmployee(request.getEmployeeId());
        responseObserver.onNext(EmployeeOuterClass.EmptyResponse.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void getAllEmployees(EmployeeOuterClass.EmptyRequest request, StreamObserver<EmployeeOuterClass.EmployeeResponse> responseObserver) {
        List<EmployeeOuterClass.Employee> responses = employeeFacade.getAllEmployees();
        for (EmployeeOuterClass.Employee response : responses) {
            EmployeeOuterClass.EmployeeResponse employeeResponse = EmployeeOuterClass.EmployeeResponse.newBuilder()

                    .build();
            responseObserver.onNext(employeeResponse);
        }
        responseObserver.onCompleted();

    }
}
