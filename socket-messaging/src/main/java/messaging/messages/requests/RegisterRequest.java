package messaging.messages.requests;

import messaging.messages.Message;

public class RegisterRequest extends Message {
    public final String playerName;
    public final String password;
    public final boolean multiPlayer;

    public RegisterRequest(String playerName, String password, boolean multiPlayer) {
        this.playerName = playerName;
        this.password = password;
        this.multiPlayer = multiPlayer;
    }
}
