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
    private static final String insertUser = "INSERT INTO Users (Username, PIN, Email, Phone, City, Country)" +
            "VALUES(";
    private static final String deleteUser = "DELETE FROM Users WHERE UserID = ";
    private static final String lastUser = "SELECT MAX(UserID) FROM Users;";
    private static final String resetIncrement = "ALTER TABLE Users AUTO_INCREMENT = ";
    private static final String checkCredential = "SELECT * FROM Users WHERE Email = '";

    private static String query;

    public static ArrayList<String> createUser(ResultSet result){
        try {
            return new ArrayList<>(Arrays.asList(Integer.toString(result.getInt("UserID")),
                    result.getString("Username"), result.getString("PIN"), result.getString("Email"),
                    result.getString("Phone"), result.getString("city"), result.getString("Country")));
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void getUsers(){
        query = getAll;
        try {
            st = DatabaseConnection.connection.createStatement();
            rs = st.executeQuery(query);
            while (rs.next())
            {
                System.out.println(createUser(rs));
            }
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addUser(ArrayList<String> user){
        query = insertUser + "'" + user.get(0) + "'";
        for (int i = 1; i < user.size(); i++) {
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

    public static ArrayList<String> isValidCredential(String credential){
        query = checkCredential + credential.split(":")[0] + "' AND PIN = " + credential.split(":")[1];
        System.out.println(query);
        try{
            st = DatabaseConnection.connection.createStatement();
            rs = st.executeQuery(query);
            st.close();
            if (rs.next()){
                return createUser(rs);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Rejected Login");
        return null;
    }
}
