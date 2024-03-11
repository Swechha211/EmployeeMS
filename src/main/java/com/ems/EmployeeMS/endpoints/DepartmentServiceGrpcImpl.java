package com.ems.EmployeeMS.endpoints;

import com.ems.EmployeeMS.facade.DepartmentFacade;
import com.grpc.DepartmentOuterClass;
import com.grpc.DepartmentServiceGrpc;
import com.grpc.EmployeeOuterClass;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;

@GrpcService
public class DepartmentServiceGrpcImpl  extends DepartmentServiceGrpc.DepartmentServiceImplBase {
    public final DepartmentFacade departmentFacade;

    public DepartmentServiceGrpcImpl(DepartmentFacade departmentFacade) {
        this.departmentFacade = departmentFacade;
    }

    @Override
    public void addDepartment(DepartmentOuterClass.Department request, StreamObserver<DepartmentOuterClass.Department> responseObserver) {
        DepartmentOuterClass.Department response = departmentFacade.saveDepartment(request);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getDepartment(DepartmentOuterClass.DepartmentRequest request, StreamObserver<DepartmentOuterClass.Department> responseObserver) {
        DepartmentOuterClass.Department response = departmentFacade.getDepartmentById(request.getDepartmentId());
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteDepartment(DepartmentOuterClass.DepartmentRequest request, StreamObserver<DepartmentOuterClass.EmptyResponse2> responseObserver) {
        departmentFacade.deleteDepartment(request.getDepartmentId());
        responseObserver.onNext(DepartmentOuterClass.EmptyResponse2.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void getAllDepartments(DepartmentOuterClass.EmptyRequest2 request, StreamObserver<DepartmentOuterClass.DepartmentsResponse> responseObserver) {
        List<DepartmentOuterClass.Department> responses = departmentFacade.getAllDepartments();
        if (responses != null) {
        for (DepartmentOuterClass.Department response : responses) {
            DepartmentOuterClass.DepartmentsResponse departmentResponse = DepartmentOuterClass.DepartmentsResponse.newBuilder()

                    .build();
            responseObserver.onNext(departmentResponse);
        }  } else {
            System.err.println("Error: departments list is null");

        }
        responseObserver.onCompleted();
    }

    @Override
    public void updateDepartment(DepartmentOuterClass.Department request, StreamObserver<DepartmentOuterClass.Department> responseObserver) {
        super.updateDepartment(request, responseObserver);
    }
}
