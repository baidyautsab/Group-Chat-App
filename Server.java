package withName;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private static int clientId = 1; // generating client id
    private static Map<Integer, PrintWriter> clientMap = new HashMap<>(); // storing the connected client with id
    private static Map<Integer, String> clientNames = new HashMap<>(); // storing the client with there id and name

    public static void main(String[] args) {
        System.out.println("Server is started!");
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(7777); // assigning the port no.
            System.out.println("Server is ready to connect");

            // Start a thread to read messages from the server console
            ConsoleReader consoleReader = new ConsoleReader(); // to read the data from server console
            Thread consoleReaderThread = new Thread(consoleReader); // converting the object as a thread object
            consoleReaderThread.start();

            while (true) {
                Socket socket = serverSocket.accept(); // accepting the client request with matched port number
//                System.out.println("Connection established with client: " + clientNames.get(clientId));
                BufferedReader nameReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String clientName = nameReader.readLine(); // reading the client name send from the client side
                clientNames.put(clientId, clientName); // adding the client with their name
                System.out.println("Connection established with client: " + clientName);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                clientMap.put(clientId, out); // adding the client with id and name(or we can say current message sender)
                ClientHandler clientHandler = new ClientHandler(socket, clientId++, consoleReader);
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (serverSocket != null)
                    serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static class ClientHandler extends Thread { // handel the multiple client
        private Socket socket;
        private BufferedReader br; // to read data
        private PrintWriter out; // to send data from console
        private int clientId; // to match with message sender client id
        private ConsoleReader consoleReader; // read the data from server console, which wil be broadcast to every client except the sender

        public ClientHandler(Socket socket, int clientId, ConsoleReader consoleReader) {
            this.socket = socket;
            this.clientId = clientId;
            try {
                br = new BufferedReader(new InputStreamReader(socket.getInputStream())); // accept data from client
                out = new PrintWriter(socket.getOutputStream(), true); // send data to client
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.consoleReader = consoleReader;
        }

        public void run() {
            try {
                while (true) {
                    String clientMsg = br.readLine(); // storing the client message
                    if (clientMsg == null) break; // Client disconnected
                    System.out.println(clientNames.get(clientId) + ": " + clientMsg);

                    // Broadcast the message to all clients except the sender
                    String messageToSend = clientNames.get(clientId) + ": " + clientMsg;
                    consoleReader.broadcast(messageToSend, clientId);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                    clientMap.remove(clientId);
                    clientNames.remove(clientId);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class ConsoleReader implements Runnable {
        public void run() {
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            try {
                while (true) {
                    String serverMsg = consoleReader.readLine(); // Read from console
                    broadcast("Server: " + serverMsg); // Broadcast message to all clients
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Method to broadcast a message to all clients except the sender
        public void broadcast(String message) {
            for (PrintWriter clientOut : clientMap.values()) {
                clientOut.println(message);
            }
        }

        // Method to broadcast a message to all clients except the sender
        public void broadcast(String message, int senderId) {
            for (int id : clientMap.keySet()) {
                if (id != senderId) { // compare with client id
                    PrintWriter clientOut = clientMap.get(id);
                    clientOut.println(message);
                }
            }
        }
    }
}

