package messaging.messages.responses;

import messaging.messages.Message;

public class PlayerNumberResponse extends Message {
    public final Integer playerNumber;

    public PlayerNumberResponse(Integer playerNumber, boolean success) {
        this.playerNumber = playerNumber;
        super.success = success;
    }
}
