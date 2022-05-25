package server;

import java.io.*;
import java.net.Socket;


/**
 * A class representing a connection between a client and the server, handled
 * in a secondary thread.
 */
public class Session implements Runnable {

    private final int clientNumber;
    private final Socket socketOfServer;

    public Session(Socket socketOfServer, int clientNumber) {
        this.clientNumber = clientNumber;
        this.socketOfServer = socketOfServer;
        Server.log("\uD83D\uDFE2 New connection with client #" + this.clientNumber + " at " + socketOfServer);
    }

    @Override
    public void run() {

        try (DataInputStream dis = new DataInputStream(new BufferedInputStream(socketOfServer.getInputStream()));
             DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socketOfServer.getOutputStream()))){

            while (true) {
                String[] request = dis.readUTF().split("\\s+");
                String response = "";

                switch (request[0]) {
                    case "get" -> {
                        Server.log("↘ Received a get request from client #" + clientNumber);
                        response = Showroom.INSTANCE.getShowsAsString();
                    }
                    case "book" -> {
                        Server.log("↘ Received a book request from client #" + clientNumber);
                        int showId = Integer.parseInt(request[1]);
                        int seats = Integer.parseInt(request[2]);
                        response = Showroom.INSTANCE.book(showId, seats);

                    }
                    case "exit" -> {
                        Server.log("\uD83D\uDD34 Client #" + clientNumber + " disconnected");
                        return;
                    }
                }
                dos.writeUTF(response);
                dos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}





