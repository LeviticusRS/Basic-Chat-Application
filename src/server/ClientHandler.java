package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * The ClientHandler is a class that handles each individual client as well as stores data of the specific client.
 *
 * @author Joshua Ransom
 * @version 1.0
 * @since 11/27/2019
 *
 */
class ClientHandler implements Runnable {
    private String name;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;
    private Socket socket;
    private boolean loggedIn;

    ClientHandler(Socket s, String name, DataInputStream dis, DataOutputStream dos) {
        this.inputStream = dis;
        this.outputStream = dos;
        this.name = name;
        this.socket = s;
        this.loggedIn = true;
    }

    @Override
    public void run() {
        String received;
        while (true) {
            try {
                received = inputStream.readUTF();

                if (received.equals("logout")) {
                    loggedIn = false;
                    socket.close();
                    break;
                }

                for (ClientHandler client : Server.getActiveClients()) {
                    if (client.equals(this)) {
                        client.getOutputStream().writeUTF("Message sent to others");
                        continue;
                    }
                    if (client.loggedIn) {
                        client.getOutputStream().writeUTF(name + ": " + received);
                    }
                }
            } catch (IOException e) {
               Server.getActiveClients().remove(this);
               System.out.println(name + " has disconnected.");
               break;
            }
        }
        try {
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public DataInputStream getInputStream() {
        return inputStream;
    }

    public DataOutputStream getOutputStream() {
        return outputStream;
    }
} 