package newproject;

import java.io.*;
import java.net.Socket;

class ClientHandler extends Thread {
    private final Socket clientSocket;
    private final FlightReservation reservation;
    private final String clientName;
    private final PrintWriter out;

    public ClientHandler(Socket socket, FlightReservation reservation, String clientName) throws IOException {
        this.clientSocket = socket;
        this.reservation = reservation;
        this.clientName = clientName;
        this.out = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                String[] parts = inputLine.split(" ");
                String command = parts[0];
                String seat;

                switch (command) {
                    case "reserve":
                        seat = parts[1];
                        reservation.makeReservation(seat, clientName, out);
                        break;
                    case "cancel":
                        seat = parts[1];
                        reservation.cancelReservation(seat, clientName, out);
                        break;
                    case "query":
                        reservation.queryReservation(out);
                        break;
                    case "exit":
                        out.println("Goodbye " + clientName + "!");
                        return;
                    default:
                        out.println("Invalid command.");
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
