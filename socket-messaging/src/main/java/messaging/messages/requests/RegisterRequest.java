package messaging.messages.requests;

import messaging.messages.Message;

public class RegisterRequest extends Message {
    public final String playerName;
    public final String password;

    public RegisterRequest(String playerName, String password) {
        this.playerName = playerName;
        this.password = password;
    }
}
