package com.ems.EmployeeMS.repositories;

import com.ems.EmployeeMS.entities.Department;
import com.ems.EmployeeMS.entities.Employee;
import com.ems.EmployeeMS.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DepartmentRepositoryImpl implements DepartmentRepository{
    private final DataSource dataSource;

    public DepartmentRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    private final Logger logger = LoggerFactory.getLogger(DepartmentRepositoryImpl.class);

    @Override
    public Department saveDepartment(Department department) {
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("Connected to the database");
            try (Statement statement = connection.createStatement()) {
                String sql = "INSERT INTO department(department_id, name) VALUES ('" + department.getDepartment_id()+ "', '" + department.getName() +  "')";
                statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
                System.out.println("Department saved successfully");
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        department.setDepartment_id(generatedKeys.getLong(1));
                        System.out.println("Department id  ="+department.getDepartment_id());
                    } else {
                        throw new SQLException("Creating department failed, no ID obtained.");
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error executing the SQL query" + e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println("Error connecting to the database" + e.getMessage());
        }
        return department;
    }

    @Override
    public Department getDepartmentByID(Long department_id) {
        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connected to the database");

            try (Statement statement = connection.createStatement()) {
                String sql = "SELECT * FROM department WHERE department_id = " + department_id;
                try (ResultSet resultSet = statement.executeQuery(sql)) {
                    if (resultSet.next()) {
                        Department department = new Department();
                        department.setDepartment_id(resultSet.getLong("department_id"));
                        department.setName(resultSet.getString("name"));

                        return department;
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
    public List<Department> getAllDepartment() {
        List<Department> departments = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connected to the database");

            try (Statement statement = connection.createStatement()) {

                String sql = "SELECT * FROM department";
                try (ResultSet resultSet = statement.executeQuery(sql)) {
                    while (resultSet.next()) {
                        Department department = new Department();
                        department .setDepartment_id(resultSet.getLong("department_id"));
                        department .setName(resultSet.getString("name"));

                        departments.add(department);
                    }
                }
                logger.info("Records selected successfully" );
            } catch (SQLException e) {
                logger.error("Error executing the SQL query: " + e.getMessage());
                throw new SQLException("Error executing the SQL query: " + e.getMessage());
            }
        } catch (Exception e) {
            logger.error("Error connecting to the database: " + e.getMessage());
        }

        return departments;
    }

    @Override
    public void updateDepartment(Long department_id, Department department) {
        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connected to the database");
            try (Statement statement = connection.createStatement()) {

                String sql = "UPDATE department SET name = '" + department.getName()+  "' WHERE department_id = " + department_id;
                int rowsAffected = statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
                if (rowsAffected == 0) {
                    throw new ResourceNotFoundException("Department", "department_id", department_id);
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
    public void deleteDepartment(Long department_id) {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            String sql = "DELETE FROM department WHERE department_id = " + department_id;
            int rowsAffected = statement.executeUpdate(sql);
            if (rowsAffected == 0) {
                logger.warn("No department found with ID: " + department_id);

            } else {
                logger.info("Department with ID " + department_id + " deleted successfully");
            }
        } catch (SQLException e) {
            logger.error("Error executing the SQL query: " + e.getMessage());

        } catch (Exception e) {
            logger.error("Error connecting to the database: " + e.getMessage());
        }

    }
}
