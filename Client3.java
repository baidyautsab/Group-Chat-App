package withName;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client3 {
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    public Client3() {
        try {
            System.out.println("Sending request to server...");
            socket = new Socket("127.0.0.1", 7777);
            System.out.println("..........");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void startReading() { // accepting data
        // creating a thread object of Runnable interface using lambda expression
        Runnable r1 = () -> {
            try {
                while (true) { // for now the loop will be executed for infinite time
                    String serverMsg = br.readLine();
                    if (serverMsg == null) break; // Server disconnected
                    System.out.println(serverMsg);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        new Thread(r1).start(); // executing the thread
    }

    public void startWriting() { // sending data
        // creating a thread object of Runnable interface using lambda expression
        Runnable r2 = () -> {
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            try {
                System.out.print("Enter your name: ");
                String name = consoleReader.readLine();
                System.out.println(name + ", you are now connected with the server.....");
                out.println(name); // Send the name to server
                out.flush();

                while (true) {
                    String clientMsg = consoleReader.readLine(); // Read from console
                    out.println(clientMsg); // Send the message to server
                    out.flush();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        new Thread(r2).start(); // executing the thread
    }

    public static void main(String[] args) {
        System.out.println("Client is ready");
        Client2 client = new Client2();
        client.startReading();
        client.startWriting();
    }
}
