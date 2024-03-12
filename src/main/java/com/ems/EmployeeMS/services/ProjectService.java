package com.ems.EmployeeMS.services;

import com.ems.EmployeeMS.entities.Project;

import java.util.List;

public interface ProjectService {

    public Project saveProject (Project project);
    public Project  getProjectByID(Long project_id);
    public List<Project > getAllProject();
    public Project updateProject (Long project_id, Project  project);
    public void deleteProject (Long project_id);
}
