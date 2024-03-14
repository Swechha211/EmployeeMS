package com.ems.EmployeeMS.endpoints;

import com.ems.EmployeeMS.entities.Department;
import com.ems.EmployeeMS.entities.Employee;
import com.ems.EmployeeMS.facade.ProjectFacade;
import com.grpc.DepartmentOuterClass;
import com.grpc.EmployeeOuterClass;
import com.grpc.ProjectOuterClass;
import com.grpc.ProjectServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@GrpcService
public class ProjectServiceGrpcImpl extends ProjectServiceGrpc.ProjectServiceImplBase {
    private final ProjectFacade projectFacade;
    @Autowired
    private Employee employee;
    @Autowired
    private Department department;

    public ProjectServiceGrpcImpl(ProjectFacade projectFacade) {
        this.projectFacade = projectFacade;
    }

//    @Override
//    public void addProject(ProjectOuterClass.Project request, StreamObserver<ProjectOuterClass.Project> responseObserver) {
//        ProjectOuterClass.Project response =projectFacade.saveProject(request);
//        responseObserver.onNext(response);
//        responseObserver.onCompleted();
//    }

    @Override
    public void addProject(ProjectOuterClass.Project request, StreamObserver<ProjectOuterClass.Project> responseObserver) {
//        ProjectOuterClass.Project response =projectFacade.saveProject(request, employee.getEmployee_id(), department.getDepartment_id());
        Long employeeId = (long) request.getEmployeeCount();
        Long departmentId = (long) request.getDepartmentCount();
        ProjectOuterClass.Project response =projectFacade.saveProject(request, employeeId, departmentId);
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

    @Override
    public void getEmployees(EmployeeOuterClass.EmployeeRequest request, StreamObserver<EmployeeOuterClass.EmployeeResponse> responseObserver) {
//        List<EmployeeOuterClass.Employee> employees = ... // Get the list of employees from somewhere
//
//        for (EmployeeOuterClass.Employee employee : employees) {
//            List<ProjectOuterClass.Project> projects = projectFacade.getProjectsByEmployeeId(employee.getId()); // Assuming you have a method to get projects by employee ID
//
//            EmployeeOuterClass.EmployeeResponse.Builder employeeResponseBuilder = EmployeeOuterClass.EmployeeResponse.newBuilder();
//            employeeResponseBuilder.setEmployee(employee);
//
//            if (projects != null) {
//                employeeResponseBuilder.addAllProjects(projects);
//            }
//
//            EmployeeOuterClass.EmployeeResponse employeeResponse = employeeResponseBuilder.build();
//            responseObserver.onNext(employeeResponse);
//        }

        responseObserver.onCompleted();
    }
}
