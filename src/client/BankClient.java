package client;

import shared.SocketCommunicationBus;

import java.io.IOException;
import java.net.Socket;

public class BankClient {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";


    public static void main(String[] args) throws IOException {
        Socket serverSocket = null;

        try {
            serverSocket = new Socket("localhost", 8081);
        } catch (Exception e) {
            System.out.println("Could not establish a connection with server.");
            System.exit(-1);
        }

        System.out.println("Connection with server has been established: " + serverSocket);

        SocketCommunicationBus communicationBus = new SocketCommunicationBus(serverSocket);
        communicationBus.registerListener(input -> System.out.println(ANSI_RED + input + ANSI_RESET));

        while(true) {
            communicationBus.handleIncomingInput();
        }
    }
}
