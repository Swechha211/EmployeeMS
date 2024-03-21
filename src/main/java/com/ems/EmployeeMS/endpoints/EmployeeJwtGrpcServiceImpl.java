package com.ems.EmployeeMS.endpoints;

import com.grpc.EmployeeJwtServiceGrpc;
import com.grpc.EmployeeServiceGrpc;
import com.grpc.Schema;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class EmployeeJwtGrpcServiceImpl extends EmployeeJwtServiceGrpc.EmployeeJwtServiceImplBase {

    @Override
    public void getEmlployeeJwtInfo(Schema.EmptyRequest4 request, StreamObserver<Schema.EmployeeJwt> responseObserver) {
        responseObserver.onNext(Schema.EmployeeJwt.newBuilder().setName("Swechha").setSalary(1000).build());
        responseObserver.onCompleted();
    }
}
