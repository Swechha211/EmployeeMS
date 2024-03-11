package com.ems.EmployeeMS.services;

import com.ems.EmployeeMS.entities.Project;
import com.ems.EmployeeMS.repositories.DepartmentRepository;
import com.ems.EmployeeMS.repositories.ProjectRepository;
import com.ems.EmployeeMS.repositories.ProjectRepositoryImpl;

import java.util.List;

public class ProjectServiceImpl implements ProjectService{
    private final ProjectRepository projectRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public Project saveProject(Project project) {
        return projectRepository.saveProject(project);
    }

    @Override
    public Project getProjectByID(Long project_id) {
        return projectRepository.getProjectByID(project_id);
    }

    @Override
    public List<Project> getAllProject() {
        return projectRepository.getAllProject();
    }

    @Override
    public void updateProject(Long project_id, Project project) {
        projectRepository.updateProject(project_id, project);


    }

    @Override
    public void deleteProject(Long project_id) {
        projectRepository.deleteProject(project_id);
    }
}
