# Chat Application using Java Socket and Multi-Threading

This is a simple chat application built in Java that utilizes socket programming and multi-threading concepts. It consists of a server and multiple clients that can communicate with each other over a network.

## Features

- Multiple clients can connect to the server simultaneously.
- Each client can send messages to the server, which are then broadcasted to all other connected clients.
- The server can also send messages to all clients.

## Requirements

- Java Development Kit (JDK) installed on your system.
- Basic understanding of Java socket programming and multi-threading.

## Usage

1. **Server Setup:**
    - Run the `Server` class.
    - The server will start listening for client connections on port `7777`.

2. **Client Setup:**
    - Run the `Client1` class to initiate a client.
    - You will be prompted to enter your name. Once entered, you'll be connected to the server.
    - You can now start sending and receiving messages.

3. **Sending Messages:**
    - Clients can type messages in the console and hit Enter to send them to the server.
    - The server will then broadcast these messages to all other connected clients.

4. **Receiving Messages:**
    - Clients receive messages from other clients via the server.
    - Messages received from other clients are displayed in the console.

## Code Structure

- `Server.java`: Contains the server implementation.
- `Client1.java`: Contains the client implementation.
- `ClientHandler`: Nested class within `Server.java` responsible for handling communication with individual clients.
- `ConsoleReader.java`: A helper class used by the server to read messages from the console and broadcast them to clients.
