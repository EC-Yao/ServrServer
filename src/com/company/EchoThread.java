package com.company;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

/** January 10, 2018
 *  Eddy Yao
 *
 *      Client thread class - This class will handle all the logic and networking between the server and client. A
 *  separate thread is required so that the server is able to handle multiple clients at once.
 */

public class EchoThread extends Thread {

    private Socket socket;
    private static BufferedReader in;
    private static PrintWriter out;
    private static String command;

    // Constructor method - Defines the socket so that each thread can handle a separate client
    public EchoThread (Socket clientSocket){
        this.socket = clientSocket;
    }

    // Main method to handle the logic behind each request
    public void run(){
        try{
            // Opens I/O streams between server and client
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Reads the first line as the identifying command - This indicates what the client is trying to do (e.g
            // registering, logging in, searching for services, etc.)
            command = in.readLine();

            // Executes SQL queries against the database accordingly, using the ServerAPI class as reference. Returns
            // any relevant information the client may need in return, or simply pings the client that the request
            // was successfully handled
            switch(command){
                case "register":
                    ServerAPI.addUser(new ArrayList<>(Arrays.asList(in.readLine().split(","))));
                    out.println("Registered");
                    break;
                case "login":
                    out.println(ServerAPI.isValidCredential(in.readLine()));
                    break;
                case "create_service":
                    ServerAPI.addService(new ArrayList<>(Arrays.asList(in.readLine().split(","))));
                    out.println("Successfully added service");
                    break;
                case "delete_service":
                    ServerAPI.deleteService(Integer.parseInt(in.readLine()));
                    out.println("Successfully deleted service");
                    break;
                case "personal_services":
                    out.println(ServerAPI.getUserServices(Integer.parseInt(in.readLine())));
                    break;
                case "search_services":
                    out.println(ServerAPI.searchServices(in.readLine()));
                    break;
                case "stream_service":
                    out.println(ServerAPI.getStreamServices());
                    break;
                case "get_user":
                    out.println(ServerAPI.getUser(Integer.parseInt(in.readLine())));
                    break;
                default:
                    // Pings back the command if it is not recognized
                    out.println(command);
            }
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }
}
