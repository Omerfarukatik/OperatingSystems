package newproject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        int port = 12345;
        FlightReservation reservation = new FlightReservation();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                // Read the client's name from the input stream
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String clientName = in.readLine();

                ClientHandler clientHandler = new ClientHandler(socket, reservation, clientName);
                clientHandler.start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
