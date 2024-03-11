package com.ems.EmployeeMS.repositories;

import com.ems.EmployeeMS.entities.Department;
import com.ems.EmployeeMS.entities.Project;
import com.ems.EmployeeMS.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProjectRepositoryImpl implements ProjectRepository{
    private final DataSource dataSource;

    public ProjectRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    private final Logger logger = LoggerFactory.getLogger(DepartmentRepositoryImpl.class);


    @Override
    public Project saveProject(Project project) {
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("Connected to the database");
            try (Statement statement = connection.createStatement()) {
                String sql = "INSERT INTO project (project_id, name, start_date, end_date) VALUES ('" + project
                        .getProject_id()+ "', '" + project.getName() + "', '" + project.getStart_date() +"', '" + project.getEnd_date() + "')";
                statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
                System.out.println("Project saved successfully");
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        project.setProject_id(generatedKeys.getLong(1));
                        System.out.println("Project id  ="+project.getProject_id());
                    } else {
                        throw new SQLException("Creating project failed, no ID obtained.");
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error executing the SQL query" + e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println("Error connecting to the database" + e.getMessage());
        }
        return project;
    }

    @Override
    public Project getProjectByID(Long project_id) {
        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connected to the database");

            try (Statement statement = connection.createStatement()) {
                String sql = "SELECT * FROM project WHERE project_id = " + project_id;
                try (ResultSet resultSet = statement.executeQuery(sql)) {
                    if (resultSet.next()) {
                        Project project= new Project();
                        project.setProject_id(resultSet.getLong("project_id"));
                        project.setName(resultSet.getString("name"));
                        project.setStart_date(resultSet.getDate("start_date"));
                        project.setEnd_date(resultSet.getDate("end_date"));

                        return project;
                    }
                }
                logger.info("Record selected successfully");
            } catch (SQLException e) {
                logger.error("Error executing the SQL query: " + e.getMessage());

            }
        } catch (Exception e) {
            logger.error("Error connecting to the database: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Project> getAllProject() {
        List<Project> projects = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connected to the database");

            try (Statement statement = connection.createStatement()) {

                String sql = "SELECT * FROM project";
                try (ResultSet resultSet = statement.executeQuery(sql)) {
                    while (resultSet.next()) {
                        Project project = new Project();
                        project .setProject_id(resultSet.getLong("project_id"));
                        project .setName(resultSet.getString("name"));
                        project.setStart_date(resultSet.getDate("start_date"));
                        project.setEnd_date(resultSet.getDate("end_date"));

                        projects.add(project);
                    }
                }
                logger.info("Records selected successfully");
            } catch (SQLException e) {
                logger.error("Error executing the SQL query: " + e.getMessage());
                throw new SQLException("Error executing the SQL query: " + e.getMessage());
            }
        } catch (Exception e) {
            logger.error("Error connecting to the database: " + e.getMessage());
        }

        return null;
    }

    @Override
    public void updateProject(Long project_id, Project project) {
        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connected to the database");
            try (Statement statement = connection.createStatement()) {

                String sql = "UPDATE project SET name = '" + project.getName()+  "', start_date = '" + project.getStart_date() + "', end_date = '" + project.getEnd_date() + "' WHERE project_id = " + project_id;
                int rowsAffected = statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
                if (rowsAffected == 0) {
                    throw new ResourceNotFoundException("Project", "project_id", project_id);
                }
                logger.info("Record updated successfully");

            } catch (SQLException e) {
                logger.error("Error executing the SQL query" + e.getMessage());

            }
        } catch (Exception e) {
            logger.error("Error connecting to the database " + e.getMessage());
        }

    }

    @Override
    public void deleteProject(Long project_id) {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            String sql = "DELETE FROM project WHERE project_id = " + project_id;
            int rowsAffected = statement.executeUpdate(sql);
            if (rowsAffected == 0) {
                logger.warn("No department found with ID: " + project_id);

            } else {
                logger.info("Project with ID " + project_id + " deleted successfully");
            }
        } catch (SQLException e) {
            logger.error("Error executing the SQL query: " + e.getMessage());

        } catch (Exception e) {
            logger.error("Error connecting to the database: " + e.getMessage());
        }

    }
}
