package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

/**
 * The Server programs handle the entirety of the chat application, it accepts new clients as well as manages the
 * current counter and all the active clients.
 *
 * @author Joshua Ransom
 * @version 1.0
 * @since 11/27/2019
 *
 */
public class Server
{
    private static Vector<ClientHandler> activeClients = new Vector<>();
    private static int clientCounter = 0;
    private static int serverPort = 1234;

    /**
     * This is the main method which makes use of beginServer method.
     * @param args Unused.
     * @exception IOException On errors.
     * @see IOException
     */
    public static void main(String[] args) throws IOException
    {
        beginServer();
    }

    /**
     * This is the beginServer method which starts the server socket and handles all incoming connections to the socket,
     * as well as the creation of each new thread for the clients.
     * @return Nothing.
     * @exception IOException On server handling error.
     * @see IOException
     */
    private static void beginServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(serverPort);
        Socket socket;
        System.out.println("Server has started on port: " + serverPort);
        while (true) {
            socket = serverSocket.accept();
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            ClientHandler newClient = new ClientHandler(socket,"[Client " + clientCounter + "]", inputStream, outputStream);

            /*
             * Create new thread for each new client then add them to the List.
             */
            Thread t = new Thread(newClient);
            activeClients.add(newClient);
            t.start();
            System.out.println(newClient.getName() + " has connected");

            //Increment the counter of clients connected.
            clientCounter++;
        }
    }

    static Vector<ClientHandler> getActiveClients() {
        return activeClients;
    }
} 