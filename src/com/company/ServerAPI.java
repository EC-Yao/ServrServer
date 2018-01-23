package com.company;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

/** December 13, 2017
 *  Eddy Yao
 *
 *      API class for entire server. This class hosts a variety of methods regarding interactions with the database, and
 *  will construct various SQL queries to be executed against the database as required.
 */

public class ServerAPI {

    private static Statement st;
    private static ResultSet rs;

    // Pre-constructed SQL queries which can be called upon as necessary
    private static final String getAllUsers = "SELECT * FROM Users;";
    private static final String getAllServices = "SELECT * FROM Services;";

    private static final String insertUser = "INSERT INTO Users (Username, PIN, Email, Phone, City, Country)" +
            "VALUES(";
    private static final String deleteUser = "DELETE FROM Users WHERE UserID = ";
    private static final String getUser = "SELECT * FROM Users WHERE UserID = ";
    private static final String lastUser = "SELECT MAX(UserID) FROM Users;";

    private static final String resetIncrementUsers = "ALTER TABLE Users AUTO_INCREMENT = ";
    private static final String resetIncrementServices = "ALTER TABLE Services AUTO_INCREMENT = ";
    private static final String checkCredential = "SELECT * FROM Users WHERE Email = '";

    private static final String flushServices = "DELETE FROM Services;";
    private static final String addService = "INSERT INTO Services (UserID, ServiceName, ServiceDescription, Price) " +
            "VALUES(";
    private static final String deleteService = "DELETE FROM Services WHERE ServiceID = ";
    private static final String getUserServices = "SELECT * FROM Services WHERE UserID = ";

    private static final String searchServices = "SELECT * FROM SERVICES WHERE ServiceName LIKE ";
    private static final String getStreamServices = "SELECT * FROM Services ORDER BY ServiceID DESC LIMIT 10;";

    // Temporary variables - used for casting information between nested array lists and strings
    private static String query;
    private static ArrayList<ArrayList<String>> serviceQuery;
    private static ArrayList<String> keywordQuery;

