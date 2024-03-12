package com.ems.EmployeeMS.endpoints;

import com.ems.EmployeeMS.facade.ProjectFacade;
import com.grpc.DepartmentOuterClass;
import com.grpc.ProjectOuterClass;
import com.grpc.ProjectServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;

@GrpcService
public class ProjectServiceGrpcImpl extends ProjectServiceGrpc.ProjectServiceImplBase {
    private final ProjectFacade projectFacade;

    public ProjectServiceGrpcImpl(ProjectFacade projectFacade) {
        this.projectFacade = projectFacade;
    }

    @Override
    public void addProject(ProjectOuterClass.Project request, StreamObserver<ProjectOuterClass.Project> responseObserver) {
        ProjectOuterClass.Project response =projectFacade.saveProject(request);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getProject(ProjectOuterClass.ProjectRequest request, StreamObserver<ProjectOuterClass.Project> responseObserver) {
        ProjectOuterClass.Project response = projectFacade.getProjectById(request.getProjectId());
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getAllProjects(ProjectOuterClass.EmptyRequest3 request, StreamObserver<ProjectOuterClass.ProjectResponse> responseObserver) {
        List<ProjectOuterClass.Project> responses = projectFacade.getAllProjects();
        if (responses != null) {
            for (ProjectOuterClass.Project response : responses) {
                ProjectOuterClass.ProjectResponse projectResponse = ProjectOuterClass.ProjectResponse.newBuilder()
                        .addAllProjects(responses)
                        .build();
                responseObserver.onNext(projectResponse);
            }
        } else {
            System.err.println("Error: projects list is null");

        }
        responseObserver.onCompleted();
    }

    @Override
    public void deleteProject(ProjectOuterClass.ProjectRequest request, StreamObserver<ProjectOuterClass.EmptyResponse3> responseObserver) {
        projectFacade.deleteProject(request.getProjectId());
        responseObserver.onNext(ProjectOuterClass.EmptyResponse3.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void updateProject(ProjectOuterClass.Project request, StreamObserver<ProjectOuterClass.Project> responseObserver) {
        ProjectOuterClass.Project response = projectFacade.updateProject(request.getProjectId(),request) ;
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
