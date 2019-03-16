package messaging.messages.requests;

import messaging.messages.Message;

public class NotifyWhenReadyRequest extends Message {
    public final int playerNumber;

    public NotifyWhenReadyRequest(int playerNumber) {
        this.playerNumber = playerNumber;
    }
}