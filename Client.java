package uygulama;
import java.io.*;
import java.net.Socket;
import java.util.List;

public class Client {
    private String serverAddress;
    private int serverPort;

    public Client(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void subscribe(String username) {
        try (
                Socket socket = new Socket(serverAddress, serverPort);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {
            out.writeObject("SUBSCRIBE " + username);
            System.out.println("Subscribed to the server.");

            // Example: Receive the list of subscribers from the server
            List<String> subscribersList = (List<String>) in.readObject();
            System.out.println("Subscribers list: " + subscribersList);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client("localhost", 12345);
        client.subscribe("Cüneyt");
    }
}
