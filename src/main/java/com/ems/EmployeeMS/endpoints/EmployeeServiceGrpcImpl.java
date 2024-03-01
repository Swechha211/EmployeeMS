package com.ems.EmployeeMS.endpoints;

import com.ems.EmployeeMS.entities.Employee;
import com.ems.EmployeeMS.exceptions.APIResponse;

import com.ems.EmployeeMS.exceptions.ResourceNotFoundException;
import com.grpc.EmployeeOuterClass;
import com.grpc.EmployeeServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@GrpcService
public class EmployeeServiceGrpcImpl extends EmployeeServiceGrpc.EmployeeServiceImplBase {
    private final DataSource dataSource;

    public EmployeeServiceGrpcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private final Logger logger = LoggerFactory.getLogger(EmployeeServiceGrpcImpl.class);

    @Override
    public void addEmployee(EmployeeOuterClass.Employee request, StreamObserver<EmployeeOuterClass.Employee> responseObserver) {
        Long employee_id = request.getEmployeeId();
        String name = request.getName();
        String email = request.getEmail();
        String address= request.getAddress();
        String phone = request.getPhone();
        Employee employee = new Employee();
        employee.setEmployee_id(employee_id);
        employee.setName(name);
        employee.setEmail(email);
        employee.setAddress(address);
        employee.setPhone(phone);

            try (Connection connection = dataSource.getConnection()) {
            System.out.println("Connected to the database");
            try (Statement statement = connection.createStatement()) {
                String sql = "INSERT INTO employee (employee_id, name, email, address, phone) VALUES ('" + employee.getEmployee_id() + "', '" + employee.getName() + "', '"+ employee.getEmail() + "', '" + employee.getAddress() + "', '" + employee.getPhone() + "')";
                statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
                System.out.println("Employee saved successfully");
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        employee.setEmployee_id(generatedKeys.getLong(1));
                        System.out.println("Employee id in saveStudent ="+employee.getEmployee_id());
                    } else {
                        throw new SQLException("Creating student failed, no ID obtained.");
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error executing the SQL query" + e.getMessage());
            }

//            APIResponse.Builder response = APIResponse.newBuilder();
//            response.setMessageCode(0).setResponseMessage("values inserted");
            System.out.println("values inserted");
        }
        catch (Exception e){
//            APIResponse.Builder response = APIResponse.newBuilder();
//            response.setMessageCode(0).setResponseMessage("values not inserted");
            System.out.println("values not inserted");
            e.printStackTrace();
        }

        super.addEmployee(request, responseObserver);
    }

    @Override
    public void getEmployee(EmployeeOuterClass.EmployeeRequest request, StreamObserver<EmployeeOuterClass.Employee> responseObserver) {
        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connected to the database");

            try (Statement statement = connection.createStatement()) {
                String sql = "SELECT * FROM employee WHERE employee_id = " + request.getEmployeeId();
                try (ResultSet resultSet = statement.executeQuery(sql)) {
                    if (resultSet.next()) {
                        Employee employee= new Employee();
                        employee.setEmployee_id(resultSet.getInt("employee_id"));
                        employee.setName(resultSet.getString("name"));
                        employee.setEmail(resultSet.getString("email"));
                        employee.setAddress(resultSet.getString("address"));
                        employee.setPhone(resultSet.getString("phone"));

                    }
                }
                logger.info("Record selected successfully");
            } catch (SQLException e) {
                logger.error("Error executing the SQL query: " + e.getMessage());

            }
        } catch (Exception e) {
            logger.error("Error connecting to the database: " + e.getMessage());
        }


        super.getEmployee(request, responseObserver);
    }

    @Override
    public void getAllEmployees(EmployeeOuterClass.EmptyRequest request, StreamObserver<EmployeeOuterClass.EmployeeResponse> responseObserver) {
        List<Employee> employeesList = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connected to the database");

            try (Statement statement = connection.createStatement()) {

                String sql = "SELECT * FROM employee";
                try (ResultSet resultSet = statement.executeQuery(sql)) {
                    while (resultSet.next()) {
                        Employee employee= new Employee();
                        employee .setEmployee_id(resultSet.getInt("employee_id"));
                        employee .setName(resultSet.getString("name"));
                        employee .setEmail(resultSet.getString("email"));
                        employee .setAddress(resultSet.getString("address"));
                        employee .setPhone(resultSet.getString("phone"));

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
        super.getAllEmployees(request, responseObserver);
    }

    @Override
    public void updateEmployee(EmployeeOuterClass.EmployeeRequest request, StreamObserver<EmployeeOuterClass.Employee> responseObserver) {
        Employee employee = new Employee();
        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connected to the database");
            try (Statement statement = connection.createStatement()) {

                String sql = "UPDATE employee SET name = '" + employee.getName()+ "', email = '" + employee.getEmail() + "', address = '" + employee.getAddress() + "', phone = '" + employee.getPhone() + "' WHERE employee_id = " + request.getEmployeeId();
                int rowsAffected = statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
                if (rowsAffected == 0) {
                    throw new ResourceNotFoundException("Employee", "employee_id", request.getEmployeeId());
                }
                logger.info("Record updated successfully");

            } catch (SQLException e) {
                logger.error("Error executing the SQL query" + e.getMessage());

            }
        } catch (Exception e) {
            logger.error("Error connecting to the database " + e.getMessage());
        }
        super.updateEmployee(request, responseObserver);
    }

    @Override
    public void deleteEmployee(EmployeeOuterClass.EmployeeRequest request, StreamObserver<EmployeeOuterClass.EmptyResponse> responseObserver) {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            String sql = "DELETE FROM employee WHERE employee_id = " + request.getEmployeeId();
            int rowsAffected = statement.executeUpdate(sql);
            if (rowsAffected == 0) {
                logger.warn("No user found with ID: " + request.getEmployeeId());

            } else {
                logger.info("User with ID " + request.getEmployeeId() + " deleted successfully");
            }
        } catch (SQLException e) {
            logger.error("Error executing the SQL query: " + e.getMessage());

        } catch (Exception e) {
            logger.error("Error connecting to the database: " + e.getMessage());
        }
        super.deleteEmployee(request, responseObserver);
    }
}