    // Creates an array list representing a user, given the result returned by the database as a parameter
    private static ArrayList<String> createUser(ResultSet result){
        try {
            return new ArrayList<>(Arrays.asList(Integer.toString(result.getInt("UserID")),
                    result.getString("Username"), result.getString("PIN"), result.getString("Email"),
                    result.getString("Phone"), result.getString("city"), result.getString("Country")));
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    // Creates an array list representing a service, given the result returned by the database as a parameter
    private static ArrayList<String> createService(ResultSet result){
        try {
            return new ArrayList<>(Arrays.asList(Integer.toString(result.getInt("ServiceID")),
                    Integer.toString(result.getInt("UserID")), result.getString("ServiceName"),
                    result.getString("ServiceDescription"), result.getString("Price")));
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    // Retrieves and prints a list of all users - Debugging
    public static void getUsers(){
        // Sets the SQL query
        query = getAllUsers;
        try {
            // Creates and executes the command for the database using the given query
            st = DatabaseConnection.connection.createStatement();
            rs = st.executeQuery(query);

            // Parses through and prints each individual result
            while (rs.next())
            {
                System.out.println(createUser(rs));
            }
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Retrieves and prints a list of all services - Debugging
    public static void getServices(){
        // See getUsers() for more detailed commenting about the query execution process
        query = getAllServices;
        try {
            st = DatabaseConnection.connection.createStatement();
            rs = st.executeQuery(query);
            while (rs.next())
            {
                System.out.println(createService(rs));
            }
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Creates / Registers a user to the database, given an array list representing the user as a parameter
    public static void addUser(ArrayList<String> user){
        // Constructing the query based on the user as defined by the registration form on the client-side
        query = insertUser + "'" + user.get(0) + "'";
        for (int i = 1; i < user.size(); i++) {
            query += ", '" + user.get(i) + "'";
        }
        query += ");";

        // See getUsers() for more detailed commenting about the query execution process
        try {
            st = DatabaseConnection.connection.createStatement();
            rs = st.executeQuery(query);
            st.close();
            System.out.println("Successfully added " + user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Finds the most recent user (i.e the one with the highest user ID) - Debugging
    public static int getLastUserID(){
        try {
            // See getUsers() for more detailed commenting about the query execution process
            st = DatabaseConnection.connection.createStatement();
            rs = st.executeQuery(lastUser);
            rs.next();
            return rs.getInt("MAX(UserID)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Deletes a user given their user ID as an identifier - Debugging
    public static void deleteUser(int id){
        // See getUsers() for more detailed commenting about the query execution process
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

    // Returns the information about a user given their user ID as an identifier
    public static ArrayList<String> getUser(int id){
        // See getUsers() for more detailed commenting about the query execution process
        query = getUser + id + ";";

        try {
            st = DatabaseConnection.connection.createStatement();
            rs = st.executeQuery(query);
            st.close();

            // Returns the user if they exist, returns null if otherwise
            if (rs.next()){
                return createUser(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Resets the user ID counter on the database - Debugging
    public static void setAutoIncrementUsers(int increment){
        query = resetIncrementUsers + increment + ";";

        try{
            st = DatabaseConnection.connection.createStatement();
            rs = st.executeQuery(query);
            st.close();
            System.out.println("Successfully reset auto-increment to " + increment);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    // Verifies is entered credentials are valid, and returns the corresponding user if they are
    public static ArrayList<String> isValidCredential(String credential){
        // Constructs query based on credentials
        query = checkCredential + credential.split(":")[0] + "' AND PIN = " + credential.split(":")[1];

        // See getUsers() for more detailed commenting about the query execution process
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

    // Creates a service on the database, given an array list describing the service
    public static void addService(ArrayList<String> service){
        // Constructs the query based on the given information
        query = addService + "'" + service.get(0) + "'";
        for (int i = 1; i < service.size(); i++) {
            query += ", '" + service.get(i) + "'";
        }
        query += ");";

        // See getUsers() for more detailed commenting about the query execution process
        try {
            st = DatabaseConnection.connection.createStatement();
            rs = st.executeQuery(query);
            st.close();
            System.out.println("Successfully added " + service);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Deletes a service using the service ID as an identifier
    public static void deleteService(int serviceID){
        // See getUsers() for more detailed commenting about the query execution process
        query = deleteService + serviceID + ";";

        try {
            st = DatabaseConnection.connection.createStatement();
            rs = st.executeQuery(query);
            st.close();
            System.out.println("Successfully removed service #" + serviceID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Resets the service ID counter on the database - Debugging
    public static void setAutoIncrementServices(int increment){
        // See getUsers() for more detailed commenting about the query execution process
        query = resetIncrementServices + increment + ";";

        try{
            st = DatabaseConnection.connection.createStatement();
            rs = st.executeQuery(query);
            st.close();
            System.out.println("Successfully reset auto-increment to " + increment);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    // Retrieves all the services registered under a user, using their user ID as an identifier
    public static ArrayList<ArrayList<String>> getUserServices(int userID){
        // Constructs query based on inputted ID
        query = getUserServices + userID + ";";

        // Declares a service query in order to handle casting the information between string and a nested array list
        serviceQuery = new ArrayList<>();

        // See getUsers() for more detailed commenting about the query execution process
        try {
            st = DatabaseConnection.connection.createStatement();
            rs = st.executeQuery(query);
            while (rs.next())
            {
                serviceQuery.add(createService(rs));
            }
            st.close();

            System.out.println(serviceQuery);
            return serviceQuery;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Deletes all services - Debugging
    public static void flushServices(){
        // See getUsers() for more detailed commenting about the query execution process
        query = flushServices;

        try {
            st = DatabaseConnection.connection.createStatement();
            rs = st.executeQuery(query);
            System.out.println("Successfully flushed services table");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    // Searches the database for services using a given search query. This method will break down the query into
    // individual words and search for each word separately, returning a list of all services whose titles contain any
    // of the keywords in the original search query
    public static ArrayList<ArrayList<String>> searchServices(String searchQuery){
        // Constructs a list of all keywords in the query
        serviceQuery = new ArrayList<>();
        if(searchQuery.contains(" ")){
            keywordQuery = new ArrayList<>(Arrays.asList(searchQuery.split(" ")));
        } else {
            keywordQuery = new ArrayList<>(Arrays.asList(searchQuery));
        }

        // Constructs the SQL query to be executed based on the keywords gathered previously
        query = searchServices + "'%" + keywordQuery.get(0) + "%'";
        for (int i = 1; i < keywordQuery.size(); i++) {
            query += " OR ServiceName LIKE '%" + keywordQuery.get(i) + "%'";
        }
        query += ";";

        // See getUsers() for more detailed commenting about the query execution process
        try {
            st = DatabaseConnection.connection.createStatement();
            rs = st.executeQuery(query);
            while (rs.next())
            {
                serviceQuery.add(createService(rs));
            }
            st.close();

            return serviceQuery;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Returns the services to be displayed in the stream. This will simply retrieve the most recently created services
    public static ArrayList<ArrayList<String>> getStreamServices(){
        // Declares an arraylist to help handle casting between string and nested array lists
        serviceQuery = new ArrayList<>();
        query = getStreamServices;

        // See getUsers() for more detailed commenting about the query execution process
        try{
            st = DatabaseConnection.connection.createStatement();
            rs = st.executeQuery(query);
            while (rs.next())
            {
                serviceQuery.add(createService(rs));
            }
            st.close();

            System.out.println(serviceQuery);
            return serviceQuery;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
