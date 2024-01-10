
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class mainserver { //bir port numarası ve bir abone listes baslattı.
    private List<String> subscribers;
    private int port;

    public mainserver(int port) {
        this.subscribers = new CopyOnWriteArrayList<>();
        this.port = port;
    }

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(port); //Server Socket Oluşturma:
            System.out.println("Server bu portta başladi " + port);

            while (true) {//İstemci Bağlantılarını Bekleme
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket)).start(); //Yeni İstemci Bağlantısı İçin Thread Başlatma:
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket clientSocket) {//Input ve Output Stream Oluşturma
        try (
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())
        ) {
            String request = (String) in.readObject(); //İstemciden İstek Okuma:
            String[] parts = request.split(" "); //İstekten Komut ve Parametreleri Ayırma:

            if ("ABONE OL".equals(parts[0])) {//Gelen Komuta Göre İşlem Yapma:
                handleSubscribe(parts[1]);
            } else if ("ABONE LİSTESİ".equals(parts[0])) {
                sendSubscribersList(out);
            } else if (parts.length == 2 && parts[0].matches("\\d+")) {
                handleUpdateMessage(Integer.parseInt(parts[0]), parts[1]);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void handleSubscribe(String username) {// abone ekleme
        subscribers.add(username);
        System.out.println(username + " abone oldu.");
    }

    private void sendSubscribersList(ObjectOutputStream out) throws IOException {//Abone listesi
        out.writeObject(subscribers);
        System.out.println("Abone listesini Clientte gönder.");
    }

    private void handleUpdateMessage(int sequenceNumber, String message) {
        System.out.println("GÜncelleme mesaji alindi: " + sequenceNumber + " " + message);
        // Güncelleme mesajını işleyin, gerekirse dahili durumu güncelleyin.
    }

    public static void main(String[] args) { // serverı bu portta başlat
        mainserver server = new mainserver(12345);
        server.start();
    }
}


