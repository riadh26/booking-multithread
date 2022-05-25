package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final int PORT = 7771;
    private static int CLIENT_ID = 1;

    public static void log(String message) {
        System.out.println(message);
    }

    public static void main(String[] args) {
        System.out.println("Server is started and listening on port " + PORT);
        System.out.println("Waiting to accept user...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            while (true) {
                // Listen for a TCP connection request.
                Socket socket = serverSocket.accept();

                // Create a new thread for the accepted connection
                Session session = new Session(socket, CLIENT_ID++);
                new Thread(session).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
