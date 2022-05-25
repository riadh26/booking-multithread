package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

    private static final String ADDRESS = "localhost";
    private static final int PORT = 7771;
    private static final String GET_REGEX = "^get$";
    private static final String BOOK_REGEX = "book\\s+\\d+\\s+\\d+";
    private static final String EXIT_REGEX = "^exit$";
    private static final String HELP_REGEX = "^help$";

    public static void main(String[] args) {

        // try with auto-closeable resources
        // (closes everything declared in it implicitly in the end)
        try (Socket clientSocket = new Socket(ADDRESS, PORT);
             DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
             DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
             Scanner scanner = new Scanner(System.in)) {

            help();

            while (true) {
                System.out.print("$ ");
                String clientInput = scanner.nextLine().trim();

                if (clientInput.matches(GET_REGEX) || clientInput.matches(BOOK_REGEX)) {
                    dos.writeUTF(clientInput);
                    dos.flush();

                    // Read data sent from the server.
                    // By reading the input stream of the Client Socket.
                    String responseLine = dis.readUTF();
                    System.out.println(responseLine);
                } else if (clientInput.matches(EXIT_REGEX)) {
                    dos.writeUTF(clientInput);
                    dos.flush();
                    break;
                } else if (clientInput.matches(HELP_REGEX)) {
                    help();
                } else if (!clientInput.isEmpty()) {
                    System.out.println("Invalid command. Type 'help' for available commands.");
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + ADDRESS);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + ADDRESS);
        }
    }

    /**
     * Prints help in the command line.
     */
    private static void help() {
        System.out.println("Available commands:");
        System.out.println("\t⦁ get → get the list of shows");
        System.out.println("\t⦁ book [showId] [numberOfSeats] → book a number of seats in a show");
        System.out.println("\t⦁ exit → close connection with the server");
        System.out.println("\t⦁ help → show help");
    }
}
