import java.io.Serializable;

public class SubscriptionRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String command;
    private String username;

    public SubscriptionRequest(String command, String username) {
        this.command = command;
        this.username = username;
    }

    // Getter ve Setter metotları
    //...
}
