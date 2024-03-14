package com.ems.EmployeeMS.repositories;

import com.ems.EmployeeMS.entities.Department;
import com.ems.EmployeeMS.entities.Project;

import java.util.List;

public interface ProjectRepository {
    public Project saveProject (Project project, Long employee_id, Long department_id);
//    public Project saveProject (Project project);
    public Project  getProjectByID(Long project_id);
    public List<Project > getAllProject();
    public void updateProject (Long project_id, Project  project);
    public void deleteProject (Long project_id);
    List<Project> getProjectByEmployee(Long employee_id);
}
