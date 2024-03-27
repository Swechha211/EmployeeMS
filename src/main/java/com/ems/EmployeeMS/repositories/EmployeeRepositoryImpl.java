package com.ems.EmployeeMS.repositories;

import com.ems.EmployeeMS.entities.Employee;
import com.ems.EmployeeMS.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public class EmployeeRepositoryImpl implements EmployeeRepository {
    private final DataSource dataSource;

    public EmployeeRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private final Logger logger = LoggerFactory.getLogger(EmployeeRepositoryImpl.class);

    @Override
    public Employee saveEmployee(Employee employee) {
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("Connected to the database");
            try (Statement statement = connection.createStatement()) {
                String sql = "INSERT INTO employees(employee_id, name, email, address, phone, password) VALUES ('" + employee.getEmployee_id() + "', '" + employee.getName() + "', '" + employee.getEmail() + "', '" + employee.getAddress() + "', '" + employee.getPhone() +"', '" + employee.getPassword() + "')";
                statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
                System.out.println("Employee saved successfully");
//                System.out.println(sql);
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        employee.setEmployee_id(generatedKeys.getLong(1));
                        System.out.println("Employee id  ="+employee.getEmployee_id());
                    } else {
                        throw new SQLException("Creating student failed, no ID obtained.");
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error executing the SQL query" + e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println("Error connecting to the database" + e.getMessage());
        }
        return employee;
    }

    @Override
    public Employee getEmployeeByID(Long employee_id) {
        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connected to the database");

            try (Statement statement = connection.createStatement()) {
                String sql = "SELECT * FROM employees WHERE employee_id = " + employee_id;
                try (ResultSet resultSet = statement.executeQuery(sql)) {
                    if (resultSet.next()) {
                        Employee employee= new Employee();
                        employee.setEmployee_id(resultSet.getLong("employee_id"));
                        employee.setName(resultSet.getString("name"));
                        employee.setEmail(resultSet.getString("email"));
                        employee.setAddress(resultSet.getString("address"));
                        employee.setPhone(resultSet.getString("phone"));
                        employee.setPassword(resultSet.getString("password"));

                        return employee;
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
    public List<Employee> getAllEmployee() {
        List<Employee> employeesList = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connected to the database");

            try (Statement statement = connection.createStatement()) {

                String sql = "SELECT * FROM employees";
                try (ResultSet resultSet = statement.executeQuery(sql)) {
                    while (resultSet.next()) {
                        Employee employee= new Employee();
                        employee .setEmployee_id(resultSet.getLong("employee_id"));
                        employee .setName(resultSet.getString("name"));
                        employee .setEmail(resultSet.getString("email"));
                        employee .setAddress(resultSet.getString("address"));
                        employee .setPhone(resultSet.getString("phone"));
                        employee .setPassword(resultSet.getString("password"));


                        employeesList.add(employee);
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

        return employeesList;
    }

    @Override
    public void updateEmployee(Long employee_id, Employee employee) {

        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connected to the database");
            try (Statement statement = connection.createStatement()) {

                String sql = "UPDATE employees SET name = '" + employee.getName()+ "', email = '" + employee.getEmail() + "', address = '" + employee.getAddress() + "', phone = '" + employee.getPhone() + "' WHERE employee_id = " + employee_id;
                int rowsAffected = statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
                if (rowsAffected == 0) {
                    throw new ResourceNotFoundException("Employee", "employee_id", employee_id);
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
    public void deleteEmployee(Long employee_id) {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            String sql = "DELETE FROM employees WHERE employee_id = " + employee_id;
            int rowsAffected = statement.executeUpdate(sql);
            if (rowsAffected == 0) {
                logger.warn("No user found with ID: " + employee_id);

            } else {
                logger.info("User with ID " + employee_id + " deleted successfully");
            }
        } catch (SQLException e) {
            logger.error("Error executing the SQL query: " + e.getMessage());

        } catch (Exception e) {
            logger.error("Error connecting to the database: " + e.getMessage());
        }

    }

    @Override
    public Employee getEmployeeByname(String ename) {
        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connected to the database");

            String sql = "SELECT * FROM employees WHERE name = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                // Set the parameter value for the name
                statement.setString(1, ename);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        Employee employee = new Employee();
                        employee.setEmployee_id(resultSet.getLong("employee_id"));
                        employee.setName(resultSet.getString("name"));
                        employee.setEmail(resultSet.getString("email"));
                        employee.setAddress(resultSet.getString("address"));
                        employee.setPhone(resultSet.getString("phone"));
                        employee.setPassword(resultSet.getString("password"));

                        return employee;
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


//    @Override
//    public Employee getEmployeeByname(String ename) {
//        try (Connection connection = dataSource.getConnection()) {
//            logger.info("Connected to the database");
//
//            try (Statement statement = connection.createStatement()) {
//                String sql = "SELECT * FROM employees WHERE name = " + ename;
//                try (ResultSet resultSet = statement.executeQuery(sql)) {
//                    if (resultSet.next()) {
//                        Employee employee= new Employee();
//                        employee.setEmployee_id(resultSet.getLong("employee_id"));
//                        employee.setName(resultSet.getString("name"));
//                        employee.setEmail(resultSet.getString("email"));
//                        employee.setAddress(resultSet.getString("address"));
//                        employee.setPhone(resultSet.getString("phone"));
//                        employee.setPassword(resultSet.getString("password"));
//
//                        return employee;
//                    }
//                }
//                logger.info("Record selected successfully");
//            } catch (SQLException e) {
//                logger.error("Error executing the SQL query: " + e.getMessage());
//
//            }
//        } catch (Exception e) {
//            logger.error("Error connecting to the database: " + e.getMessage());
//        }
//        return null;
//    }
}
