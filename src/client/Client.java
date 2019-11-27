package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

/**
 * The Client is a program that connects to the Server, and is able to chat in the application through the scanner.
 *
 * @author Joshua Ransom
 * @version 1.0
 * @since 11/27/2019
 *
 */
public class Client {
    private final static int serverPort = 1234;
    private final static String serverHost = "127.0.0.1";

    public static void main(String[] args) throws IOException {
        Scanner scn = new Scanner(System.in);

        Socket socket = null;

        try {
            socket = new Socket(serverHost, serverPort);
        } catch (ConnectException ex) {
            System.exit(1);
        }

        DataInputStream inputStream = new DataInputStream(socket.getInputStream());
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

        Thread sendMessage = new Thread(() -> {
            while (true) {
                System.out.println("Enter your message:");
                String msg = scn.nextLine();
                try {
                    outputStream.writeUTF(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (msg.toLowerCase().equals("logout")) {
                    System.out.println("You have logged out.");
                    System.exit(0);
                    return;
                }
            }
        });

        Thread readMessage = new Thread(() -> {
            while (true) {
                try {
                    String msg = inputStream.readUTF();
                    System.out.println(msg);
                } catch (IOException e) {
                    System.out.println("Server has closed.");
                    System.exit(1);
                    return;
                }
            }
        });

        sendMessage.start();
        readMessage.start();
    }
} 
