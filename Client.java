import java.io.*;
import java.net.Socket;
import java.util.List;

public class mainclient {
    private String serverAddress;
    private int serverPort;

    public mainclient(String serverAddress, int serverPort) { //bir port numarası ve bir abone listesi tanımladık
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void subscribe(String username) {
        try (
                Socket socket = new Socket(serverAddress, serverPort);// sunucuya bağlanma
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream())// soketin veri alışverisi nesnelerle
        ) {
            out.writeObject("ABONE OL " + username);
            System.out.println("servera abone olundu.");//sunucuya aboen ol istegi ve olundu bildirimi

            // Örnek: Sunucudan abone listesini alma
            List<Object> subscribersList = (List<Object>) in.readObject();
            System.out.println("ABONE LİSTESİ: " + subscribersList);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        mainclient client = new mainclient("localhost", 12345);
        client.subscribe("Cüneyt");
    }
}
    public static void main(String[] args) {
        Client client = new Client("localhost", 12345);
        client.subscribe("Cüneyt");
    }
}
