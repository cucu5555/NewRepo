import java.io.*;
import java.net.Socket;
import java.util.List;

public class mainclient {
    private final String serverAddress;
    private final int serverPort;

    public mainclient(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void subscribe(String username) {
        try (
                Socket socket = new Socket(serverAddress, serverPort);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {
            SubscriptionRequest subscriptionRequest = new SubscriptionRequest("ABONE OL", username);
            out.writeObject(subscriptionRequest);

            System.out.println("servera abone olundu.");

            // Sunucudan gelen nesnenin tip kontrolü
            Object receivedObject = in.readObject();
            if (receivedObject instanceof List) {
                List<?> rawList = (List<?>) receivedObject;
                // List<String> olarak kullanmaya devam etmek istiyorsanız
                @SuppressWarnings("unchecked")
                List<String> subscribersList = (List<String>) rawList;
                System.out.println("ABONE LİSTESİ: " + subscribersList);
            } else {
                System.out.println("Geçersiz nesne türü alındı.");
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        mainclient client = new mainclient("localhost", 12345);
        client.subscribe("Cüneyt");
    }
}
