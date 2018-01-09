package com.company;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class ClientConnection {
    private static int serverPort = 8001;
    private static String server_IP;
    private static ServerSocket server;
    private static Socket client;
    private static BufferedReader in;
    private static PrintWriter out;
    private static String command;

    public static void listenSocket(){
        try{
            InetAddress iAddress = InetAddress.getLocalHost();
            server_IP = iAddress.getHostAddress();
            server = new ServerSocket(serverPort);
            System.out.println("Server IP address : " + server_IP);
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            System.exit(-1);
        }

        while (true){
            try{
                client = server.accept();
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                out = new PrintWriter(client.getOutputStream(), true);

                command = in.readLine();

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
                    case "delete_service":
                        ServerAPI.deleteService(Integer.parseInt(in.readLine()));
                        out.println("Successfully deleted service");
                    case "personal_services":
                        out.println(ServerAPI.getUserServices(Integer.parseInt(in.readLine())));
                    case "search_services":
                        out.println(ServerAPI.searchServices(in.readLine()));
                    default:
                        out.println(command);
                }

                System.out.println("Returned ping to client");
            } catch (Exception e) {
                System.out.println(Arrays.toString(e.getStackTrace()));
            }
        }
    }
}