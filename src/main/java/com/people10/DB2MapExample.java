package com.people10;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

class DBEmployee {
    private final int employeeId;
    private String name;
    private int age;

    public DBEmployee(int employeeId, String name, int age) {
        this.employeeId = employeeId;
        this.name = name;
        this.age = age;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}

public class DB2MapExample {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Map<Integer, DBEmployee> map= new HashMap<>();
        Class.forName("com.mysql.cj.jdbc.Driver");

        String url = "jdbc:mysql://localhost:3306/mydatabase";
        String userName = "root";
        String password = "Password@123";

        Connection connection = DriverManager.getConnection(url, userName, password);

        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery("SELECT * FROM employee WHERE name LIKE 'Anjoe'");

        while (resultSet.next()) {
            map.put(
                    resultSet.getInt(1),
                    new DBEmployee(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3))
            );
        }

        for(int employeeId : map.keySet()) {
            System.out.println(map.get(employeeId));
        }

        statement.close();
        connection.close();
    }
}
