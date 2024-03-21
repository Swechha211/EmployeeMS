package com.ems.EmployeeMS.repositories;


import com.ems.EmployeeMS.entities.Department;
import com.ems.EmployeeMS.entities.Employee;
import com.ems.EmployeeMS.entities.Project;
import com.ems.EmployeeMS.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProjectRepositoryImpl implements ProjectRepository{
    private final DataSource dataSource;
    @Autowired
    private Employee employee;
    @Autowired
    private Department department;

    public ProjectRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    private final Logger logger = LoggerFactory.getLogger(ProjectRepositoryImpl.class);


//    @Override
//    public Project saveProject(Project project) {
//        try (Connection connection = dataSource.getConnection()) {
//            System.out.println("Connected to the database");
//            try (Statement statement = connection.createStatement()) {
//                String sql = "INSERT INTO project (project_id, name, start_date, end_date) VALUES ('" + project
//                        .getProject_id()+ "', '" + project.getName() + "', '" + project.getStart_date() +"', '" + project.getEnd_date() + "')";
//                statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
//                System.out.println("Project saved successfully");
//                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
//                    if (generatedKeys.next()) {
//                        project.setProject_id(generatedKeys.getLong(1));
//                        System.out.println("Project id  ="+project.getProject_id());
//                    } else {
//                        throw new SQLException("Creating project failed, no ID obtained.");
//                    }
//                }
//            } catch (SQLException e) {
//                System.out.println("Error executing the SQL query" + e.getMessage());
//            }
//
//        } catch (SQLException e) {
//            System.out.println("Error connecting to the database" + e.getMessage());
//        }
//        return project;
//    }

    @Override
    public Project saveProject(Project project, Long employee_id, Long department_id) {
        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connected to the database");
            try (Statement statement = connection.createStatement()) {

                String sql = "INSERT INTO project (project_id, name, start_date, end_date, emp_id, dep_id) VALUES ('" + project.getProject_id()+ "', '" + project.getName() + "', '" + project.getStart_date() +"', '" + project.getEnd_date() +"', '" + employee_id + "', '" + department_id +"')";
                statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
                project.setEmployee(employee);
                project.setDepartment(department);
                logger.info("Record saved successfully");

            } catch (SQLException e) {
                logger.error("Error executing the SQL query" + e.getMessage());
                throw new SQLException("Error executing the SQL query" + e.getMessage());
            }
        } catch (Exception e) {
            logger.error("Error connecting to the database" + e.getMessage());
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
                        project.setStart_date(resultSet.getString("start_date"));
                        project.setEnd_date(resultSet.getString("end_date"));

                        Long eId = resultSet.getLong("emp_id");
                        Employee employee1 = getEmployeeById(eId);
                        project.setEmployee(employee1);

                        Integer dId = resultSet.getInt("dep_id");
                        Department department1 = getDepartmentById(dId);
                        project.setDepartment(department1);

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
                        project.setStart_date(resultSet.getString("start_date"));
                        project.setEnd_date(resultSet.getString("end_date"));

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

        return projects;
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
                logger.warn("No project found with ID: " + project_id);

            } else {
            logger.info("Project with ID " + project_id + " deleted successfully");
            }
        } catch (SQLException e) {
            logger.error("Error executing the SQL query: " + e.getMessage());

        } catch (Exception e) {
            logger.error("Error connecting to the database: " + e.getMessage());
        }

    }

    @Override
    public List<Project> getProjectByEmployee(Long employee_id) {
        List<Project> projects = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connected to the database");

            try (Statement statement = connection.createStatement()) {
                String sql = "SELECT * FROM project WHERE emp_id = " + employee_id;
                try (ResultSet resultSet = statement.executeQuery(sql)) {
                    while (resultSet.next()) {
                        Project project = new Project();
                        project .setProject_id(resultSet.getLong("project_id"));
                        project .setName(resultSet.getString("name"));
                        project.setStart_date(resultSet.getString("start_date"));
                        project.setEnd_date(resultSet.getString("end_date"));

                        Long eId = resultSet.getLong("emp_id");
                        Employee employee1 = getEmployeeById(eId); // Implement this method to fetch user by ID
                        project.setEmployee(employee1);

                       Integer dId = resultSet.getInt("dep_id");
                        Department department1 = getDepartmentById(dId);
                        project.setDepartment(department1);


                        projects.add(project);
                    }
                }
                logger.info("Record selected successfully");
            } catch (SQLException e) {
                logger.error("Error executing the SQL query: " + e.getMessage());

            }
        } catch (Exception e) {
            logger.error("Error connecting to the database: " + e.getMessage());
        }

        return  projects;
    }
    // Method to get Employee by ID using Statement
    private Employee getEmployeeById(Long employeeId) {
        Employee employee1 = null;
        String sql = "SELECT * FROM employee WHERE employee_id = " + employeeId;
        try (   Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                employee1 = new Employee();
                employee1 .setEmployee_id(resultSet.getLong("employee_id"));
                employee1 .setName(resultSet.getString("name"));
                employee1 .setEmail(resultSet.getString("email"));
                employee1 .setAddress(resultSet.getString("address"));
                employee1 .setPhone(resultSet.getString("phone"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employee1;
    }

    // Method to get Department by ID using Statement
    private Department getDepartmentById(int departmentId) {
        Department department1 = null;
        String sql = "SELECT * FROM department WHERE department_id = " + departmentId;
        try (   Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                department1 = new Department();
                department1.setDepartment_id(resultSet.getLong("department_id"));
                department1.setName(resultSet.getString("name"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return department1;
    }

}
