package com.company;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class DatabaseConnection {

    public static Connection connection;

    private static DatabaseConnection dBConnection;

    private DatabaseConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.mariadb.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/servr?user=root&password=5723283329");
    }

    public static void main(String[] args){
        initializeConnection();
        ServerAPI.flushServices();
        ServerAPI.setAutoIncrementServices(1);
        ServerAPI.addService(new ArrayList<>(Arrays.asList("1", "Alpha Tester", "Testing for alpha", "$1.99")));
        ServerAPI.addService(new ArrayList<>(Arrays.asList("1", "Alpha Alpha Tester", "Testing for alpha alpha", "$11.99")));
        ServerAPI.addService(new ArrayList<>(Arrays.asList("2", "Beta Tester", "Testing for beta", "$2.99")));
        ServerAPI.addService(new ArrayList<>(Arrays.asList("3", "Cheesecake Eater", "Eating cheesecake professionally", "$3.99")));
        ServerAPI.addService(new ArrayList<>(Arrays.asList("6", "Plumbing", "Plumbing since 1999", "$49.99")));
        ServerAPI.addService(new ArrayList<>(Arrays.asList("3", "Chemistry Tutor", "Masters in biochemistry. Will tutor high school chemistry", "$49.99")));
        ServerAPI.addService(new ArrayList<>(Arrays.asList("3", "DJ", "Party DJ for a litmas", "$99.99")));
        ServerAPI.addService(new ArrayList<>(Arrays.asList("2", "Developer", "Developing on Android platform, experienced with UI", "$34.99")));
        ServerAPI.addService(new ArrayList<>(Arrays.asList("6", "Accounting", "Will manage personal finances and taxes", "$149.99")));
        ServerAPI.addService(new ArrayList<>(Arrays.asList("7", "Lawn Mowing", "Self-explanatory", "$14.99")));
        ServerAPI.addService(new ArrayList<>(Arrays.asList("7", "Dice Roller", "Professional Gambling Services", "$79.99")));
        ServerAPI.addService(new ArrayList<>(Arrays.asList("12", "STEM Tutor", "Offering lessons in STEM", "$50.00")));
        ServerAPI.addService(new ArrayList<>(Arrays.asList("12", "Computer Science Teacher", "Will manage and teach a group of students CS", "$44.99")));
        ServerAPI.addService(new ArrayList<>(Arrays.asList("9", "Video Game Boosting", "Professional Account Booster", "$14.99")));
        ServerAPI.addService(new ArrayList<>(Arrays.asList("7", "Delivery / Pickup", "Will pickup and deliver anything within 3 days", "$4.99")));
        ServerAPI.addService(new ArrayList<>(Arrays.asList("6", "Piano Tutor", "Level 10 RCM First Class Honors w/ Distinction", "$15.99")));
        ServerAPI.getUsers();
        ServerAPI.getServices();
        ServerAPI.getUserServices(3);
        ServerAPI.searchServices("Alpha Cheesecake");
        ServerAPI.getStreamServices();
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
