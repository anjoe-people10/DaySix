package com.people10;

import java.sql.*;

class Employee {
    private final int employeeId;
    private String firstName, middleName, lastName;
    private int age, departmentId, managerId;
    private float salary;

    public Employee(int employeeId, String firstName, String middleName, String lastName, int age, int departmentId, int managerId, float salary) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.age = age;
        this.departmentId = departmentId;
        this.managerId = managerId;
        this.salary = salary;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public int getManagerId() {
        return managerId;
    }

    public float getSalary() {
        return salary;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", departmentId=" + departmentId +
                ", managerId=" + managerId +
                ", salary=" + salary +
                '}';
    }
}

class EmployeeDAO {
    Connection connection;

    void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/mydatabase";
            String userName = "root";
            String password = "Password@123";
            connection = DriverManager.getConnection(url, userName, password);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    void createEmployee(Employee employee) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO employee VALUES(?, ?, ?, ?, ?, ?, ?, ?)"
            );
            preparedStatement.setInt(1, employee.getEmployeeId()); // Employee id
            preparedStatement.setString(2, employee.getFirstName()); // First name
            preparedStatement.setString(3, employee.getMiddleName()); // Middle name
            preparedStatement.setString(4, employee.getLastName()); // Last name
            preparedStatement.setInt(5, employee.getAge()); // Age
            preparedStatement.setInt(6, employee.getDepartmentId()); // Department id
            preparedStatement.setInt(7, employee.getManagerId()); // Manager id
            preparedStatement.setFloat(8, employee.getSalary()); // salary

            preparedStatement.executeUpdate();

            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    Employee readEmployee(int employeeId) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM employee WHERE employee_id=" + employeeId);
            resultSet.next();
            Employee employee = new Employee(
                    resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getInt(5),
                    resultSet.getInt(6),
                    resultSet.getInt(7),
                    resultSet.getFloat(8)
            );
            System.out.println(employee);
            return employee;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    void updateEmployee(Employee employee) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE employee SET first_name=?, middle_name=?, last_name=?, age=?, department_id=?, manager_id=?, salary=? WHERE employee_id=?"
            );
            preparedStatement.setString(1, employee.getFirstName()); // First name
            preparedStatement.setString(2, employee.getMiddleName()); // Middle name
            preparedStatement.setString(3, employee.getLastName()); // Last name
            preparedStatement.setInt(4, employee.getAge()); // Age
            preparedStatement.setInt(5, employee.getDepartmentId()); // Department id
            preparedStatement.setInt(6, employee.getManagerId()); // Manager id
            preparedStatement.setFloat(7, employee.getSalary()); // Salary
            preparedStatement.setInt(8, employee.getEmployeeId()); // Employee id

            preparedStatement.executeUpdate();

            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    void deleteEmployee(Employee employee) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM employee WHERE employee_id=?"
            );
            preparedStatement.setInt(1, employee.getEmployeeId());

            preparedStatement.executeUpdate();

            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

public class DAOExample {
    public static void main(String[] args) {
        EmployeeDAO employeeDAO = new EmployeeDAO();
        employeeDAO.connect();

        Employee employee = new Employee(
                113,
                "Anjoe", "S", "Nambadan",
                21,
                1,
                211,
                20000
        );

        //CREATE
        employeeDAO.createEmployee(employee);

        //READ
        Employee employee1 = employeeDAO.readEmployee(employee.getEmployeeId());

        //UPDATE
        employee1.setDepartmentId(2);
        employeeDAO.updateEmployee(employee1);

        //DELETE
        employeeDAO.deleteEmployee(employee1);
    }
}
