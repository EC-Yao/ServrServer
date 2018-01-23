package com.company;

import java.sql.*;

/** December 13, 2017
 *  Eddy Yao
 *
 *      Database connection class - Instances server-database connection. This class also contains the main method,
 *  which will begin the connection and begin listening for clients.
 */

public class DatabaseConnection {

    public static Connection connection;

    private static DatabaseConnection dBConnection;

    // Connects java application to local SQL database
    private DatabaseConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.mariadb.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/servr?user=root&password=5723283329");
    }

    // Launches server
    public static void main(String[] args){
        initializeConnection();
        ClientConnection.listenSocket();
    }

    // Initializes connection with database
    private static void initializeConnection(){
        if (dBConnection == null) {
            try {
                dBConnection = new DatabaseConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
