package com.ems.EmployeeMS.facade;


import com.ems.EmployeeMS.entities.Project;
import com.ems.EmployeeMS.services.ProjectService;
import com.grpc.ProjectOuterClass;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectFacade {
    public final ProjectService projectService;

    public ProjectFacade(ProjectService projectService) {
        this.projectService = projectService;
    }


    public ProjectOuterClass.Project saveProject (ProjectOuterClass.Project project, Long employee_id, Long department_id){
        System.out.println("Project from input = "+project);
        Project project1 = mapToEntity(project);
        System.out.println("Project after mapping to Project entity  = "+project1);
        ProjectOuterClass.Project project2= mapToOuterClass(projectService.saveProject(project1, employee_id, department_id));
        System.out.println(" Project after mapping to outerclass  = "+ project2);
        return project2;
    }

//    public ProjectOuterClass.Project saveProject (ProjectOuterClass.Project project){
//        System.out.println("Project from input = "+project);
//        Project project1 = mapToEntity(project);
//        System.out.println("Project after mapping to Project entity  = "+project1);
//        ProjectOuterClass.Project project2= mapToOuterClass(projectService.saveProject(project1));
//        System.out.println(" Project after mapping to outerclass  = "+ project2);
//        return project2;
//    }
    public ProjectOuterClass.Project getProjectById(Long project_id) {
        System.out.println("Project id= "+project_id);
        return mapToOuterClass(projectService.getProjectByID(project_id));
    }

    public void deleteProject(Long projectId) {
        System.out.println("Project id to delete = " + projectId);
        projectService.deleteProject(projectId);
    }

    public List<ProjectOuterClass.Project> getAllProjects() {
        List<Project> projects = projectService.getAllProject();
        List<ProjectOuterClass.Project> projectOuterList = new ArrayList<>();
        if (projects != null) {
            for (Project project : projects) {
                ProjectOuterClass.Project projectOuter = mapToOuterClass(project);
                projectOuterList.add(projectOuter);
            }
        }
        return projectOuterList;
    }

    public ProjectOuterClass.Project updateProject(Long project_id,ProjectOuterClass.Project project) {
        Project updatedProject = projectService.updateProject(project_id, mapToEntity(project));
        return mapToOuterClass(updatedProject);
    }

    public List<ProjectOuterClass.Project> getProjectByEmployee(Long employee_id){
        List<Project> projects = projectService.getProjectByEmployee(employee_id);
        List<ProjectOuterClass.Project> projectOuterList = new ArrayList<>();
        if (projects != null) {
            for (Project project : projects) {
                ProjectOuterClass.Project projectOuter = mapToOuterClass(project);
                projectOuterList.add(projectOuter);
            }
        }
        return projectOuterList;

    }

    private Project mapToEntity(ProjectOuterClass.Project project){
        Project project1 = new Project();
        project1.setProject_id(project.getProjectId());
        project1.setName(project.getName());
        project1.setStart_date(project.getStartDate());
        project1.setEnd_date(project.getEndDate());

        return project1;
    }

    private ProjectOuterClass.Project mapToOuterClass(Project project){
        ProjectOuterClass.Project.Builder builder = ProjectOuterClass.Project.newBuilder();
        builder.setProjectId(project.getProject_id())
                .setName(project.getName())
                .setStartDate(project.getStart_date())
                .setEndDate(project.getEnd_date());
        return builder.build();
    }
}
