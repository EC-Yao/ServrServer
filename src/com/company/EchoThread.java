package com.company;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Arrays;

public class EchoThread extends Thread {
    private Socket socket;
    private static BufferedReader in;
    private static PrintWriter out;
    private static String command;

    public EchoThread (Socket clientSocket){
        this.socket = clientSocket;
    }

    public void run(){
        try{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

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
                case "stream_service":
                    out.println(ServerAPI.getStreamServices());
                case "get_user":
                    out.println(ServerAPI.getUser(Integer.parseInt(in.readLine())));
                default:
                    out.println(command);
            }

            System.out.println("Returned ping to client");
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }
}
