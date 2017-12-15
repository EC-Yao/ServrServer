package com.company;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

public class ServerAPI {

    private static Statement st;
    private static ResultSet rs;
    private static final String getAll = "SELECT * FROM Users;";
    private static final String insertUser = "INSERT INTO Users (Username, PIN, Name, Email, Phone, City, Country)" +
            "VALUES(";
    private static final String deleteUser = "DELETE FROM Users WHERE UserID = ";
    private static final String lastUser = "SELECT MAX(UserID) FROM Users;";
    private static final String resetIncrement = "ALTER TABLE Users AUTO_INCREMENT = ";

    private static String query;

    private static ArrayList<String> createUser(int ID, String username, String PIN, String name, String email, String
                                               phone, String city, String country){
        return new ArrayList<>(Arrays.asList(Integer.toString(ID), username, PIN, name, email, phone, city, country));
    }

    public static void getUsers(){
        query = getAll;
        try {
            st = DatabaseConnection.connection.createStatement();
            rs = st.executeQuery(query);
            while (rs.next())
            {
                int id = rs.getInt("UserID");
                String username = rs.getString("Username");
                String PIN = rs.getString("PIN");
                String name = rs.getString("Name");
                String email = rs.getString("Email");
                String phone = rs.getString("Phone");
                String city = rs.getString("City");
                String country = rs.getString("Country");

                System.out.println(createUser(id, username, PIN, name, email, phone, city, country));
            }
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addUser(ArrayList<String> user){
        query = insertUser + "'" + user.get(1) + "'";
        for (int i = 2; i < user.size(); i++) {
            query += ", '" + user.get(i) + "'";
        }
        query += ");";

        try {
            st = DatabaseConnection.connection.createStatement();
            rs = st.executeQuery(query);
            st.close();
            System.out.println("Successfully added " + user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getLastUserID(){
        try {
            st = DatabaseConnection.connection.createStatement();
            rs = st.executeQuery(lastUser);
            rs.next();
            return rs.getInt("MAX(UserID)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void deleteUser(int id){
        query = deleteUser + id + ";";

        try {
            st = DatabaseConnection.connection.createStatement();
            rs = st.executeQuery(query);
            st.close();
            System.out.println("Successfully deleted User # " + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setAutoIncrement(int increment){
        query = resetIncrement + increment + ";";

        try{
            st = DatabaseConnection.connection.createStatement();
            rs = st.executeQuery(query);
            st.close();
            System.out.println("Successfully reset auto-increment to " + increment);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
