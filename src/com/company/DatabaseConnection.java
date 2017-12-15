package com.company;

import java.sql.*;

public class DatabaseConnection {

    public static Connection connection;

    private static DatabaseConnection dBConnection;

    private DatabaseConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.mariadb.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/servr?user=root&password=5723283329");
    }

    public static void main(String[] args){
        initializeConnection();
        ClientConnection.listenSocket();
    }

    private static void initializeConnection(){
        if (dBConnection == null) {
            try {
                dBConnection = new DatabaseConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
