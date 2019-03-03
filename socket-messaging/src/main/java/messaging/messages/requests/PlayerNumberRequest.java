package messaging.messages.requests;

import messaging.messages.Message;

public class PlayerNumberRequest extends Message {
    public final String playerName;

    public PlayerNumberRequest(String playerName) {

        this.playerName = playerName;
    }
}