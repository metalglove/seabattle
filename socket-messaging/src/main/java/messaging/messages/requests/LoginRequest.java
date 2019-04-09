package messaging.messages.requests;

import messaging.messages.Message;

public class LoginRequest extends Message {
    private final String playerName;
    private final String password;
    private final boolean multiPlayer;

    public LoginRequest(String playerName, String password, boolean multiPlayer) {
        this.playerName = playerName;
        this.password = password;
        this.multiPlayer = multiPlayer;
    }

    public boolean isMultiPlayer() {
        return multiPlayer;
    }

    public String getPassword() {
        return password;
    }

    public String getPlayerName() {
        return playerName;
    }
}
