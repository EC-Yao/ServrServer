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
                new EchoThread(server.accept()).start();
            } catch (Exception e) {
                System.out.println(Arrays.toString(e.getStackTrace()));
            }
        }
    }
}