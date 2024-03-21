package com.ems.EmployeeMS.endpoints;

import com.ems.EmployeeMS.entities.Department;
import com.ems.EmployeeMS.entities.Employee;
import com.ems.EmployeeMS.facade.DepartmentFacade;
import com.ems.EmployeeMS.facade.EmployeeFacade;
import com.ems.EmployeeMS.facade.ProjectFacade;
import com.grpc.DepartmentOuterClass;
import com.grpc.EmployeeOuterClass;
import com.grpc.ProjectOuterClass;
import com.grpc.ProjectServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

@GrpcService
public class ProjectServiceGrpcImpl extends ProjectServiceGrpc.ProjectServiceImplBase {
    private final ProjectFacade projectFacade;
    private final EmployeeFacade employeeFacade;
    private final DepartmentFacade departmentFacade;
    @Autowired
    private Employee employee1;
    @Autowired
    private Department department1;

    public ProjectServiceGrpcImpl(ProjectFacade projectFacade, EmployeeFacade employeeFacade, DepartmentFacade departmentFacade) {
        this.projectFacade = projectFacade;
        this.employeeFacade = employeeFacade;
        this.departmentFacade = departmentFacade;
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

//    @Override
//    public void getEmployees(EmployeeOuterClass.EmployeeRequest request, StreamObserver<EmployeeOuterClass.EmployeeResponse> responseObserver) {
//        List<EmployeeOuterClass.Employee> employees = Collections.singletonList(employeeFacade.getEmployeetById(request.getEmployeeId()));
//
//        for (EmployeeOuterClass.Employee employee : employees) {
//            List<ProjectOuterClass.Project> projects = projectFacade.getProjectByEmployee(employee.getEmployeeId()); // Assuming you have a method to get projects by employee ID
//
//            EmployeeOuterClass.EmployeeResponse.Builder employeeResponseBuilder = EmployeeOuterClass.EmployeeResponse.newBuilder();
//            employeeResponseBuilder.addAllEmployees(employees);
//
////            if (projects != null) {
////                employeeResponseBuilder.addAllProjects(projects);
////            }
//
//            EmployeeOuterClass.EmployeeResponse employeeResponse = employeeResponseBuilder.build();
//            responseObserver.onNext(employeeResponse);
//        }
//
//        responseObserver.onCompleted();
//    }

    //run with employee[]

    @Override
    public void getEmployees(EmployeeOuterClass.EmployeeRequest request, StreamObserver<ProjectOuterClass.ProjectResponse> responseObserver) {

        List<ProjectOuterClass.Project> projects = projectFacade.getProjectByEmployee(request.getEmployeeId());

        // Create a ProjectResponse for each project and send it to the client
        ProjectOuterClass.ProjectResponse.Builder responseBuilder = ProjectOuterClass.ProjectResponse.newBuilder();
        responseBuilder.addAllProjects(projects);
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

//    @Override
//    public void getEmployees(EmployeeOuterClass.EmployeeRequest request, StreamObserver<ProjectOuterClass.ProjectResponse> responseObserver) {
//        long employee_id = request.getEmployeeId();
//        List<EmployeeOuterClass.Employee> employees = Collections.singletonList(employeeFacade.getEmployeetById(employee_id));
//
//        for (EmployeeOuterClass.Employee employee : employees) {
//            List<ProjectOuterClass.Project> projects = projectFacade.getProjectByEmployee(employee_id); // Assuming you have a method to get projects by employee ID
//
//            ProjectOuterClass.ProjectResponse.Builder projectResponseBuilder = ProjectOuterClass.ProjectResponse.newBuilder();
//            projectResponseBuilder.addAllProjects(projects);
//
////            if (projects != null) {
////                employeeResponseBuilder.addAllProjects(projects);
////            }
//
//            ProjectOuterClass.ProjectResponse projectResponse = projectResponseBuilder.build();
//            responseObserver.onNext(projectResponse);
//        }
//
//        responseObserver.onCompleted();
//
////        Long employeeId = request.getEmployeeId();
////
////        // Query projects associated with the employeeId from your data source
////        List<ProjectOuterClass.Project> projects = projectFacade.getProjectByEmployee(employeeId);
////
////        if (projects != null) {
////        // Stream the project responses back to the client
////        for (ProjectOuterClass.Project project : projects) {
////            ProjectOuterClass.ProjectResponse response = ProjectOuterClass.ProjectResponse.newBuilder()
////                    .addAllProjects(projects)
////
////                    .build();
////            responseObserver.onNext(response);
////        }
////        } else {
////            System.err.println("Error: projects list is null");
////
////        }
////
////        responseObserver.onCompleted();
//    }


//        @Override
//    public void getEmployees(ProjectOuterClass.ProjectRequest request, StreamObserver<EmployeeOuterClass.EmployeeResponse> responseObserver) {
//        List<ProjectOuterClass.Project> employees = projectFacade.getProjectByEmployee(employee1.getEmployee_id());
//
//        for (ProjectOuterClass.Project employee : employees) {
//            List<EmployeeOuterClass.Employee> projects = Collections.singletonList(employeeFacade.getEmployeetById(employee1.getEmployee_id())); // Assuming you have a method to get projects by employee ID
//
//            EmployeeOuterClass.EmployeeResponse.Builder employeeResponseBuilder = EmployeeOuterClass.EmployeeResponse.newBuilder();
//            employeeResponseBuilder.addAllEmployees(projects);
//
////            if (projects != null) {
////                employeeResponseBuilder.addAllProjects(projects);
////            }
//
//            EmployeeOuterClass.EmployeeResponse employeeResponse = employeeResponseBuilder.build();
//            responseObserver.onNext(employeeResponse);
//        }
//
//        responseObserver.onCompleted();
//    }
//   remain to test
//    @Override
//    public void getProjectsByEmployeeId(ProjectOuterClass.ProjectRequest request, StreamObserver<ProjectOuterClass.ProjectResponse> responseObserver) {
//        Long employeeId = request.getProjectId();
//        List<ProjectOuterClass.Project> projects = projectFacade.getProjectByEmployee(employeeId);
//        ProjectOuterClass.ProjectResponse.Builder responseBuilder = ProjectOuterClass.ProjectResponse.newBuilder();
//        for (ProjectOuterClass.Project project : projects) {
//
//            responseBuilder.addAllProjects(projects);
//        }
//        responseObserver.onNext(responseBuilder.build());
//        responseObserver.onCompleted();
//    }
//}

//    @Override
//    public void getEmployees(ProjectOuterClass.ProjectRequest request, StreamObserver<EmployeeOuterClass.EmployeeResponse> responseObserver) {
////        Long employeeId = (long) request.getEmployeeCount();
////        Long departmentId = (long) request.getDepartmentCount();
//        // Fetch employee from some data source or service
//        EmployeeOuterClass.Employee employee = employeeFacade.getEmployeetById(employee1.getEmployee_id());
//
//        // Check if the employee is not null
//        if (employee != null) {
//            // Fetch project associated with the current employee
//            List<ProjectOuterClass.Project> project = projectFacade.getProjectByEmployee(employee.getEmployeeId());
//
//            // Fetch department associated with the current employee's department ID
//            DepartmentOuterClass.Department department = departmentFacade.getDepartmentById(department1.getDepartment_id());
//
//            // Create a response for the employee including their project and department
//            EmployeeOuterClass.EmployeeResponse.Builder employeeResponseBuilder = EmployeeOuterClass.EmployeeResponse.newBuilder()
//                    .addEmployees(employee);
//            ProjectOuterClass.ProjectResponse.Builder projectResponseBuilder = ProjectOuterClass.ProjectResponse.newBuilder();
////            projectResponseBuilder.addAllProjects(projects);
//
//            // Add project information to the employee response
//            if (project != null) {
//                projectResponseBuilder.addAllProjects(project);
//            } else {
//                // If the project is null, print a message indicating that the employee doesn't have a project
//                System.err.println("Error: Employee with ID " + employee1.getEmployee_id()+ " does not have a project.");
//            }
//
//            // Add department information to the employee response
////            if (department != null) {
////                projectResponseBuilder.addd(department);
////            }
//
//            // Build the employee response
//            EmployeeOuterClass.EmployeeResponse employeeResponse = employeeResponseBuilder.build();
//
//            // Send the response to the client
//            responseObserver.onNext(employeeResponse);
//        } else {
//            // If the employee is null, print an error message
//            System.err.println("Error: Employee with ID " +  employee1.getEmployee_id() + " not found");
//        }
//
//        // Signal to the client that all responses have been sent
//        responseObserver.onCompleted();
//    }



//    @Override
//    public void getEmployees(EmployeeOuterClass.EmployeeRequest request, StreamObserver<EmployeeOuterClass.EmployeeResponse> responseObserver) {
//        List<EmployeeOuterClass.Employee> employees = Collections.singletonList(employeeFacade.getEmployeetById(request.getEmployeeId()));
//
//        for (EmployeeOuterClass.Employee employee : employees) {
//            List<ProjectOuterClass.Project> projects = projectFacade.getProjectByEmployee(employee.getEmployeeId()); // Assuming you have a method to get projects by employee ID
//
//            EmployeeOuterClass.EmployeeResponse.Builder employeeResponseBuilder = EmployeeOuterClass.EmployeeResponse.newBuilder();
//            employeeResponseBuilder.addAllEmployees(employees);
//            ProjectOuterClass.ProjectResponse.Builder projectResponseBuilder = ProjectOuterClass.ProjectResponse.newBuilder();
//            projectResponseBuilder.addAllProjects(projects);
//
////            if (projects != null) {
////                employeeResponseBuilder.addAllProjects(projects);
////            }
//
//            EmployeeOuterClass.EmployeeResponse employeeResponse = employeeResponseBuilder.build();
//            responseObserver.onNext(employeeResponse);
//            ProjectOuterClass.ProjectResponse projectResponse = projectResponseBuilder.build();
//
//
//        }
//
//
//
//        responseObserver.onCompleted();
//    }









}
