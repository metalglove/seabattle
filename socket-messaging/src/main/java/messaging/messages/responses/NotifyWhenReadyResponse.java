package messaging.messages.responses;

import messaging.messages.Message;

public class NotifyWhenReadyResponse extends Message {
    public final Integer playerNumber;
    public final boolean isPlayersTurn;

    public NotifyWhenReadyResponse(Integer playerNumber, boolean success, boolean isPlayersTurn) {
        this.playerNumber = playerNumber;
        this.isPlayersTurn = isPlayersTurn;
        super.success = success;
    }
}
