package messaging.messages.responses;

import messaging.messages.Message;

public class NotifyWhenReadyResponse extends Message {
    public final Integer playerNumber;

    public NotifyWhenReadyResponse(Integer playerNumber, boolean success) {
        this.playerNumber = playerNumber;
        super.success = success;
    }
}
