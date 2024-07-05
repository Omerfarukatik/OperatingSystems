package newproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
    private static final String SERVER_ADDRESS = "localhost"; // Sunucu adresi
    private static final int SERVER_PORT = 12345; // Sunucu bağlantı noktası

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public Client() {
        try {
            // Sunucuya bağlan
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Connected to server.");

            // Giriş ve çıkış akışlarını al
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);

        // Kullanıcı adını sunucuya gönder
        System.out.print("Enter your name: ");
        String clientName = scanner.nextLine();
        out.println(clientName);

        // Komutları işle
        ExecutorService executor = Executors.newFixedThreadPool(2); // İki iş parçacığı

        executor.submit(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    String serverResponse = in.readLine();
                    if (serverResponse != null) {
                        System.out.println("Server response: " + serverResponse);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        executor.submit(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    System.out.println("Please choose an action:");
                    System.out.println("1. Reserve a seat (Usage: reserve <seat>)");
                    System.out.println("2. Cancel a reservation (Usage: cancel <seat>)");
                    System.out.println("3. Query a reservation (Usage: query)");
                    System.out.println("4. Exit");

                    String userInput = scanner.nextLine();
                    String[] parts = userInput.split(" ");
                    String command = parts[0];
                    String seat;

                    switch (command) {
                        case "reserve":
                        case "cancel":
                            if (parts.length < 2) {
                                System.out.println("Usage: " + command + " <seat>");
                                continue;
                            }
                            seat = parts[1];
                            out.println(command + " " + seat);
                            break;
                        case "query":
                            out.println(command);
                            break;
                        case "exit":
                            out.println(command);
                            System.out.println("Exiting...");
                            executor.shutdown();
                            socket.close();
                            return;
                        default:
                            System.out.println("Invalid command.");
                            continue;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Executor'ı kapat
        executor.shutdown();
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }
}
