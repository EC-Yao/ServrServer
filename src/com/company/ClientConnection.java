package com.company;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

/** December 20, 2017
 *  Eddy Yao
 *
 *      Client connection class - This class will handle any attempt the client-side application makes to connect with
 *  the server using sockets. Each time a client attempts to connect, the server will begin a new thread to handle the
 *  request, the logic of which is handled through the EchoThread class.
 */

public class ClientConnection {
    // Defining basic properties of the server
    private static int serverPort = 8001;
    private static String server_IP;
    private static ServerSocket server;

    // Listens for client connections
    public static void listenSocket(){
        // Print out basic server properties - Debugging
        try{
            InetAddress iAddress = InetAddress.getLocalHost();
            server_IP = iAddress.getHostAddress();
            server = new ServerSocket(serverPort);
            System.out.println("Server IP address : " + server_IP);
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            System.exit(-1);
        }

        // Permanently listens for a client. Will launch a new thread to handle the connection when one is found.
        while (true){
            try{
                new EchoThread(server.accept()).start();
            } catch (Exception e) {
                System.out.println(Arrays.toString(e.getStackTrace()));
            }
        }
    }
}