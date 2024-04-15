package com.people10;
import java.sql.*;

class Employee {
    private final int employeeId;
    private String firstName;
    private String middeleName;
    private String lastName;
    private int age;
    private int departmentId;
    private int managerId;
    private float salary;

    public Employee(int employeeId, String firstName, String middeleName, String lastName, int age, int departmentId, int managerId, float salary) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.middeleName = middeleName;
        this.lastName = lastName;
        this.age = age;
        this.departmentId = departmentId;
        this.managerId = managerId;
        this.salary = salary;
    }
}

class Department {
    private final int departmentId;
    private String departmentName;

    public Department(int departmentId, String departmentName) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
    }
}

public class CRUDExample {
    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");

        String url = "jdbc:mysql://localhost:3306/mydatabase";
        String userName = "root";
        String password = "Password@123";


    }
}
