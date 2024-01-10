package uygulama;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    private List<String> subscribers;
    private int port;

    public Server(int port) {
        this.subscribers = new CopyOnWriteArrayList<>();
        this.port = port;
    }

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server bu portta başladi " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket clientSocket) {
        try (
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())
        ) {
            String request = (String) in.readObject();
            String[] parts = request.split(" ");

            if ("ABONE OL".equals(parts[0])) {
                handleSubscribe(parts[1]);
            } else if ("ABONE OLUN".equals(parts[0])) {
                sendSubscribersList(out);
            } else if (parts.length == 2 && parts[0].matches("\\d+")) {
                handleUpdateMessage(Integer.parseInt(parts[0]), parts[1]);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void handleSubscribe(String username) {
        subscribers.add(username);
        System.out.println(username + " abone oldu.");
    }

    private void sendSubscribersList(ObjectOutputStream out) throws IOException {
        out.writeObject(subscribers);
        System.out.println("Abone listesini Clientte gönder.");
    }

    private void handleUpdateMessage(int sequenceNumber, String message) {
        System.out.println("GÜncelleme mesaji alindi: " + sequenceNumber + " " + message);
        // Handle the update message, update the internal state if needed.
    }

    public static void main(String[] args) {
        Server server = new Server(12345);
        server.start();
    }
}


           
