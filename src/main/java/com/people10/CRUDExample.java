//UNFINISHED
package com.people10;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class CRUDExample {
    static BufferedReader br;
    static Connection connection;
    static Set<Integer> employeeSet;
    static Set<Integer> departmentSet;

    static void createEmployee() throws IOException, SQLException {
        int employeeId, age, departmentId, managerId;
        String name, firstName, middleName, lastName;
        float salary;
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO employee VALUES(?, ?, ?, ?, ?, ?, ?, ?)"
        );

        //Reading Data
        while (true) {
            System.out.print("Enter the employee id: ");
            employeeId = Integer.parseInt(br.readLine());
            if (employeeSet.contains(employeeId)) {
                System.out.println("ERROR: Employee already exists. Try again!");
            } else {
                break;
            }
        }
        while (true) {
            System.out.print("Enter the full name: ");
            name = br.readLine();
            StringTokenizer stringTokenizer = new StringTokenizer(name, " ");
            if (stringTokenizer.countTokens() == 3) {
                firstName = stringTokenizer.nextToken();
                middleName = stringTokenizer.nextToken();
                lastName = stringTokenizer.nextToken();
                break;
            } else if (stringTokenizer.countTokens() == 2) {
                firstName = stringTokenizer.nextToken();
                middleName = null;
                lastName = stringTokenizer.nextToken();
                break;
            } else {
                System.out.println("Enter a valid name with at-least first and last name!");
            }
        }
        System.out.print("Enter the age: ");
        age = Integer.parseInt(br.readLine());
        System.out.print("Enter the manager id (-1 if not assigned): ");
        managerId = Integer.parseInt(br.readLine());

        while (true) {
            System.out.print("Enter the department id: ");
            departmentId = Integer.parseInt(br.readLine());
            if (!departmentSet.contains(departmentId)) {
                System.out.println("ERROR: Department does not exist. Try again!");
            } else {
                break;
            }
        }

        System.out.print("Enter the salary: ");
        salary = Float.parseFloat(br.readLine());

        //Running query
        preparedStatement.setInt(1, employeeId); // Employee id
        preparedStatement.setString(2, firstName); // First name
        preparedStatement.setString(3, middleName); // Middle name
        preparedStatement.setString(4, lastName); // Last name
        preparedStatement.setInt(5, age); // Age
        preparedStatement.setInt(6, departmentId); // Department id
        preparedStatement.setInt(7, managerId); // Manager id
        preparedStatement.setFloat(8, salary); // salary

        preparedStatement.executeUpdate();

        preparedStatement.close();
        System.out.println("Successfully created...\n");
        employeeSet.add(employeeId);
    }

    static void createDepartment() throws SQLException, IOException {
        int departmentId;
        String departmentName;

        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO department VALUES(?, ?)"
        );

        //Read inputs
        while (true) {
            System.out.print("Enter the department id: ");
            departmentId = Integer.parseInt(br.readLine());
            if (departmentSet.contains(departmentId)) {
                System.out.println("ERROR: Department already exists. Try again!");
            } else {
                break;
            }
        }
        System.out.print("Enter the department name: ");
        departmentName = br.readLine();

        //Running query
        preparedStatement.setInt(1, departmentId);
        preparedStatement.setString(2, departmentName);

        preparedStatement.executeUpdate();

        preparedStatement.close();

        System.out.println("Successfully created...\n");

        departmentSet.add(departmentId);
    }

    static void create() throws IOException, SQLException {
        System.out.println("x---CREATE---x\n1.Employee\n2.Department\n3.Go Back");
        System.out.print("Enter your choice: ");
        int choice = Integer.parseInt(br.readLine());
        switch (choice) {
            case 1:
                createEmployee();
                break;
            case 2:
                createDepartment();
                break;
            case 3:
                return;
            default:
                System.out.println("Wrong choice!!!");

        }
    }

    static void displayEmployees(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            System.out.printf(
                    "{\n" +
                            "\tid: %d,\n" +

                            "\tname: %s%s%s,\n" +
                            "\tAge: %d,\n" +
                            "\tDepartment: %s,\n" +
                            "\tManager: %s,\n" +
                            "\tSalary: %f\n" +
                            "}\n",
                    resultSet.getInt(1),
                    resultSet.getString(2),
                    (resultSet.getString(3) == null) ? " " : " " + resultSet.getString(3) + " ",
                    resultSet.getString(4),
                    resultSet.getInt(5),
                    resultSet.getString(6),
                    ((resultSet.getInt(7) != -1) ? resultSet.getInt(7) : "NOT ASSIGNED"),
                    resultSet.getFloat(8)
            );
        }
    }

    static void readById() throws IOException, SQLException {
        System.out.print("Enter the employee id: ");
        int employeeId = Integer.parseInt(br.readLine());
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM employee WHERE employee_id=" + employeeId);
        displayEmployees(resultSet);
        statement.close();
    }

    static void readByName() throws IOException, SQLException {
        System.out.print("Enter the employee name: ");
        String employeeName = br.readLine();
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM employee WHERE first_name LIKE ? " +
                        "OR middle_name LIKE ? OR last_name LIKE ?"
        );
        preparedStatement.setString(1, "%" + employeeName + "%");
        preparedStatement.setString(2, "%" + employeeName + "%");
        preparedStatement.setString(3, "%" + employeeName + "%");
        ResultSet resultSet = preparedStatement.executeQuery();
        displayEmployees(resultSet);
        preparedStatement.close();
    }

    static void readByDepartment() throws IOException, SQLException {
        readDepartment();
        Statement statement = connection.createStatement();
        System.out.print("Enter the department id: ");
        int departmentId = Integer.parseInt(br.readLine());
        ResultSet resultSet = statement.executeQuery("SELECT * FROM employee WHERE department_id=" + departmentId);
        displayEmployees(resultSet);
        statement.close();
    }

    static void readByManager() throws SQLException, IOException {
        readManager();
        Statement statement = connection.createStatement();
        System.out.print("Enter the manager id: ");
        int managerId = Integer.parseInt(br.readLine());
        ResultSet resultSet = statement.executeQuery("SELECT * FROM employee WHERE manager_id=" + managerId);
        displayEmployees(resultSet);
        statement.close();
    }

    static void readEmployee() throws IOException, SQLException {
        System.out.println("x---SEARCH BY---x\n1.Id\n2.Name\n3.Department\n4.Manager\n5.Exit");
        System.out.print("Enter your choice: ");
        int choice = Integer.parseInt(br.readLine());
        switch (choice) {
            case 1:
                readById();
                break;
            case 2:
                readByName();
                break;
            case 3:
                readByDepartment();
                break;
            case 4:
                readByManager();
                break;
            case 5:
                return;
            default:
                System.out.println("Wrong choice!!!");

        }
    }

    static void readDepartment() throws SQLException {
        System.out.println("x---DEPARTMENTS---x");
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM department");
        System.out.println("Department Id\tName");
        System.out.println("___________________");
        while (resultSet.next()) {
            System.out.println("\t" + resultSet.getInt(1) + "\t\t\t" + resultSet.getString(2));
        }
    }

    static void readManager() throws SQLException {
        System.out.println("x---MANAGERS---x");
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(
                "SELECT employee_id, first_name, last_name FROM employee " +
                        "WHERE employee_id IN " +
                        "(SELECT manager_id FROM employee);"
        );
        System.out.println("Employee Id\tName");
        System.out.println("___________________");
        while (resultSet.next()) {
            System.out.println("\t" + resultSet.getInt(1) + "\t\t" + resultSet.getString(2) + " " + resultSet.getString(3));
        }
    }

    static void read() throws IOException, SQLException {
        System.out.println("x---SEARCH---x\n1.Employee\n2.Department\n3.Go Back");
        System.out.print("Enter your choice: ");
        int choice = Integer.parseInt(br.readLine());
        switch (choice) {
            case 1:
                readEmployee();
                break;
            case 2:
                readDepartment();
                break;
            case 3:
                return;
            default:
                System.out.println("Wrong choice!!!");

        }
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");

        String url = "jdbc:mysql://localhost:3306/mydatabase";
        String userName = "root";
        String password = "Password@123";

        try {
            connection = DriverManager.getConnection(url, userName, password);
            System.out.println("Connection established...\n");

            employeeSet = new HashSet<>();
            departmentSet = new HashSet<>();
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT employee_id FROM employee");
            while (resultSet.next()) {
                employeeSet.add(resultSet.getInt(1));
            }

            resultSet = statement.executeQuery("SELECT department_id FROM department");
            while (resultSet.next()) {
                departmentSet.add(resultSet.getInt(1));
            }

            br = new BufferedReader(new InputStreamReader(System.in));
            int choice = 0;
            while (choice != 5) {
                System.out.println("x---MENU---x\n1.Create\n2.Read\n3.Update\n4.Delete\n5.Exit");
                System.out.print("Enter your choice: ");
                choice = Integer.parseInt(br.readLine());
                switch (choice) {
                    case 1:
                        create();
                        break;
                    case 2:
                        read();
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        connection.close();
                        break;
                    default:
                        System.out.println("Please try again...\n");
                }
            }
        } catch (SQLException | IOException e) {
            System.out.println("Could not connect to database\n" + e.getMessage());
        }
    }
}