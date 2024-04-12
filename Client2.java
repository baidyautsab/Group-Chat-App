package withName;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client2 {
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    public Client2() {
        try {
            // Establish connection with the server
            System.out.println("Sending request to server...");
            socket = new Socket("127.0.0.1", 7777);
            System.out.println("Connection established");

            // Initialize input and output streams for communication with the server
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void startReading() { // Method to start reading data from the server
        // Create a thread to continuously read data from the server
        Runnable r1 = () -> {
            try {
                while (true) { // Read data from the server until the connection is closed
                    String serverMsg = br.readLine();
                    if (serverMsg == null) break; // Exit the loop if the server disconnects
                    System.out.println(serverMsg);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        new Thread(r1).start(); // Start the thread
    }

    public void startWriting() { // Method to start sending data to the server
        // Create a thread to continuously send data to the server
        Runnable r2 = () -> {
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            try {
                // Prompt the user to enter their name
                System.out.print("Enter your name: ");
                String name = consoleReader.readLine();
                System.out.println(name + ", you are now connected with the server...");

                // Send the user's name to the server
                out.println(name);
                out.flush();

                // Continuously read input from the console and send it to the server
                while (true) {
                    String clientMsg = consoleReader.readLine(); // Read from console
                    out.println(clientMsg); // Send the message to server
                    out.flush();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        new Thread(r2).start(); // Start the thread
    }

    public static void main(String[] args) {
        System.out.println("Client is ready");
        // Create a new client instance, start reading and writing threads
        Client2 client = new Client2();
        client.startReading();
        client.startWriting();
    }
}
